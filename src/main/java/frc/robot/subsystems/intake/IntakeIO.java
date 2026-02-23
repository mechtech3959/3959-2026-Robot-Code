package frc.robot.subsystems.intake;

public interface IntakeIO {
    void setControl(double position);

    void stop();

    double getPosition();
}
