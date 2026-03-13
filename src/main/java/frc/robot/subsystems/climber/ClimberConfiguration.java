package frc.robot.subsystems.climber;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.*;

public class ClimberConfiguration {
    private final TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    public ClimberConfiguration() {
        Slot0Configs slot0 = new Slot0Configs()
                .withGravityType(GravityTypeValue.Arm_Cosine)
                .withKG(0.1) // Voltage to overcome gravity
                .withKS(0)   // Static voltage to move
                .withKV(0.1) // Cruising voltage
                .withKP(50)   // Present error
                .withKI(0)   // Past error
                .withKD(0.1) // Future error
                .withStaticFeedforwardSign(
                        StaticFeedforwardSignValue.UseClosedLoopSign);

        MotionMagicConfigs motionMagicConfig =
                new MotionMagicConfigs()
                        .withMotionMagicCruiseVelocity(160) // 80 rps
                        .withMotionMagicAcceleration(320)  // Reach 80 rps in 0.5 seconds
                        .withMotionMagicJerk(3200);        // S-Curve

        FeedbackConfigs feedbackConfigs =
                new FeedbackConfigs()
                        .withFeedbackRemoteSensorID(23)
                        .withFeedbackSensorSource(FeedbackSensorSourceValue.FusedCANcoder)
                        .withSensorToMechanismRatio(28.0 / 12.0)
                        .withRotorToSensorRatio(135);

        MotorOutputConfigs motorOutputConfigs =
                new MotorOutputConfigs()
                        //.withInverted(InvertedValue.Clockwise_Positive)
                        .withNeutralMode(NeutralModeValue.Brake);

        CurrentLimitsConfigs currentLimits =
                new CurrentLimitsConfigs()
                        .withStatorCurrentLimit(100)
                        .withStatorCurrentLimitEnable(true)
                        .withSupplyCurrentLimit(50)
                        .withSupplyCurrentLimitEnable(true);

        SoftwareLimitSwitchConfigs softwareLimits =
                new SoftwareLimitSwitchConfigs()
                        .withForwardSoftLimitEnable(true) // Disabled for testing
                        .withForwardSoftLimitThreshold(230.5)
                        .withReverseSoftLimitEnable(true) // Disabled for testing
                        .withReverseSoftLimitThreshold(-0.5);

        talonConfig.withSlot0(slot0)
                .withMotionMagic(motionMagicConfig)
                .withFeedback(feedbackConfigs)
                .withMotorOutput(motorOutputConfigs)
                .withCurrentLimits(currentLimits)
                .withSoftwareLimitSwitch(softwareLimits);
    }

    public TalonFXConfiguration getClimberMotorConfig() {
        return talonConfig;
    }
}
