package frc.robot.subsystems.conveyor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ConveyorSubsystem extends SubsystemBase {
    private final ConveyorIO conveyorIO;

    public ConveyorSubsystem(ConveyorIO io) {
        this.conveyorIO = io;
    }

    private ConveyorStates currentConveyorState = ConveyorStates.STOP;

    private void applyState() {
        switch (currentConveyorState) {
            case RUN:
                conveyorIO.runConveyorMotor();
                break;
            case STOP:
                conveyorIO.stopConveyorMotor();
                break;
            default:
                System.out.println(
                        "Error in Conveyor Subsystem: State applied to " + "non-existing option/undefined error.");
                break;

        }
    }

    public void setConveyorState(ConveyorStates state) {
        this.currentConveyorState = state;
    }

    @Override
    public void periodic() {
        applyState();
    }

    public enum ConveyorStates {
        RUN,
        STOP
    }
}
