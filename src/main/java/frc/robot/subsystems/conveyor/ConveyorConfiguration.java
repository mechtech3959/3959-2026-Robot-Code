package frc.robot.subsystems.conveyor;

import com.revrobotics.spark.config.SparkMaxConfig;

public class ConveyorConfiguration {
    private final SparkMaxConfig conveyorMotorConfig = new SparkMaxConfig();

    public ConveyorConfiguration() {
        conveyorMotorConfig.smartCurrentLimit(15, 10);
        conveyorMotorConfig.inverted(true);
        conveyorMotorConfig.idleMode(SparkMaxConfig.IdleMode.kCoast);
    }

    public SparkMaxConfig getConfig() {
        return conveyorMotorConfig;
    }

}
