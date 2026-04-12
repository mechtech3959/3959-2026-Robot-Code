package frc.robot.subsystems.conveyor;

import org.littletonrobotics.junction.AutoLog;

public interface ConveyorIO {
    @AutoLog
    public class ConveyorIOInputs {
        public double motorCurrent = 0;
        public double motorVelocity = 0;
        public double motorTemperature = 0;
        
    }
    default void runConveyorMotor() {
    }

    default void stopConveyorMotor() {
    }
    default void reverseConveyorMotor() {
    }
    default void setSpeed(double speed) {
    }
    default void updateInputs(ConveyorIOInputs inputs) {
    }
}