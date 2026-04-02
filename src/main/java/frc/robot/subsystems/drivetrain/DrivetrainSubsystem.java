package frc.robot.subsystems.drivetrain;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
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

import edu.wpi.first.wpilibj.smartdashboard.Field2d;

public class DrivetrainSubsystem extends SubsystemBase {

    public enum SwerveStates {
        Disabled,
        Brake,
        ChoreoTrajectory,
        PathPlannerTrajectory,
        TeleOp,
        Heading,
        VisionHeading,
        Climb,
        AutoBack
    }

    private final SwerveRequest.FieldCentricFacingAngle headingDrive = new SwerveRequest.FieldCentricFacingAngle()
            .withHeadingPID(7, 0.0, 0.0)
            .withDriveRequestType(SwerveModule.DriveRequestType.Velocity);
    private final ChassisSpeeds emptySpeed = new ChassisSpeeds(0,
            0, 0);
    private final SwerveRequest.ApplyFieldSpeeds fieldSpeeds = new SwerveRequest.ApplyFieldSpeeds();
    private final SwerveRequest.SwerveDriveBrake brakeRequest = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.FieldCentric fieldCentric = new SwerveRequest.FieldCentric()
            .withDriveRequestType(SwerveModule.DriveRequestType.Velocity);
    private final SwerveRequest.ApplyRobotSpeeds robotCentric = new SwerveRequest.ApplyRobotSpeeds();

    private final ClimbRequest climbRequest = new ClimbRequest();

    private ChassisSpeeds trajectoryTargetSpeeds = new ChassisSpeeds(0, 0, 0);

    private SwerveStates currentDriveState = SwerveStates.TeleOp;
    private final CommandXboxController controller;
    private final DrivetrainIO io;
    final DrivetrainIOInputsAutoLogged swerveInputs = new DrivetrainIOInputsAutoLogged();
    private final ModuleIOInputsAutoLogged[] moduleInputs = new ModuleIOInputsAutoLogged[] {
            new ModuleIOInputsAutoLogged(), // FL (Index 0)
            new ModuleIOInputsAutoLogged(), // FR (Index 1)
            new ModuleIOInputsAutoLogged(), // BL (Index 2)
            new ModuleIOInputsAutoLogged() // BR (Index 3)
    };

    private final ModuleIO[] modules = new ModuleIO[4];

    private final double maxSpeed = 5.0; // meters per second, placeholder value - adjust based on your robot's
                                         // capabilities
    private final double maxAngSpeed = 6; // radians per second, placeholder value - adjust based on your robot's
                                          // capabilities

