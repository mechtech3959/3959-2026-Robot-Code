package frc.robot.subsystems.drivetrain;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

public interface DrivetrainIO {

    @AutoLog
    public class DrivetrainIOInputs {
        public Pose2d Pose = new Pose2d(0, 0, new Rotation2d(0));
        public ChassisSpeeds Speeds = new ChassisSpeeds(0, 0, 0);
        public SwerveModuleState[] ModuleStates;
        public SwerveModuleState[] ModuleTargets;
        public SwerveModulePosition[] ModulePositions;
        public Rotation2d RawHeading = new Rotation2d(0);
        public double Timestamp;
        public double OdometryPeriod;
        public int SuccessfulDaqs;
        public int FailedDaqs;

        void logState(SwerveDrivetrain.SwerveDriveState state) {
            this.Pose = state.Pose;
            this.RawHeading = state.RawHeading;
            this.ModuleStates = state.ModuleStates;
            this.ModuleTargets = state.ModuleTargets;
            this.ModulePositions = state.ModulePositions;
            this.Speeds = state.Speeds;
            this.SuccessfulDaqs = state.SuccessfulDaqs;
            this.FailedDaqs = state.FailedDaqs;
            this.OdometryPeriod = state.OdometryPeriod;
            this.Timestamp = state.Timestamp;

        }
    }

    default void registerDrivetrainTelemetry(DrivetrainIOInputs inputs) {
    }

    default void updateDrivetrainData(DrivetrainIOInputs inputs) {
    }

    default SwerveModule<TalonFX, TalonFX, CANcoder> getSwerveModule(int index) {
        throw new UnsupportedOperationException(
                "DrivetrainIO.getSwerveModule(int) must be implemented by concrete drivetrain classes.");
    }

    default void configure() {
    }

    default void updateInputs(DrivetrainIOInputs inputs) {
    }

    default void resetHeading(Rotation2d heading) {
    }

    default Pose2d getPose() {
        return new Pose2d();
    }

    default void resetRobotPose(Pose2d pose) {
    }

    default void setSwerveState(SwerveRequest req) {
    }

    default void setPoseEstValues(Pose2d pose, double timestamp, Matrix<N3, N1> dev) {
    }

    default ChassisSpeeds getRobotRelSpeed() {
        return new ChassisSpeeds();
    }

    default void trajPath(ChassisSpeeds speeds) {
    };

    default void simulationInit() {
    }

    default void simulationPeriodic() {
    }
    default void seedField(){}
    default void periodic() {
    }

}
