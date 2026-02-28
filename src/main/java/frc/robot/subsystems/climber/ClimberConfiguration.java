package frc.robot.subsystems.climber;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberConfiguration {
    private final SparkMaxConfig climberMotorConfig = new SparkMaxConfig();

    public ClimberConfiguration() {
        climberMotorConfig.smartCurrentLimit(40, 20);
        climberMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);

        // Add to release hook, subtract to raise hook
        climberMotorConfig.softLimit.forwardSoftLimit(200.0)
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(1.0)
                .reverseSoftLimitEnabled(true);

        climberMotorConfig.alternateEncoder
                .inverted(false)
                // 1 Rotation = 2 * PI Radians
                .positionConversionFactor(2.0 * Math.PI)
                // 1 RPM = (2 * PI) / 60 Radians per Second
                .velocityConversionFactor((2.0 * Math.PI) / 60.0);

        climberMotorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                .outputRange(-1.0, 1.0);

        // Use small numbers for the high ratio
        climberMotorConfig.closedLoop.pid(0.01, 0.0, 0.01)
                .feedForward.kS(0.15)
                .kV(0.12)
                .kCos(0.2)
                .kCosRatio(1.0); // Using 1.0 because the code is programed with radians

        climberMotorConfig.closedLoop.maxMotion
                .cruiseVelocity(2000)       // 2000 rpm
                .maxAcceleration(1000)      // 2000/1000 = 2 sec to reach 2000 rpm
                .allowedProfileError(0.5); // Deadband
    }

    public SparkMaxConfig getClimberMotorConfig() {
        return climberMotorConfig;
    }
}
