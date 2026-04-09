package frc.robot.subsystems.indexer;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import frc.robot.RobotMap;

public class IndexerREVIO implements IndexerIO {
    
    private final SparkMax fuelIndexingMotor = new SparkMax(RobotMap.INDEXER.INDEXER_MOTOR, MotorType.kBrushless);

    public IndexerREVIO() {
        
        IndexerConfiguration fuelIndexingMotorConfig = new IndexerConfiguration();
        fuelIndexingMotor.configure(fuelIndexingMotorConfig.getConfig(), ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
    }

    @Override
    public void runForwardMotor() {
        fuelIndexingMotor.set(1);
    }

    @Override
    public void stopMotor() {
        fuelIndexingMotor.set(0);
    }
    @Override
    public void updateInputs(indexerIOInputs inputs) {
        inputs.indexerCurrent = fuelIndexingMotor.getOutputCurrent();
        inputs.indexerSpeed = fuelIndexingMotor.get();
        inputs.indexerTemperature = fuelIndexingMotor.getMotorTemperature();
    }
}
