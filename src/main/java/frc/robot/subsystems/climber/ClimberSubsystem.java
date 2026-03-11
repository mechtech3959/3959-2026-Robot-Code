package frc.robot.subsystems.climber;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {
  private final ClimberIO climberIO;
  private final ClimberIOInputsAutoLogged inputs =
      new ClimberIOInputsAutoLogged();
  public double cameraCustomPosition = 0.6; // Init clear of intake
  private ClimberStates currentClimberState = ClimberStates.HOME;

  public ClimberSubsystem(ClimberIO climberIO) { this.climberIO = climberIO; }

  public void setClimberState(ClimberStates state) {
    this.currentClimberState = state;
  }

  private void applyState() {
    switch (currentClimberState) {
    case HOME ->
      climberIO.setPosition(0);
    case CLEAR_INTAKE ->
      climberIO.setPosition(30);
    case CLIMB ->
      climberIO.setPosition(90);
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

  @Override
  public void periodic() {
    applyState();
    climberIO.updateInputs(inputs);
    Logger.processInputs(getName(), inputs);
  }

  public enum ClimberStates { HOME, CLEAR_INTAKE, CLIMB, CAMERA_CUSTOM }
}
