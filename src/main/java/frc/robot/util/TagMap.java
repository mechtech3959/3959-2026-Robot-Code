package frc.robot.util;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;

public class TagMap {
    private static final AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);

    public static  double getTagHeight(int tagID) {
        return fieldLayout.getTagPose(tagID).get().getZ();
    }
}
