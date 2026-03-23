package frc.robot.util;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.DriverStation;

public class FieldBasedConstants {
    public static boolean isBlueAlliance() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isEmpty()) {
            // Don't guess — return a safe default and log it
            System.out.println("WARNING: Alliance not set by driver station!");
            return true; // or false depending on which side you practice on
        }
        return alliance.get() == DriverStation.Alliance.Blue;
    }

    private static final Pose3d blueBase = new Pose3d(4.611, 4.034, 3.0, new Rotation3d(new Rotation2d(0)));
    private static final Pose3d redBase = new Pose3d(11.901, 4.034, 3.0, new Rotation3d(new Rotation2d(0)));

    public static Pose3d getBlueBase() {
        return new Pose3d(blueBase.getX(), blueBase.getY(), blueBase.getZ(), blueBase.getRotation());
    }

    public static Pose3d getRedBase() {
        return new Pose3d(redBase.getX(), redBase.getY(), redBase.getZ(), redBase.getRotation());
    }

}
