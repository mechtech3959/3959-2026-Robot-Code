package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.util.Units;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private final ClimberIO climberIO;
    private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
    public double cameraCustomPosition = 0.6; // Init clear of intake
    private ClimberStates currentClimberState = ClimberStates.HOME;

    public ClimberSubsystem(ClimberIO climberIO) {
        this.climberIO = climberIO;
    }

    public void changeState(ClimberStates state) {
        this.currentClimberState = state;
    }

    private void applyState() {
        switch (currentClimberState) {
            case HOME ->
                climberIO.setPosition(-30);
            case CLEAR_INTAKE ->
                climberIO.setPosition(65);
            case CLIMB ->
                climberIO.setPosition(-25);
            case STARTING_CONFIG ->
                climberIO.setPosition(-30);
            case CAMERA_CUSTOM ->
                climberIO.setPosition(cameraCustomPosition);
            // May not work how I think it will, but use the controller thumbstick
            // input to slowly add or subtract this number to move the camera for the
            // driver.
            default ->
                System.out.println("Error in Climber Subsystem: State applied to "
                        + "non-existing option/undefined error.");
        }
    }

    public double getPosition() {
        return Units.rotationsToDegrees(climberIO.getPosition());
    }

    public boolean isAtTarget() {

        return climberIO.isAtTarget();
    }

    public ClimberStates getState() {
        return currentClimberState;
    }

    @Override
    public void periodic() {
        Logger.recordOutput("States/Climber", currentClimberState.toString());

        applyState();
        climberIO.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
    }

    public enum ClimberStates {
        HOME, CLEAR_INTAKE, CLIMB, CAMERA_CUSTOM, STARTING_CONFIG
    }
}