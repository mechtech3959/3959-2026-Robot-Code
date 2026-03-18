package frc.robot.subsystems.climber;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.util.Units;

public class ClimberCTREIO implements ClimberIO {
    TalonFX climberMotor = new TalonFX(18, new CANBus("rio"));
    MotionMagicVoltage request = new MotionMagicVoltage(0);
    private double target = 0;
    private static final double POSITION_TOLERANCE_DEGREES = 0.5;

    public ClimberCTREIO() {
        TalonFXConfiguration climberMotorConfig = new ClimberConfiguration().getClimberMotorConfig();
        climberMotor.getConfigurator().apply(climberMotorConfig);
    }

    @Override
    public void setPosition(double position) {
         if (position != target) {
        climberMotor.setControl(request.withPosition(Units.degreesToRotations(position)));
        target = position;
         }
    }

    @Override
    public void stop() {
        climberMotor.stopMotor();
    }

    @Override
    public double getPosition() {
        return climberMotor.getPosition().getValueAsDouble();
    }

    @Override
    public boolean isAtTarget() {
        // Convert motor position (rotations) to degrees to match stored target units
        double positionDegrees = Units.rotationsToDegrees(climberMotor.getPosition().getValueAsDouble());
        return Math.abs(positionDegrees - target) <= POSITION_TOLERANCE_DEGREES;
    }

    @Override
    public void updateInputs(ClimberIOInputs inputs) {
        inputs.position = Units.rotationsToDegrees(climberMotor.getPosition().getValueAsDouble());
        inputs.appliedVolts = climberMotor.getMotorVoltage().getValueAsDouble();
        inputs.currentAmps = climberMotor.getSupplyCurrent().getValueAsDouble();
        inputs.tempCelsius = climberMotor.getDeviceTemp().getValue().magnitude();
        inputs.target = target;
    }
}