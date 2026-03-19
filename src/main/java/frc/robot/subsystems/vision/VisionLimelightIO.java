package frc.robot.subsystems.vision;

import java.util.List;
import com.ctre.phoenix6.Utils;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import frc.robot.generated.LimelightHelpers;
import frc.robot.util.FieldBasedConstants;
import org.littletonrobotics.junction.Logger;

import frc.robot.util.TagMap;

public class VisionLimelightIO implements VisionIO {

    public double TX;
    public double TY;
    public double TA;
    public boolean TV;
    public String pipeLine;
    public Pose2d foundPosition;
    public double timeStamp;
    private LimelightHelpers.PoseEstimate limelightMeasurement;
    private String cameraType;
    private double distanceEstimate;
    private double yawDegrees;
    private double targetHeight;
    private double limelightHeight;
    private double limelightAngle;
    private int[] redHubTags = { 2, 3, 4, 5, 8, 9, 10, 11 };
    private int[] blueHubTags = { 18, 19, 20, 21, 24, 25, 26, 27 };

    private double goalHeight = Units.inchesToMeters(44.25);
    private LimelightHelpers.RawFiducial[] detectedTags;
    private double yaw;

    public VisionLimelightIO(String pipeLine, String cameraType, double limelightHeight, double limelightAngle,
            double yaw) {
        this.pipeLine = pipeLine;
        this.cameraType = cameraType;
        this.limelightHeight = limelightHeight;
        this.limelightAngle = limelightAngle;
        this.targetHeight = TagMap.getTagHeight(2);
        // this.detectedTags = new int[0];
        this.yaw = yaw;

    }

    @Override
    public LimelightHelpers.PoseEstimate getPoseEstimate() {
        return limelightMeasurement;
    }

    @Override
    public double estimatedDistanceToTarget() {
        // LimelightHelpers.setPipelineIndex(pipeLine, 1);

        // double _TY = LimelightHelpers.getTY(pipeLine);
        if (FieldBasedConstants.isBlueAlliance()) {
            LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, blueHubTags);
        } else {
            LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, redHubTags);
        }
        if (!TV) {
            return -1;
        }
        /*
         * double angleToTargetDegrees = limelightAngle + _TY;
         * double angleToTargetRadians = Math.toRadians(
         * angleToTargetDegrees);
         * 
         * Logger.recordOutput("POSEESTDIST", getPoseEstimate().avgTagDist);
         * 
         * return (goalHeight - limelightHeight) / Math.tan(angleToTargetRadians);
         */
        double avgTagDist = getPoseEstimate().avgTagDist;
        LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, null);
        return avgTagDist;
    }

    @Override
    public void updateTracking(double yawDegrees) {
        LimelightHelpers.SetRobotOrientation(pipeLine, yawDegrees + yaw, 0, 0, 0, 0, 0);
        limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(pipeLine);
        if (limelightMeasurement != null) {
            foundPosition = limelightMeasurement.pose;
            double latency = (limelightMeasurement.latency) / 1000.0;
            timeStamp = Utils.getCurrentTimeSeconds() - latency;
            limelightMeasurement.timestampSeconds = timeStamp;
        }
    }

    

    @Override
    public void trackingStart() {
        TX = LimelightHelpers.getTX(pipeLine);
        TY = LimelightHelpers.getTY(pipeLine);
        TA = LimelightHelpers.getTA(pipeLine);
        TV = LimelightHelpers.getTV(pipeLine);

    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        /* 
        inputs.TX = LimelightHelpers.getTX(pipeLine);
        inputs.TY = LimelightHelpers.getTY(pipeLine);
        inputs.TA = LimelightHelpers.getTA(pipeLine);
        inputs.TV = LimelightHelpers.getTV(pipeLine);
        inputs.pipeLine = pipeLine;
        if (limelightMeasurement != null) {

            timeStamp = Utils.getCurrentTimeSeconds() -
                    (LimelightHelpers.getLatency_Pipeline(pipeLine) / 1000.0) -
                    (LimelightHelpers.getLatency_Capture(pipeLine) / 1000.0);
            limelightMeasurement.timestampSeconds = timeStamp;
            foundPosition = limelightMeasurement.pose;
            // timeStamp = limelightMeasurement.timestampSeconds;
        }*/
    }

    public void setPipeline(int index) {
        LimelightHelpers.setPipelineIndex(pipeLine, index);
    }
}