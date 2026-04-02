package frc.robot.subsystems.drivetrain;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;

// Inspired by FRC 2910 
public class DrivetrainCTREIO extends SwerveDrivetrain<TalonFX, TalonFX, CANcoder>
        implements DrivetrainIO {
    private static final double SIM_LOOP_PERIOD = 0.004; // 4 ms
    private Notifier simNotifier = null;
    private double lastSimTime;

    public DrivetrainCTREIO(SwerveDrivetrainConstants constants,
            @SuppressWarnings("unchecked") SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>... moduleConstants) {

        super(TalonFX::new, TalonFX::new, CANcoder::new, constants, 250, moduleConstants);

        if (Utils.isSimulation()) {
            startSimThread();
        }

    }

    @Override
    public void registerDrivetrainTelemetry(DrivetrainIOInputs inputs) {
        this.registerTelemetry(state -> {
            SwerveDriveState modifiedState = ((SwerveDriveState) state).clone(); // copy, don't mutate live state
            modifiedState.Speeds = ChassisSpeeds.fromRobotRelativeSpeeds(
                    modifiedState.Speeds, modifiedState.Pose.getRotation());
            inputs.logState(modifiedState);
        });
    }

    @Override
    public void updateDrivetrainData(DrivetrainIOInputs inputs) {
        var state = this.getStateCopy();
        state.Speeds = ChassisSpeeds.fromRobotRelativeSpeeds(state.Speeds, state.Pose.getRotation());
        inputs.logState(state);
    }

    @Override
    public SwerveModule<TalonFX, TalonFX, CANcoder> getSwerveModule(int index) {
        return this.getModule(index);
    }

    @Override
    public void setSwerveState(SwerveRequest req) {
        this.setControl(req);
    }

    @Override
    public void resetHeading(Rotation2d Heading) {
        this.resetRotation(Heading);
    }

    @Override
    public ChassisSpeeds getRobotRelSpeed() {
        return this.getState().Speeds;
    }

    @Override
    public Pose2d getPose() {
        return this.getState().Pose;
    }

    @Override
    public void resetRobotPose(Pose2d Pose) {
        Logger.recordOutput("Debug/PoseResetTo", Pose);
        this.resetPose(Pose);
    }

    private void startSimThread() {
        if (simNotifier != null) {
            return;
        }
        lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        simNotifier = new Notifier(() -> {
            final double currentTime = Utils.getCurrentTimeSeconds();
            double deltaTime = currentTime - lastSimTime;
            lastSimTime = currentTime;

            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage());
        });
        simNotifier.startPeriodic(SIM_LOOP_PERIOD);
    }

    @Override
    public void close() {
        if (simNotifier != null) {
            simNotifier.stop();
            simNotifier.close();
            simNotifier = null;
        }
    }

    @Override
    public void setPoseEstValues(Pose2d pose, double timestamp, Matrix<N3, N1> dev) {
        this.addVisionMeasurement(pose, timestamp, dev);
    }

    @Override
    public void simulationInit() {
        startSimThread();
    }

    @Override
    public void simulationPeriodic() {
        this.updateSimState(SIM_LOOP_PERIOD, RobotController.getBatteryVoltage());
        // This method can be used to add additional simulation code if needed.
    }

    @Override
    public void seedField() {
        this.seedFieldCentric();
    }

}
