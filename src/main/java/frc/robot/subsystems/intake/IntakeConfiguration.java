package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeConfiguration {
        private final SparkFlexConfig sparkMotorConfig = new SparkFlexConfig();

        public IntakeConfiguration() {
                sparkMotorConfig.closedLoop.feedbackSensor(FeedbackSensor.kDetachedAbsoluteEncoder, 43)
                                .pid(1.65, 0.0005, 0).iZone(0.03)
                                .iMaxAccum(0.05).feedForward.kS(0.1).kV(0.2); // .kCosRatio(2.0 * Math.PI);
                sparkMotorConfig.smartCurrentLimit(20, 40);

                sparkMotorConfig.idleMode(IdleMode.kBrake);
                sparkMotorConfig.inverted(true);

                sparkMotorConfig.softLimit
                                .forwardSoftLimit(0.314)
                                .forwardSoftLimitEnabled(true)
                                .reverseSoftLimit(0.005)
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
