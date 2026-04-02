package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
  @AutoLog
  public class IntakeIOInputs {
    public double axisPosition = 0;
    public double axisVelocity = 0;
    public double axisTarget = 0;
    public double axisCurrent = 0;
    public double axisTemperature = 0;
    public boolean isAtTarget = false;

  }

  default void setControl(double position) {
  }

  default void stop() {
  }

  default double getPosition() {
    return 0;
  }
  default void updateInputs(IntakeIOInputs inputs) {
  }
}
