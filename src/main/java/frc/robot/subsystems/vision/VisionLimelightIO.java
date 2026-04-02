package frc.robot.subsystems.vision;

import com.ctre.phoenix6.Utils;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.generated.LimelightHelpers;
import frc.robot.util.FieldBasedConstants;

public class VisionLimelightIO implements VisionIO {

    public double TX;
    public double TY;
    public double TA;
    public boolean TV;
    public String pipeLine;
    public Pose2d foundPosition;
    public double timeStamp;
    private LimelightHelpers.PoseEstimate limelightMeasurement;
 //   private String cameraType;
  //  private double distanceEstimate;
   // private double yawDegrees;
    //private double targetHeight;
   // private double limelightHeight;
    //private double limelightAngle;
    private final  int[] redHubTags = { 2, 3, 4, 5, 8, 9, 10, 11 };
    private final int[] blueHubTags = { 18, 19, 20, 21, 24, 25, 26, 27 };

   //  private double yaw;

    public VisionLimelightIO(String pipeLine, String cameraType, double limelightHeight, double limelightAngle,
            double yaw) {
        this.pipeLine = pipeLine;
     //   this.cameraType = cameraType;
      //  this.limelightHeight = limelightHeight;
       // this.limelightAngle = limelightAngle;
       // this.targetHeight = TagMap.getTagHeight(2);
      //  this.yaw = yaw;

    }

    @Override
    public LimelightHelpers.PoseEstimate getPoseEstimate() {
        return limelightMeasurement;
    }

    @Override
    public double estimatedDistanceToTarget() {
     
        //Set the fiducial ID filters to only include the tags on the alliance's hub, so that we get a more accurate distance estimate (since the camera will prioritize closer tags, and the opposing hub's tags are farther away)
        if (FieldBasedConstants.isBlueAlliance()) {
            LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, blueHubTags);
        } else {
            LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, redHubTags);
        }
        if (!TV) {
            return -1;
        }

        double avgTagDist = getPoseEstimate().avgTagDist;
        LimelightHelpers.SetFiducialIDFiltersOverride(pipeLine, null);
        return avgTagDist;
    }

    @Override
    public void updateTracking(double yawDegrees) {
        LimelightHelpers.SetRobotOrientation(pipeLine, yawDegrees, 0, 0, 0, 0, 0);
        limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(pipeLine);
        if (limelightMeasurement != null) {
            foundPosition = limelightMeasurement.pose;
            double latency = (limelightMeasurement.latency) / 1000.0;
            timeStamp = Utils.getCurrentTimeSeconds() - latency;
            limelightMeasurement.timestampSeconds = timeStamp;
        }
    }

    

   
  

    public void setPipeline(int index) {
        LimelightHelpers.setPipelineIndex(pipeLine, index);
    }
}