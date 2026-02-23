package frc.robot.subsystems.climber;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberREVIO implements ClimberIO {
    private final SparkMax climberMotor = new SparkMax(1, SparkLowLevel.MotorType.kBrushless);
    ClimberConfiguration climberConfig = new ClimberConfiguration();

    public ClimberREVIO() {
        climberMotor.configure(climberConfig.getClimberMotorConfig(), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void setPosition(double position) {
        // Update to a quicker PID to move out of the way faster
        SparkMaxConfig climberMotorConfig = climberConfig.getClimberMotorConfig();
        climberMotorConfig.closedLoop.pid(15.0, 0.0, 0.0);
        climberMotor.configure(climberMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        climberMotor.getClosedLoopController().setSetpoint(position, SparkBase.ControlType.kMAXMotionPositionControl);
    }

    public void stop() {
        climberMotor.stopMotor();
    }

    public double getPosition() {
        return climberMotor.getEncoder().getPosition();
    }

    public void climb() {
        // Update to a slower PID so we do not kill the robot
        SparkMaxConfig climberMotorConfig = climberConfig.getClimberMotorConfig();
        climberMotorConfig.closedLoop.pid(2.0, 1.0, 0.0);
        climberMotor.configure(climberMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        climberMotor.getClosedLoopController().setSetpoint(0, SparkBase.ControlType.kMAXMotionPositionControl);
    }
}
