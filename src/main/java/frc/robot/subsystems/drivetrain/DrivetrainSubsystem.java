package frc.robot.subsystems.drivetrain;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.utility.PhoenixPIDController;

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
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drivetrain.modules.ModuleCTREIO;
import frc.robot.subsystems.drivetrain.modules.ModuleIO;
import frc.robot.subsystems.drivetrain.modules.ModuleIOInputsAutoLogged;
import frc.robot.util.BaseCalculator;
import frc.robot.util.FieldBasedConstants;

public class DrivetrainSubsystem extends SubsystemBase {

    public enum SwerveStates {
        Disabled,
        Brake,
        ChoreoTrajectory,
        TeleOp,
        Heading,
        VisionHeading,
        climb
    }

    private final SwerveRequest.FieldCentricFacingAngle headingDrive = new SwerveRequest.FieldCentricFacingAngle()
            .withHeadingPID(7, 0, 0.5)
            .withDriveRequestType(SwerveModule.DriveRequestType.Velocity);
    private final ClimbRequest climbRequest = new ClimbRequest();
    private final PIDController autoXController = new PIDController(7, 0, 0);
    private final PIDController autoYController = new PIDController(7, 0, 0);
    private final PIDController autoHeadingController = new PIDController(7, 0, 0);
    private SwerveSample trajectorySample = null;

    private final SwerveRequest.ApplyFieldSpeeds pathRequest = new SwerveRequest.ApplyFieldSpeeds();

    private SwerveStates currentDriveState = SwerveStates.TeleOp;
    private final CommandXboxController controller;
    private DrivetrainIO io;
    final DrivetrainIOInputsAutoLogged swerveInputs = new DrivetrainIOInputsAutoLogged();
    private final ModuleIOInputsAutoLogged[] moduleInputs = new ModuleIOInputsAutoLogged[] {
            new ModuleIOInputsAutoLogged(), // FL (Index 0)
            new ModuleIOInputsAutoLogged(), // FR (Index 1)
            new ModuleIOInputsAutoLogged(), // BL (Index 2)
            new ModuleIOInputsAutoLogged() // BR (Index 3)
    };

    private final ModuleIO[] modules = new ModuleIO[4];

    private final double maxSpeed = 8.0; // meters per second, placeholder value - adjust based on your robot's
                                         // capabilities
    private final double maxAngSpeed = 6; // radians per second, placeholder value - adjust based on your robot's
                                          // capabilities

    public DrivetrainSubsystem(DrivetrainIO io, CommandXboxController controller) {

        this.io = io;
        this.controller = controller;
        headingDrive.HeadingController = new PhoenixPIDController(7, 0, 0);
        headingDrive.HeadingController.enableContinuousInput(-Math.PI, Math.PI);

        modules[0] = new ModuleCTREIO(io.getSwerveModule(0));
        modules[1] = new ModuleCTREIO(io.getSwerveModule(1));
        modules[2] = new ModuleCTREIO(io.getSwerveModule(2));
        modules[3] = new ModuleCTREIO(io.getSwerveModule(3));

        modules[0].updateInputs(moduleInputs[0]);
        modules[1].updateInputs(moduleInputs[1]);
        modules[2].updateInputs(moduleInputs[2]);
        modules[3].updateInputs(moduleInputs[3]);

        io.registerDrivetrainTelemetry(swerveInputs);

        autoHeadingController.enableContinuousInput(-Math.PI, Math.PI);
        SmartDashboard.putData("Swerve Drive", (SendableBuilder builder) -> {
            builder.setSmartDashboardType("SwerveDrive");

            builder.addDoubleProperty("Front Left Angle", () -> moduleInputs[0].steerAbsolutePositionRad, null);
            builder.addDoubleProperty("Front Left Velocity", () -> moduleInputs[0].driveVelocityRadPerSec / 30,
                    null);

            builder.addDoubleProperty("Front Right Angle", () -> moduleInputs[1].steerAbsolutePositionRad, null);
            builder.addDoubleProperty("Front Right Velocity", () -> moduleInputs[1].driveVelocityRadPerSec / 30,
                    null);
            builder.addDoubleProperty("Back Left Angle", () -> moduleInputs[2].steerAbsolutePositionRad, null);
            builder.addDoubleProperty("Back Left Velocity", () -> moduleInputs[2].driveVelocityRadPerSec / 30,
                    null);
            builder.addDoubleProperty("Back Right Angle", () -> moduleInputs[3].steerAbsolutePositionRad, null);
            builder.addDoubleProperty("Back Right Velocity", () -> moduleInputs[3].driveVelocityRadPerSec / 30,
                    null);
            builder.addDoubleProperty("Robot Angle", () -> getHeading().getRadians(), null);
        });

    }

    @Override
    public void periodic() {

        applyState();

        io.updateDrivetrainData(swerveInputs);
        Logger.processInputs(getName(), swerveInputs);

        // Read fresh data from hardware
        modules[0].updateInputs(moduleInputs[0]);
        modules[1].updateInputs(moduleInputs[1]);
        modules[2].updateInputs(moduleInputs[2]);
        modules[3].updateInputs(moduleInputs[3]);

        // Send to dashboard
        Logger.processInputs(getName() + "/Module " + 0, moduleInputs[0]);
        Logger.processInputs(getName() + "/Module " + 1, moduleInputs[1]);
        Logger.processInputs(getName() + "/Module " + 2, moduleInputs[2]);
        Logger.processInputs(getName() + "/Module " + 3, moduleInputs[3]);

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
        final double SKEW_COMPENSATION_TIME_S = -0.03;

        Rotation2d skewCompensationFactor = Rotation2d
                .fromRadians(swerveInputs.Speeds.omegaRadiansPerSecond * SKEW_COMPENSATION_TIME_S);

        return ChassisSpeeds.fromRobotRelativeSpeeds(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                        new ChassisSpeeds(xVelocity, yVelocity, angularVelocity),
                        swerveInputs.Pose.getRotation()),
                swerveInputs.Pose.getRotation().plus(skewCompensationFactor));
        // return ChassisSpeeds.fromFieldRelativeSpeeds(
        // new ChassisSpeeds(xVelocity, yVelocity, angularVelocity),
        // swerveInputs.Pose.getRotation());

    }

    public Pose2d getPose() {
        return io.getPose();
    }

    public void resetPose(Pose2d pose) {
        io.resetRobotPose(pose);
    }

    public void resetHeading(Rotation2d heading) {
        io.resetHeading(heading);

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

    public void climb() {

        io.setSwerveState(climbRequest);
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

    public void changeState(SwerveStates wanted) {
        currentDriveState = wanted;
    }

    public void stageTrajectory(SwerveSample sample) {
        this.trajectorySample = sample;
        currentDriveState = SwerveStates.ChoreoTrajectory;
    }

    public Rotation2d getHeading() {
        return swerveInputs.Pose.getRotation();
    }

    public double getAngularVelocity() {
        return swerveInputs.Speeds.omegaRadiansPerSecond;
    }

    public double getLinearVelocity() {
        return Math.hypot(swerveInputs.Speeds.vxMetersPerSecond, swerveInputs.Speeds.vyMetersPerSecond);
    }

}