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
import frc.robot.util.FieldBasedConstants;

// Inspired by FRC 2910 
public class DrivetrainCTREIO extends SwerveDrivetrain<TalonFX, TalonFX, CANcoder>
        implements DrivetrainIO {
    private static final double simLoopPeriod = 0.004; // 4 ms
    private Notifier m_simNotifier = null;
    private double m_lastSimTime;

    private final SwerveRequest.FieldCentric fieldCentric = new SwerveRequest.FieldCentric();

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
        var state = this.getState();
        Logger.recordOutput("Debug/RawStateSpeeds", state.Speeds);
        Logger.recordOutput("Debug/ConvertedRobotRel",
                ChassisSpeeds.fromFieldRelativeSpeeds(state.Speeds, state.Pose.getRotation()));
        return state.Speeds;
    }

    @Override
    public void trajPath(ChassisSpeeds speeds) {
        this.setControl(new SwerveRequest.ApplyRobotSpeeds().withSpeeds(speeds));
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
        if (m_simNotifier != null) {
            return;
        }
        m_lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        m_simNotifier = new Notifier(() -> {
            final double currentTime = Utils.getCurrentTimeSeconds();
            double deltaTime = currentTime - m_lastSimTime;
            m_lastSimTime = currentTime;

            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage());
        });
        m_simNotifier.startPeriodic(simLoopPeriod);
    }

    @Override
    public void close() {
        if (m_simNotifier != null) {
            m_simNotifier.stop();
            m_simNotifier.close();
            m_simNotifier = null;
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
        this.updateSimState(simLoopPeriod, RobotController.getBatteryVoltage());
        // This method can be used to add additional simulation code if needed.
    }

    @Override
    public void seedField() {
        this.seedFieldCentric();
    }

}
