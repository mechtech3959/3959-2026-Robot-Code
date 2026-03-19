package frc.robot.subsystems.conveyor;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ConveyorSubsystem extends SubsystemBase {
    private final ConveyorIO conveyorIO;

    public ConveyorSubsystem(ConveyorIO io) {
        this.conveyorIO = io;
    }

    private ConveyorStates currentConveyorState = ConveyorStates.STOP;

    private void applyState() {
        switch (currentConveyorState) {
            case RUN -> conveyorIO.runConveyorMotor();
            case STOP -> conveyorIO.stopConveyorMotor();
            default -> System.out.println(
                    "Error in Conveyor Subsystem: State applied to " + "non-existing option/undefined error.");

        }
    }

    public void changeState(ConveyorStates state) {
        this.currentConveyorState = state;
    }

    @Override
    public void periodic() {
        Logger.recordOutput(" Conveyor State", currentConveyorState.toString());
        applyState();
    }

    public enum ConveyorStates {
        RUN,
        STOP
    }
}
