package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {
    @AutoLog
    public class indexerIOInputs{
        double indexerSpeed = 0;
        double indexerCurrent = 0;
        double indexerTemperature = 0;
    }
    default void runForwardMotor() {
    }

    default void stopMotor() {
    }
        default void updateInputs(indexerIOInputs inputs) {
        }
}