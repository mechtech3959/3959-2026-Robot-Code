package frc.robot.util;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.DriverStation;

public class FieldBasedConstants {
    public static boolean isBlueAlliance() {
        return DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;
    }

    public static final Pose3d blueBase = new Pose3d(4.611, 4.034, 3.0, new Rotation3d(new Rotation2d(0)));
    public static final Pose3d redBase = new Pose3d(11.901, 4.034, 3.0, new Rotation3d(new Rotation2d(0)));

}
