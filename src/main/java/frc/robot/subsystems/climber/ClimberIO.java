package frc.robot.subsystems.climber;

public interface ClimberIO {
    void setPosition(double position);

    void stop();

    double getPosition();

    void climb();
}
