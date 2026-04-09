package frc.robot.subsystems.intake;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;

public class IntakeConfiguration {
        private final SparkFlexConfig sparkMotorConfig = new SparkFlexConfig();

        public IntakeConfiguration() {
                // p = 1.65
                sparkMotorConfig.closedLoop.feedbackSensor(FeedbackSensor.kDetachedAbsoluteEncoder, 43)
                                .pid(1.8, 0.00, 0).iMaxAccum(0.0).iZone(0.0).feedForward.kS(0.1).kV(0.0).kG(0

                );

                sparkMotorConfig.smartCurrentLimit(30, 10);

                sparkMotorConfig.idleMode(IdleMode.kBrake);
                sparkMotorConfig.inverted(true);

                sparkMotorConfig.softLimit
                                .forwardSoftLimit(0.312)
                                .forwardSoftLimitEnabled(true)
                                .reverseSoftLimit(0.00)
                                .reverseSoftLimitEnabled(true);

                sparkMotorConfig.closedLoop.maxMotion
                                .cruiseVelocity(20) // 1.5 RAD/s = 14.3 rpm
                                .maxAcceleration(15) // 0.5 sec to reach 1.5 RAD/s
                                .allowedProfileError(1); // Deadband

        }

        public SparkFlexConfig getSparkMotorConfig() {
                return sparkMotorConfig;
        }

}
