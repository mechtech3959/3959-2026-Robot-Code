package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {
    private final IndexorIO indexerIO;

    public IndexerSubsystem(IndexorIO io) {
        this.indexerIO = io;
    }

    private IndexerStates currentIndexingState = IndexerStates.STOP;

    private void applyState() {
        switch (currentIndexingState) {
            case RUN:
                indexerIO.runFuelIndexingMotor();
                break;
            case STOP:
                indexerIO.stopFuelIndexingMotor();
                break;
            default:
                System.out.println(
                        "Error in Fuel Indexing Subsystem: State applied to " + "non-existing option/undefined error.");
                break;
        }
    }

    public void changeState(IndexerStates state) {
        this.currentIndexingState = state;
    }

    @Override
    public void periodic() {
        applyState();
    }

    public enum IndexerStates {
        RUN,
        STOP
    }
}