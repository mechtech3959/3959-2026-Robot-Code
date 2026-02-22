package frc.robot.subsystems.drivetrain;

import frc.robot.subsystems.drivetrain.modules.ModuleIO;
import frc.robot.subsystems.drivetrain.modules.ModuleIOCTRE;
import frc.robot.subsystems.drivetrain.modules.ModuleIOInputsAutoLogged;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;

import choreo.auto.AutoFactory;
import choreo.trajectory.SwerveSample;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.util.BaseCalculator;
import frc.robot.util.FieldBasedConstants;

public class DrivetrainSubsystem extends SubsystemBase {

    public enum SwerveState {
        Disabled,
        Brake,
        ChoreoTrajectory,
        TeleOp,
        Heading,
        VisionHeading,

    }

    private final SwerveRequest.FieldCentricFacingAngle headingDrive = new SwerveRequest.FieldCentricFacingAngle()
            .withHeadingPID(3, 0, 0)
            .withDriveRequestType(SwerveModule.DriveRequestType.OpenLoopVoltage);
    private final PIDController autoXController = new PIDController(7, 0, 0);
    private final PIDController autoYController = new PIDController(7, 0, 0);
    private final PIDController autoHeadingController = new PIDController(7, 0, 0);
    private SwerveSample trajectorySample = null;
    private final PIDController autoDriveController = new PIDController(3.0, 0, 0.1);

    private final SwerveRequest.ApplyFieldSpeeds pathRequest = new SwerveRequest.ApplyFieldSpeeds();

    private SwerveState currentDriveState = SwerveState.TeleOp;
    private CommandXboxController controller;
    private DrivetrainIO io = new DrivetrainIO() {
    };
    final DrivetrainIOInputsAutoLogged swerveInputs = new DrivetrainIOInputsAutoLogged();
    private final ModuleIOInputsAutoLogged[] moduleInputs = new ModuleIOInputsAutoLogged[] {
            new ModuleIOInputsAutoLogged(), // FL (Index 0)
            new ModuleIOInputsAutoLogged(), // FR (Index 1)
            new ModuleIOInputsAutoLogged(), // BL (Index 2)
            new ModuleIOInputsAutoLogged() // BR (Index 3)
    };

    private final ModuleIO[] modules = new ModuleIO[4];

    private static final double maxSpeed = 5;
    private static final double maxAngSpeed = 2.75;

    public DrivetrainSubsystem(DrivetrainIO io, CommandXboxController controller) {

        this.io = io;
        this.controller = controller;

        for (int i = 0; i < 4; i++) {
            modules[i] = new ModuleIOCTRE(io.getSwerveModule(i));
            modules[i].updateInputs(moduleInputs[i]);

        }

        io.registerDrivetrainTelemetry(swerveInputs);

        autoHeadingController.enableContinuousInput(-Math.PI, Math.PI);
    }

    @Override
    public void periodic() {

        io.updateDrivetrainData(swerveInputs);
        Logger.processInputs(getName(), swerveInputs);

        applyState();

        for (int i = 0; i < 4; i++) {
            // Read fresh data from hardware
            modules[i].updateInputs(moduleInputs[i]);
            // Send to dashboard
            Logger.processInputs("Drive/Module " + i, moduleInputs[i]);
        }

    }

    public AutoFactory makeAutoFactory() {
        return new AutoFactory(
                this::getPose,
                this::resetPose,
                this::stageTrajectory,
                true, // Trajectories are relative to starting pose
                this);

    }

    public void poseEst(Pose2d pose, double time, Matrix<N3, N1> dev) {

        io.setPoseEstValues(pose, time, dev);
    }

