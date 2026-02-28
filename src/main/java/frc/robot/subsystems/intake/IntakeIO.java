package frc.robot.subsystems.intake;

public interface IntakeIO {
  default void setControl(double position) {
  }

  default void stop() {
  }

  default double getPosition() {
    return 0;
  }
}
