package frc.robot.util;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose2d;

public class BaseCalculator {
    public static Rotation2d angleToAlign(Pose2d position) {
        double y1 = FieldBasedConstants.isBlueAlliance() ? FieldBasedConstants.blueBase.getY()
                : FieldBasedConstants.redBase.getY();
        double x1 = FieldBasedConstants.isBlueAlliance() ? FieldBasedConstants.blueBase.getX()
                : FieldBasedConstants.redBase.getX();
        double y2 = position.getY();
        double x2 = position.getX();
        double yDist = y2 - y1;
        double xDist = x2 - x1;
        Rotation2d Phi = new Rotation2d(Math.atan2(yDist, xDist));
        return Phi.rotateBy( Rotation2d.k180deg);

    }
}
