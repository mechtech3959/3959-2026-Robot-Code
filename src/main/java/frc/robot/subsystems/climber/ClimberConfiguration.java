package frc.robot.subsystems.climber;

import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberConfiguration {
    private SparkMaxConfig climberMotorConfig = new SparkMaxConfig();

    public ClimberConfiguration() {
        climberMotorConfig.smartCurrentLimit(40, 20);
        climberMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);

        climberMotorConfig.softLimit.forwardSoftLimit(1)
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(0.1)
                .reverseSoftLimitEnabled(true);

        climberMotorConfig.closedLoop.pid(15.0, 0.0, 0.0)
                .feedForward.kS(0.15)
                .kV(0.12)
                .kCos(0.2)
                // kCosRatio = (Gear Ratio) * (2 * PI) = converts Rotations to Radians
                .kCosRatio(9.0 * 2.0 * Math.PI);

        climberMotorConfig.closedLoop.maxMotion
                .cruiseVelocity(2000)       // 2000 rpm
                .maxAcceleration(4000)      // 2000/4000 = 0.5 sec to reach 2000 rpm
                .allowedProfileError(0.01); // Deadband
    }

    public SparkMaxConfig getClimberMotorConfig() {
        return climberMotorConfig;
    }
}
