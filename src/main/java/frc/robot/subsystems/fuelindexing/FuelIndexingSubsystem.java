package frc.robot.subsystems.fuelindexing;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FuelIndexingSubsystem extends SubsystemBase {
    private final FuelIndexingIO fuelIndexingIO;

    public FuelIndexingSubsystem(FuelIndexingIO io) {
        this.fuelIndexingIO = io;
    }

    private FuelIndexingStates currentFuelIndexingState = FuelIndexingStates.STOP;

    private void applyState() {
        switch(currentFuelIndexingState) {
            case RUN:
                fuelIndexingIO.runFuelIndexingMotor();
                break;
            case STOP:
                fuelIndexingIO.stopFuelIndexingMotor();
                break;
            default:
                System.out.println("Error in Fuel Indexing Subsystem: State applied to " + "non-existing option/undefined error.");
                break;
        }
    }

    public void setFuelIndexingState(FuelIndexingStates state) {
        this.currentFuelIndexingState = state;
    }

    @Override
    public void periodic() {applyState();}

    public enum FuelIndexingStates {
        RUN,
        STOP
    }
}