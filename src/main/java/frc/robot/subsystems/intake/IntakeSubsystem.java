package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    private final IntakeIO intakeIO;
    private IntakeStates currentIntakeState = IntakeStates.START;

    public IntakeSubsystem(IntakeIO io) {
        this.intakeIO = io;
    }

    private void applyState() {
        switch (currentIntakeState) {
            case STOW:
                intakeIO.setControl(0);
                break;
            case MID_STOW:
                intakeIO.setControl(1);
                break;
            case INTAKE:
                intakeIO.setControl(2);
                break;
            default:
                System.out.println("Error in Intake Subsystem: State applied to "
                        + "non-existing option/undefined error.");
                break;
        }
    }

    public void setIntakeState(IntakeStates state) {
        this.currentIntakeState = state;
    }

    @Override
    public void periodic() {
        applyState();
    }

    public enum IntakeStates {STOW, MID_STOW, INTAKE, START}
}