    private ChassisSpeeds calculateSpeedsBasedOnJoystickInputs() {
        // was .isEmpty() but threw error for some reason
        if (!DriverStation.getAlliance().isPresent()) {
            return new ChassisSpeeds(0, 0, 0);
        }

        double xMagnitude = MathUtil.applyDeadband(controller.getLeftY(), 0.1);
        double yMagnitude = MathUtil.applyDeadband(controller.getLeftX(), 0.1);
        double angularMagnitude = MathUtil.applyDeadband(controller.getRightX(), 0.1);
        double ramp = 1.1 - controller.getLeftTriggerAxis();

        angularMagnitude = Math.copySign(angularMagnitude * angularMagnitude, angularMagnitude);

        double xVelocity = (FieldBasedConstants.isBlueAlliance() ? -xMagnitude * maxSpeed : xMagnitude * maxSpeed)
                * ramp;
        double yVelocity = (FieldBasedConstants.isBlueAlliance() ? -yMagnitude * maxSpeed : yMagnitude * maxSpeed)
                * ramp;
        double angularVelocity = angularMagnitude * maxAngSpeed * ramp;

        // Empirical time offset (−0.02 s) used to compensate for rotational skew:
        // we rotate the pose estimate by omega * dt to account for ~20 ms latency
        // between measured pose and applied chassis speeds.
        final double SKEW_COMPENSATION_TIME_S = -0.02;

        Rotation2d skewCompensationFactor = Rotation2d
                .fromRadians(swerveInputs.Speeds.omegaRadiansPerSecond * SKEW_COMPENSATION_TIME_S);

        return ChassisSpeeds.fromRobotRelativeSpeeds(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                        new ChassisSpeeds(xVelocity, yVelocity, -angularVelocity),
                        swerveInputs.Pose.getRotation()),
                swerveInputs.Pose.getRotation().plus(skewCompensationFactor));

    }

    public Pose2d getPose() {
        return io.getPose();
    }

    public void resetPose(Pose2d pose) {
        io.resetRobotPose(pose);
    }

    public void followTrajectory(SwerveSample sample) {
        // Get the current pose of the robot
        Pose2d pose = io.getPose();
        ChassisSpeeds speed = sample.getChassisSpeeds();
        speed.vxMetersPerSecond += autoXController.calculate(pose.getX(), sample.x);
        speed.vyMetersPerSecond += autoYController.calculate(pose.getY(), sample.y);
        speed.omegaRadiansPerSecond += autoHeadingController.calculate(pose.getRotation().getRadians(), sample.heading);

        io.setSwerveState(pathRequest.withSpeeds(speed)
                .withWheelForceFeedforwardsX(sample.moduleForcesX())
                .withWheelForceFeedforwardsY(sample.moduleForcesY())
                .withDriveRequestType(SwerveModule.DriveRequestType.Velocity));
    }

    public void teleopDrive() {
        io.setSwerveState(new SwerveRequest.ApplyFieldSpeeds()
                .withSpeeds(calculateSpeedsBasedOnJoystickInputs())
                .withDriveRequestType(SwerveModule.DriveRequestType.OpenLoopVoltage));
    }

    public void brake() {
        io.setSwerveState(new SwerveRequest.SwerveDriveBrake());
    }

    public void disable() {
    }

    public void headingDrive() {
        ChassisSpeeds joystickSpeeds = calculateSpeedsBasedOnJoystickInputs();
        io.setSwerveState(headingDrive.withTargetDirection(BaseCalculator.angleToAlign(swerveInputs.Pose))
                .withVelocityX(joystickSpeeds.vxMetersPerSecond)
                .withVelocityY(joystickSpeeds.vyMetersPerSecond));

    }

    public void visionHeadingDrive() {
    }

    public void applyState() {
        switch (currentDriveState) {
            case Disabled -> disable();
            case Brake -> brake();
            case ChoreoTrajectory -> {
                if (trajectorySample != null) {
                    followTrajectory(trajectorySample);
                }
            }
            case TeleOp -> teleopDrive();

            case Heading -> headingDrive();
            case VisionHeading -> visionHeadingDrive();

            default -> {
            }
        }

    }

    public void changeState(SwerveState wanted) {
        currentDriveState = wanted;
    }

    public void stageTrajectory(SwerveSample sample) {
        this.trajectorySample = sample;
        currentDriveState = SwerveState.ChoreoTrajectory;
    }

    public Rotation2d getHeading() {
        return swerveInputs.Pose.getRotation();
    }

}
