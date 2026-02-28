package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ShooterConfig {
        public static TalonFXConfiguration leftShooterConfiguration() {
                return new TalonFXConfiguration().withCurrentLimits(currentLimits).withSlot0(slot0Configs)
                                // .withMotionMagic(motionMagic)
                                .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive)
                                                .withNeutralMode(NeutralModeValue.Coast));
        }

        public static TalonFXConfiguration rightShooterConfiguration() {
                return new TalonFXConfiguration().withCurrentLimits(currentLimits).withSlot0(slot0Configs)
                                // .withMotionMagic(motionMagic)
                                .withMotorOutput(new MotorOutputConfigs()
                                                .withInverted(InvertedValue.CounterClockwise_Positive)
                                                .withNeutralMode(NeutralModeValue.Coast));
        }

        static CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs().withStatorCurrentLimit(60)
                        .withStatorCurrentLimitEnable(true).withSupplyCurrentLimit(40)
                        .withSupplyCurrentLimitEnable(true);

        static Slot0Configs slot0Configs = new Slot0Configs().withKP(0.5).withKI(7.5).withKD(0).withKS(0).withKV(0.002)
                        .withKA(0);
        static MotionMagicConfigs motionMagic = new MotionMagicConfigs().withMotionMagicAcceleration(5)
                        .withMotionMagicCruiseVelocity(5).withMotionMagicJerk(5);

}
