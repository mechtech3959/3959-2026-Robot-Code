package frc.robot.subsystems.indexer;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

public class IndexerREVIO implements IndexorIO {
    private final SparkMax fuelIndexingMotor = new SparkMax(30, MotorType.kBrushless);

    public IndexerREVIO() {
        IndexerConfiguration fuelIndexingMotorConfig = new IndexerConfiguration();
        fuelIndexingMotor.configure(fuelIndexingMotorConfig.getConfig(), ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
    }

    @Override
    public void runFuelIndexingMotor() {
        fuelIndexingMotor.set(0.5);
    }

    @Override
    public void stopFuelIndexingMotor() {
        fuelIndexingMotor.set(0);
    }
}
