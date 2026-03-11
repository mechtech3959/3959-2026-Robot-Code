package frc.robot.subsystems.climber;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberConfiguration {
    private final SparkMaxConfig climberMotorConfig = new SparkMaxConfig();

    public ClimberConfiguration() {
        climberMotorConfig.smartCurrentLimit(40, 20);
        climberMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        // Add to release hook, subtract to raise hook
        climberMotorConfig.softLimit.forwardSoftLimit(4.0) // 6.28 radians is one full rotation
                .forwardSoftLimitEnabled(false)
                .reverseSoftLimit(1.0) // Leaving room so the hooks don't collide with anything else
                .reverseSoftLimitEnabled(false);

        climberMotorConfig.absoluteEncoder
                .apply(AbsoluteEncoderConfig.Presets.REV_ThroughBoreEncoderV2)
                .setSparkMaxDataPortConfig()
                .inverted(false)
                // Convert rotations (0-1) to radians
                .positionConversionFactor(2.0 * Math.PI)
                // 1 RPM = (2 * PI) / 60 Radians per Second
                .velocityConversionFactor((2.0 * Math.PI) / 60.0);

        // Telling closed loop to use the absolute encoder
        climberMotorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                .outputRange(-1.0, 1.0)
                // Use small numbers for the high ratio
                .pid(0.01, 0.0, 0.01)
                .feedForward.kS(0.15)
                .kV(0.12)
                .kCos(0.2)
                .kCosRatio(1.0); // Using 1.0 because the code is programed with radians

        climberMotorConfig.closedLoop.maxMotion
                .cruiseVelocity(1.5)       // 1.5 radians per second
                .maxAcceleration(3);      // 0.5 sec to reach top speed
    }

    public SparkMaxConfig getClimberMotorConfig() {
        return climberMotorConfig;
    }
}
