package frc.robot.subsystems.intake.feed;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class FeedConfiguration {
    private final TalonFXConfiguration feedMotorConfig = new TalonFXConfiguration();

    public FeedConfiguration() {
        feedMotorConfig.withCurrentLimits(new CurrentLimitsConfigs()
                .withStatorCurrentLimit(40.0)
                .withStatorCurrentLimitEnable(true).withSupplyCurrentLimit(20).withSupplyCurrentLimitEnable(true))
                .withMotorOutput(new MotorOutputConfigs()
                        .withNeutralMode(NeutralModeValue.Coast).withInverted(

                                InvertedValue.Clockwise_Positive));
    }

    public TalonFXConfiguration getConfig() {
        return feedMotorConfig;
    }
}
