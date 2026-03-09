package frc.robot.subsystems.intake;


import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.feed.FeedSubsystem.FeedStates;
import frc.robot.subsystems.intake.feed.FeedSubsystem;

public class IntakeSubsystem extends SubsystemBase {
    public enum IntakeStates {
        STOW,
        MID_STOW,
        INTAKE,
        START,
        TEST
    }

    private final IntakeIO intakeIO;
    private final FeedSubsystem feedSubsystem;
    private IntakeStates currentIntakeState = IntakeStates.START;

    private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

    public IntakeSubsystem(IntakeIO io, FeedSubsystem feedSubsystem) {
        this.intakeIO = io;
        this.feedSubsystem = feedSubsystem;
        
    }

    private void applyState() {
        // TODO WATCH THIS AND CHANGE BEFORE APPLICATION
        switch (currentIntakeState) {
            case STOW ->
                intakeIO.setControl(0);

            case MID_STOW ->
                intakeIO.setControl(0);
            case INTAKE ->
                intakeIO.setControl(0);
            case TEST ->{}
            default -> System.out.println("Error in Intake Subsystem: State applied to "
                    + "non-existing option/undefined error.");
        }
    }

    public void setIntakeState(IntakeStates state) {
        this.currentIntakeState = state;
    }

    public void changeState(IntakeStates state, FeedStates feedState) {
        this.currentIntakeState = state;
        this.feedSubsystem.changeState(feedState);
    }

    public void changeState(IntakeStates state, FeedStates feedState, double feedSpeed) {
        this.currentIntakeState = state;
        this.feedSubsystem.changeState(feedState, feedSpeed);
    }

    @Override
    public void periodic() {
        intakeIO.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
        applyState();
    }

}
