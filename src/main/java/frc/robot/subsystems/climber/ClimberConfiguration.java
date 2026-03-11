package frc.robot.subsystems.climber;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberConfiguration {
    private CANBus canBus = new CANBus("canBus");
    private TalonFX climberMotor = new TalonFX(21, canBus);
    private TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    public ClimberConfiguration() {

    }

    public SparkMaxConfig getClimberMotorConfig() {
        return climberMotorConfig;
    }
}
