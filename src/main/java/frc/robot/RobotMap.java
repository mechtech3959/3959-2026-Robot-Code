package frc.robot;

import com.ctre.phoenix6.CANBus;

public class RobotMap {
    public static class CAN {
        public static final CANBus FAST_BUS = new CANBus("High Speed");
        public static final CANBus SLOW_BUS = new CANBus("rio");

    }

    public static class SHOOTER {
        public static final int LEFT_SHOOTER = 16;
        public static final int RIGHT_SHOOTER = 15;
    }
    public static class INTAKE {
        public static final int FEED_MOTOR = 14;
        public static final int AXIS_MOTOR = 19;
        //DO NOT USE THIS CAN ID, IT IS FOR TESTING PURPOSES ONLY. REPLACE WITH PROPER CAN ID BEFORE COMPETITION
        public static final int ENCODER = 43;
    }
    public static class INDEXER{
        public static final int INDEXER_MOTOR =20;
    }
    public static class CONVEYOR{
        public static final int CONVEYOR_MOTOR = 22;
    }
}
