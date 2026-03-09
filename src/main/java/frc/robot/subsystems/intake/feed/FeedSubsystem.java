package frc.robot.subsystems.intake.feed;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FeedSubsystem extends SubsystemBase {
    public enum FeedStates {
        RUN,
        PERCENTOUTPUT,
        STOP
    }

    private final FeedIO feedIO;
    private FeedStates currentFeedState = FeedStates.STOP;
    private final FeedIOInputsAutoLogged inputs = new FeedIOInputsAutoLogged();
    private double targetSpeed = 0.0;

    public FeedSubsystem(FeedIO io) {
        this.feedIO = io;
    }

    public void applyState() {
        switch (currentFeedState) {
            case RUN->feedIO.runFeedMotor();
            case STOP -> feedIO.stopFeedMotor();
            case PERCENTOUTPUT -> feedIO.setSpeed(targetSpeed);
            default ->System.out.println("Error in Feed Subsystem: State applied to " +
                    "non-existing option/undefined error.");
        }
    }

    public void setFeedState(FeedStates state) {
        this.currentFeedState = state;
    }
        public void setFeedState(FeedStates state, double speed) {
        this.currentFeedState = state;
        this.targetSpeed = speed;
    }

    @Override
    public void periodic() {
        feedIO.updateInputs(inputs);
        Logger.processInputs(getName() , inputs);
        applyState();

    }

}
