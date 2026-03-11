package frc.robot.subsystems.climber;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

public class ClimberConfiguration {
    private final TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    public ClimberConfiguration() {
        Slot0Configs slot0 = new Slot0Configs()
                .withGravityType(GravityTypeValue.Arm_Cosine)
                .withKG(0.1) // Voltage to overcome gravity
                .withKS(0) // Static voltage to move
                .withKV(0.1) // Cruising voltage
                .withKP(2) // Present error
                .withKI(0) // Past error
                .withKD(0.1) // Future error
                .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);

        MotionMagicConfigs motionMagicConfig = new MotionMagicConfigs()
                .withMotionMagicCruiseVelocity(80) // 80 rps
                .withMotionMagicAcceleration(160) // Reach 80 rps in 0.5 seconds
                .withMotionMagicJerk(1600); // S-Curve

        FeedbackConfigs feedbackConfigs = new FeedbackConfigs()
                .withFeedbackRemoteSensorID(19)
                .withFeedbackSensorSource(FeedbackSensorSourceValue.FusedCANcoder)
                .withSensorToMechanismRatio(28.0 / 12.0)
                .withRotorToSensorRatio(135);

        CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs()
                .withStatorCurrentLimit(100)
                .withStatorCurrentLimitEnable(true)
                .withSupplyCurrentLimit(50)
                .withSupplyCurrentLimitEnable(true);

        SoftwareLimitSwitchConfigs softwareLimits = new SoftwareLimitSwitchConfigs()
                .withForwardSoftLimitEnable(false) // Disabled for testing
                .withForwardSoftLimitThreshold(6)
                .withReverseSoftLimitEnable(false) // Disabled for testing
                .withReverseSoftLimitThreshold(1);

        talonConfig.withSlot0(slot0)
                .withMotionMagic(motionMagicConfig)
                .withFeedback(feedbackConfigs)
                .withCurrentLimits(currentLimits)
                .withSoftwareLimitSwitch(softwareLimits);
    }

    public TalonFXConfiguration getClimberMotorConfig() {
        return talonConfig;
    }
}
