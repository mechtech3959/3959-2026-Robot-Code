package frc.robot.subsystems.conveyor;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ConveyorSubsystem extends SubsystemBase {
    private final ConveyorIO conveyorIO;

    public ConveyorSubsystem(ConveyorIO io) {
        this.conveyorIO = io;
    }

    private ConveyorStates currentConveyorState = ConveyorStates.STOP;
    private final ConveyorIOInputsAutoLogged inputs = new ConveyorIOInputsAutoLogged();

    private void applyState() {
        switch (currentConveyorState) {
            case RUN -> conveyorIO.runConveyorMotor();
            case STOP -> conveyorIO.stopConveyorMotor();
            case REVERSE -> conveyorIO.reverseConveyorMotor();
            default -> System.out.println(
                    "Error in Conveyor Subsystem: State applied to " + "non-existing option/undefined error.");

        }
    }

    public void changeState(ConveyorStates state) {
        this.currentConveyorState = state;
    }

    @Override
    public void periodic() {
        Logger.recordOutput("States/Conveyor", currentConveyorState.toString());
        conveyorIO.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
        applyState();
    }

    public enum ConveyorStates {
        RUN,
        REVERSE,
        STOP
    }
}
