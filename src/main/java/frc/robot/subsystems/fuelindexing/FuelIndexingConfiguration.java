package frc.robot.subsystems.fuelindexing;

import com.revrobotics.spark.config.SparkMaxConfig;


public class FuelIndexingConfiguration {
    private final SparkMaxConfig fuelIndexingMotorConfig = new SparkMaxConfig();

    public FuelIndexingConfiguration() {
        fuelIndexingMotorConfig.smartCurrentLimit(15,10); //placeholder values, I'm not sure what they need to be
        fuelIndexingMotorConfig.inverted(true);
        fuelIndexingMotorConfig.idleMode(SparkMaxConfig.IdleMode.kCoast);
    }

    public SparkMaxConfig getConfig(){
        return fuelIndexingMotorConfig; 
    }
}