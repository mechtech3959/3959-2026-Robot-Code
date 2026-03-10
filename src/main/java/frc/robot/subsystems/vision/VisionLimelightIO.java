package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.generated.LimelightHelpers;
import frc.robot.util.FieldBasedConstants;
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
    private int[] redHubTags = {2,3,4,5,8,9,10,11};
    private int[] blueHubTags = {18,19,20,21,24,25,26,27};
    

    public VisionLimelightIO(String pipeLine,String cameraType, double limelightHeight, double limelightAngle) {
        this.pipeLine = pipeLine;
        this.cameraType = cameraType;
        this.limelightHeight = limelightHeight;
        this.limelightAngle = limelightAngle;
        this.targetHeight = TagMap.getTagHeight(2);
    }

    @Override
    public LimelightHelpers.PoseEstimate getPoseEstimate() {
        return limelightMeasurement;
    }
    @Override
    public double estimatedDistanceToTarget() {
     
        if(FieldBasedConstants.isBlueAlliance() ){
            LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, blueHubTags);
        }else{
            LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, redHubTags);
        }
           if (!TV) {
            return -1;
        }
        double angleToTargetDegrees = limelightAngle + TY;
        double angleToTargetRadians = Math.toRadians(angleToTargetDegrees);
        distanceEstimate = (targetHeight - limelightHeight) / Math.tan(angleToTargetRadians);
        return distanceEstimate;
    }

    @Override
    public void setVisionNeutral() {
        TX = 0;
        TY = 0;
        TA = 0;
        TV = false;
        foundPosition = new Pose2d();
        timeStamp = 0;
    }

    @Override
    public void updateTracking(double yawDegrees) {
        LimelightHelpers.SetRobotOrientation(pipeLine, yawDegrees, 0, 0, 0, 0, 0);
        limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(pipeLine);
        if (limelightMeasurement != null) {
            foundPosition = limelightMeasurement.pose;
            timeStamp = limelightMeasurement.timestampSeconds;
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
        inputs.TX = LimelightHelpers.getTX(pipeLine);
        inputs.TY = LimelightHelpers.getTY(pipeLine);
        inputs.TA = LimelightHelpers.getTA(pipeLine);
        inputs.TV = LimelightHelpers.getTV(pipeLine);
        inputs.pipeLine = pipeLine;
        if (limelightMeasurement != null) {
            foundPosition = limelightMeasurement.pose;
            timeStamp = limelightMeasurement.timestampSeconds;
        }
    }

}