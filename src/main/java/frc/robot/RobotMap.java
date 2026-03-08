package frc.robot;

import com.ctre.phoenix6.CANBus;

public class RobotMap {
    public static class CAN {
        public static final CANBus DRIVE_BUS = new CANBus("Drive");
        public static final CANBus SUPER_BUS = new CANBus("Super");

    }

    public static class SHOOTER {
        public static final int LEFT_SHOOTER = 16;
        public static final int RIGHT_SHOOTER = 15;
    }
    public static class INTAKE {
        public static final int FEED_MOTOR = 18;
        public static final int AXIS_MOTOR = 19;
    }
}
