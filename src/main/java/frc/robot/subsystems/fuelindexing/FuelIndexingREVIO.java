package frc.robot.subsystems.fuelindexing;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;


public class FuelIndexingREVIO implements FuelIndexingIO {
    private final SparkMax fuelIndexingMotor = new SparkMax(30,MotorType.kBrushless);

    public FuelIndexingREVIO(){
        FuelIndexingConfiguration fuelIndexingMotorConfig = new FuelIndexingConfiguration();
        fuelIndexingMotor.configure(fuelIndexingMotorConfig.getConfig(), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void runFuelIndexingMotor() { fuelIndexingMotor.set(1);}
    public void stopFuelIndexingMotor() { fuelIndexingMotor.set(0);}
}
