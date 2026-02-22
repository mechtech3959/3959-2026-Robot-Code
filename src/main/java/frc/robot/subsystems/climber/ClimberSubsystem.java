package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private final ClimberIO climberIO;

    public ClimberSubsystem(ClimberIO climberIO) {
        this.climberIO = climberIO;
    }

    private ClimberStates currentClimberState = ClimberStates.HOME;

    public void setClimberState(ClimberStates state){
        this.currentClimberState = state;
    }

    private void applyState(){
        switch (currentClimberState){
            case HOME:
                climberIO.setPosition(0);
                break;
            case CLEAR_INTAKE:
                climberIO.setPosition(0.6);
                break;
            case CLIMB:
                climberIO.climb();
                break;
            default:
                System.out.println("Error in Climber Subsystem: State applied to "
                        + "non-existing option/undefined error.");
                break;
        }
    }

    public enum ClimberStates {
        HOME,
        CLEAR_INTAKE,
        CLIMB
    }

    @Override
    public void periodic() {
        applyState();
    }
}
