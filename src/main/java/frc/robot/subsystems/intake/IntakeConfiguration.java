package frc.robot.subsystems.intake;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;

public class IntakeConfiguration {
     private final SparkFlexConfig intakeConfig = new SparkFlexConfig();
    public IntakeConfiguration() {
        intakeConfig.smartCurrentLimit(40); // Set the smart current limit to 40 amps
        intakeConfig.apply(AbsoluteEncoderConfig.Presets.REV_SplineEncoder);
        intakeConfig.idleMode(IdleMode.kBrake);
        
        intakeConfig.softLimit
                .forwardSoftLimit(5.0)
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(0.1)
                .reverseSoftLimitEnabled(true);
      //  intakeConfig.limitSwitch.forwardLimitSwitchTriggerBehavior(LimitSwitchConfig.Behavior.kStopMovingMotorAndSetPosition).reverseLimitSwitchTriggerBehavior(LimitSwitchConfig.Behavior.kStopMovingMotorAndSetPosition).limitSwitchPositionSensor(FeedbackSensor.kAnalogSensor);
              
        intakeConfig.closedLoop.feedbackSensor(FeedbackSensor.kDetachedAbsoluteEncoder)
                .pid(2.0, 0.0, 0.0).feedForward
                .kS(0.15)
                .kV(0.12)
                .kCos(0.2)
                // kCosRatio = (Gear Ratio) * (2 * PI) = converts Rotations to Radians
                .kCosRatio(9.0 * 2.0 * Math.PI);

        intakeConfig.closedLoop.maxMotion
                .cruiseVelocity(1.5) // 1.5 RAD/s = 14.3 rpm
                .maxAcceleration(3) //  0.5 sec to reach 1.5 RAD/s
                .allowedProfileError(0.01); // Deadband
    }

    public SparkFlexConfig getConfig() {
        return intakeConfig;
    }
}
