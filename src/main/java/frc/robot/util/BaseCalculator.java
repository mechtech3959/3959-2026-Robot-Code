package frc.robot.util;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose2d;

public class BaseCalculator {
    public static Rotation2d angleToAlign(Pose2d position) {
        boolean isBlue = FieldBasedConstants.isBlueAlliance();
        double y1 = isBlue ? FieldBasedConstants.getBlueBase().getY()
                : FieldBasedConstants.getRedBase().getY();
        double x1 = isBlue ? FieldBasedConstants.getBlueBase().getX()
                : FieldBasedConstants.getRedBase().getX();
        double y2 = position.getY();
        double x2 = position.getX();
        double yDist = y2 - y1;
        double xDist = x2 - x1;
        Rotation2d phi = new Rotation2d(Math.atan2(yDist, xDist));
        return phi.rotateBy(Rotation2d.k180deg);

    }
}