    public DrivetrainSubsystem(DrivetrainIO io, CommandXboxController controller) {

        this.io = io;
        this.controller = controller;

        modules[0] = new ModuleCTREIO(io.getSwerveModule(0));
        modules[1] = new ModuleCTREIO(io.getSwerveModule(1));
        modules[2] = new ModuleCTREIO(io.getSwerveModule(2));
        modules[3] = new ModuleCTREIO(io.getSwerveModule(3));

        modules[0].updateInputs(moduleInputs[0]);
        modules[1].updateInputs(moduleInputs[1]);
        modules[2].updateInputs(moduleInputs[2]);
        modules[3].updateInputs(moduleInputs[3]);

        io.registerDrivetrainTelemetry(swerveInputs);

        // Defer AutoBuilder configuration to avoid leaking `this` from the constructor.
        // Call configureAutoBuilder() after constructing this subsystem (for example,
        // from RobotContainer)
        // so the AutoBuilder gets a fully constructed subsystem reference.
        // RobotConfig config;
        // try { ... AutoBuilder.configure(..., this) ... } catch (Exception e) { ... }
        // <-- moved to configureAutoBuilder()

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
        Logger.recordOutput("States/drivetrain", currentDriveState.toString());

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

    public void poseEst(Pose2d pose, double time, Matrix<N3, N1> dev) {

        io.setPoseEstValues(pose, time, dev);
    }

    private ChassisSpeeds calculateSpeedsBasedOnJoystickInputs() {
        if (DriverStation.getAlliance().isEmpty()) {
            return emptySpeed;
        }

        double xMagnitude = MathUtil.applyDeadband(-controller.getLeftY(), 0.1);
        double yMagnitude = MathUtil.applyDeadband(-controller.getLeftX(), 0.1);
        double angularMagnitude = MathUtil.applyDeadband(controller.getRightX(), 0.1);
        double ramp = 1.1 - controller.getLeftTriggerAxis();

        angularMagnitude = Math.copySign(angularMagnitude * angularMagnitude, angularMagnitude);

        double xVelocity = (FieldBasedConstants.isBlueAlliance() ? xMagnitude * maxSpeed : -xMagnitude * maxSpeed)
                * ramp;
        double yVelocity = (FieldBasedConstants.isBlueAlliance() ? yMagnitude * maxSpeed : -yMagnitude * maxSpeed)
                * ramp;
        double angularVelocity = -angularMagnitude * maxAngSpeed * ramp;

        // Empirical time offset (−0.02 s) used to compensate for rotational skew:
        // we rotate the pose estimate by omega * dt to account for ~20 ms latency
        // between measured pose and applied chassis speeds.
        final double SKEW_COMPENSATION_TIME_S = -0.02;

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

    public void prepTrajectory(ChassisSpeeds speeds) {
        this.trajectoryTargetSpeeds = speeds;
        currentDriveState = SwerveStates.PathPlannerTrajectory;

    }

    public void followPathPlannerTrajectory(ChassisSpeeds speeds) {
        io.setSwerveState(robotCentric.withSpeeds(speeds).withDriveRequestType(SwerveModule.DriveRequestType.Velocity));
        // io.setSwerveState(
        // new SwerveRequest.ApplyRobotSpeeds()
        // .withSpeeds(speeds)
        // .withDriveRequestType(SwerveModule.DriveRequestType.Velocity));
    }

    public void teleopDrive() {
        if (DriverStation.getAlliance().isEmpty()) {
            io.setSwerveState(fieldSpeeds.withSpeeds(emptySpeed));
            return;
        }

        double xMagnitude = MathUtil.applyDeadband(-controller.getLeftY(), 0.1);
        double yMagnitude = MathUtil.applyDeadband(-controller.getLeftX(), 0.1);
        double angularMagnitude = MathUtil.applyDeadband(controller.getRightX(), 0.1);
        double ramp = 1.1 - controller.getLeftTriggerAxis();

        angularMagnitude = Math.copySign(angularMagnitude * angularMagnitude, angularMagnitude);

        double xVelocity = (FieldBasedConstants.isBlueAlliance() ? xMagnitude * maxSpeed : -xMagnitude * maxSpeed)
                * ramp;
        double yVelocity = (FieldBasedConstants.isBlueAlliance() ? yMagnitude * maxSpeed : -yMagnitude * maxSpeed)
                * ramp;
        double angularVelocity = -angularMagnitude * maxAngSpeed * ramp;

        io.setSwerveState(fieldCentric
                .withVelocityX(xVelocity)
                .withVelocityY(yVelocity)
                .withRotationalRate(angularVelocity));
    }

    public void brake() {
        io.setSwerveState(brakeRequest);
    }

    public void climb() {

        io.setSwerveState(climbRequest);
    }

    public void disable() {
        io.setSwerveState(fieldSpeeds.withSpeeds(emptySpeed));

    }

    public void headingDrive() {
        ChassisSpeeds joystickSpeeds = calculateSpeedsBasedOnJoystickInputs();
        io.setSwerveState(
                headingDrive.withTargetDirection(BaseCalculator.angleToAlign(swerveInputs.Pose))
                        .withVelocityX(joystickSpeeds.vxMetersPerSecond)
                        .withVelocityY(joystickSpeeds.vyMetersPerSecond));

    }

    public void visionHeadingDrive() {
    }

    public void applyState() {
        switch (currentDriveState) {
            case Disabled -> disable();
            case Brake -> brake();

            case PathPlannerTrajectory -> {
                if (trajectoryTargetSpeeds != null) {
                    followPathPlannerTrajectory(trajectoryTargetSpeeds);
                }
            }
            case TeleOp -> teleopDrive();

            case Heading -> headingDrive();
            case VisionHeading -> visionHeadingDrive();
            case Climb -> climb();
            case AutoBack -> autoRobotCenticBack();
            default -> {
            }
        }

    }

    public void changeState(SwerveStates wanted) {
        currentDriveState = wanted;
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

    public ChassisSpeeds getRobotSpeeds() {
        return io.getRobotRelSpeed();
    }

    public void seedField() {
        io.seedField();
    }

    public void resetAllianceHeading() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            io.resetHeading(alliance.get() == DriverStation.Alliance.Red ? Rotation2d.k180deg : Rotation2d.kZero);
        }
    }

    public void autoBack() {
        io.setSwerveState(
                fieldCentric.withVelocityX(0.5)
                        .withVelocityY(0)
                        .withRotationalRate(0));
    }

    public void autoRobotCenticBack() {
        io.setSwerveState(
                robotCentric.withSpeeds(new ChassisSpeeds(0.8, 0, 0)));
    }

    // New public initializer — call this after the subsystem is fully constructed.
    public void configureAutoBuilder() {
        try {
            RobotConfig config = RobotConfig.fromGUISettings();

            // Configure AutoBuilder with fully-constructed `this` (safe to pass now).
            AutoBuilder.configure(
                    this::getPose, // Robot pose supplier
                    this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                    this::getRobotSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                    (speeds, feedforwards) -> prepTrajectory(speeds), // Method that will drive the robot given ROBOT
                                                                      // RELATIVE ChassisSpeeds
                    new PPHolonomicDriveController(
                            new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                            new PIDConstants(7, 0.0, 0.1) // Rotation PID constants
                    ),
                    config,
                    () -> {
                        var alliance = DriverStation.getAlliance();
                        if (alliance.isPresent()) {
                            return alliance.get() == DriverStation.Alliance.Red;
                        }
                        return false;
                    },
                    this // Reference to this subsystem to set requirements — safe now
            );
        } catch (Exception e) {
            // Handle exception as needed
        }
    }

}