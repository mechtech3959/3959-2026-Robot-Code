package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ShooterConfig {
        public static TalonFXConfiguration leftShooterConfiguration() {
                return new TalonFXConfiguration().withCurrentLimits(currentLimits).withSlot0(slot0Configs)
                                .withMotionMagic(motionMagic)
                                .withMotorOutput(new MotorOutputConfigs()
                                                .withInverted(InvertedValue.CounterClockwise_Positive)
                                                .withNeutralMode(NeutralModeValue.Coast).withControlTimesyncFreqHz(0));
        }

        public static TalonFXConfiguration rightShooterConfiguration() {
                return new TalonFXConfiguration().withCurrentLimits(currentLimits).withSlot0(slot0Configs)
                                .withSlot1(slot1Configs)
                                .withMotionMagic(motionMagic)
                                .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive)
                                                .withNeutralMode(NeutralModeValue.Coast).withControlTimesyncFreqHz(0));
        }

        static CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs().withStatorCurrentLimit(150)
                        .withStatorCurrentLimitEnable(true).withSupplyCurrentLimit(50)
                        .withSupplyCurrentLimitEnable(true);
        // KI (7.5) 1
        static Slot0Configs slot0Configs = new Slot0Configs().withKP(0.6).withKI(0).withKD(0).withKS(0.14)
                        .withKV(0.108).withKA(0);
        static Slot1Configs slot1Configs = new Slot1Configs().withKP(0.3).withKI(0).withKD(0).withKS(0.05)
                        .withKV(0.05).withKA(0);
        static MotionMagicConfigs motionMagic = new MotionMagicConfigs().withMotionMagicAcceleration(9999)
                        .withMotionMagicCruiseVelocity(0).withMotionMagicJerk(0);

}
