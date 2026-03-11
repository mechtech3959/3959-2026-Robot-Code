package frc.robot.subsystems.climber;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.util.Units;

public class ClimberREVIO implements ClimberIO {
    private final SparkMax climberMotor = new SparkMax(21, SparkLowLevel.MotorType.kBrushless);
    ClimberConfiguration climberConfig = new ClimberConfiguration();

    public ClimberREVIO() {
        climberMotor.configure(climberConfig.getClimberMotorConfig(), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void setPosition(double position) {
        climberMotor.getClosedLoopController().setSetpoint(position, SparkBase.ControlType.kPosition);
    }

    @Override
    public void stop() {
        climberMotor.stopMotor();
    }

    @Override
    public double getPosition() {
        return climberMotor.getEncoder().getPosition();
    }

    @Override
    public void updateInputs(ClimberIOInputs inputs) {
        inputs.position = Units.rotationsToRadians(climberMotor.getEncoder().getPosition());
        inputs.appliedVolts = climberMotor.getAppliedOutput() * climberMotor.getBusVoltage();
        inputs.currentAmps = climberMotor.getOutputCurrent();
        inputs.tempCelsius = climberMotor.getMotorTemperature();
        inputs.velocity = climberMotor.getEncoder().getVelocity();
    }
}
