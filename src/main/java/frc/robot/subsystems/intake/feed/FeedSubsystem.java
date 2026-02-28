package frc.robot.subsystems.intake.feed;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FeedSubsystem extends SubsystemBase {
    private final FeedIO feedIO;
    private FeedStates currentFeedState = FeedStates.STOP;

    public FeedSubsystem(FeedIO io) {
        this.feedIO = io;
    }

    public void applyState() {
        switch (currentFeedState) {
            case RUN:
                feedIO.runFeedMotor();
                break;
            case STOP:
                feedIO.stopFeedMotor();
                break;
            default:
                System.out.println("Error in Feed Subsystem: State applied to " +
                        "non-existing option/undefined error.");
                break;
        }
    }

    public void setFeedState(FeedStates state) {
        this.currentFeedState = state;
    }

    @Override
    public void periodic() {
        applyState();
    }

    public enum FeedStates {RUN, STOP}
}
