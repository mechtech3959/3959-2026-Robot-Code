package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {
    private final IndexerIO indexerIO;

    public IndexerSubsystem(IndexerIO io) {
        this.indexerIO = io;
    }

    private IndexerStates currentIndexingState = IndexerStates.STOP;

    private void applyState() {
        switch (currentIndexingState) {
            case RUN -> indexerIO.runForwardMotor();
            case STOP -> indexerIO.stopMotor();
            default -> System.out.println(
                        "Error in Fuel Indexing Subsystem: State applied to " + "non-existing option/undefined error.");
        }
    }

    public void changeState(IndexerStates state) {
        this.currentIndexingState = state;
    }

    @Override
    public void periodic() {
        Logger.recordOutput("States/Indexer", currentIndexingState.toString());

        applyState();
    }

    public enum IndexerStates {
        RUN,
        STOP
    }
}