package frc.robot.subsystems.climber;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TorqueCurrentConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberConfiguration {
    private CANBus canBus = new CANBus("canBus");
    private TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    public ClimberConfiguration() {

    }

    public TalonFXConfiguration getClimberMotorConfig() {
        return talonConfig
                .withSlot0(new Slot0Configs().withKP(2).withKI(0).withKD(0).withKV(0.1).withKS(0)
                        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign))
                .withMotionMagic(new MotionMagicConfigs())
                .withFeedback(new FeedbackConfigs().withFeedbackRemoteSensorID(19)
                        .withFeedbackSensorSource(FeedbackSensorSourceValue.FusedCANcoder)
                        .withSensorToMechanismRatio(10).withRotorToSensorRatio(300))
                .withCurrentLimits(
                        new CurrentLimitsConfigs().withStatorCurrentLimit(100).withStatorCurrentLimitEnable(true)
                                .withSupplyCurrentLimit(50).withSupplyCurrentLimitEnable(true));
    }
}
