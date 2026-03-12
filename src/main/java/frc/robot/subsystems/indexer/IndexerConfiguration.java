package frc.robot.subsystems.indexer;

import com.revrobotics.spark.config.SparkMaxConfig;

public class IndexerConfiguration {
    private final SparkMaxConfig indexerMotorConfig = new SparkMaxConfig();

    public IndexerConfiguration() {
        indexerMotorConfig.smartCurrentLimit(15, 10); // placeholder values, I'm not sure what they need to be
        indexerMotorConfig.inverted(true);
        indexerMotorConfig.idleMode(SparkMaxConfig.IdleMode.kCoast);

    }

    public SparkMaxConfig getConfig() {
        return indexerMotorConfig;
    }
}