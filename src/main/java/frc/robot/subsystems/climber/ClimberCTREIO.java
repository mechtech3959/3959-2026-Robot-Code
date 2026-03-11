package frc.robot.subsystems.climber;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.util.Units;

public class ClimberCTREIO implements ClimberIO {
    TalonFX climberMotor = new TalonFX(19, new CANBus("Super"));
    MotionMagicVoltage request = new MotionMagicVoltage(0);

    public ClimberCTREIO() {
    }

    @Override
    public void setPosition(double position) {
        climberMotor.setControl(request.withPosition(Units.degreesToRotations(position)));
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
    public void updateInputs(ClimberIOInputs inputs) {
        inputs.position = climberMotor.getPosition().getValueAsDouble();
        inputs.appliedVolts = climberMotor.getMotorVoltage().getValueAsDouble();
        inputs.currentAmps = climberMotor.getSupplyCurrent().getValueAsDouble();
        inputs.tempCelsius = climberMotor.getDeviceTemp().getValue().magnitude();
        
    }
}
