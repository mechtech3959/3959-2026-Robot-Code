package frc.robot.subsystems.climber;

public interface ClimberIO {
    default void setPosition(double position) {
    }

    default void stop() {
    }

    default double getPosition() {
        return 0;
    }

    default void climb() {
    }
}
