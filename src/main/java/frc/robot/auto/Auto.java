package frc.robot.auto;

import org.littletonrobotics.junction.Logger;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import static frc.robot.generated.ChoreoTraj.RCenterBackup;

import static frc.robot.generated.ChoreoTraj.BCenterBackup;
import static frc.robot.generated.ChoreoTraj.BLBallToShoot;
import static frc.robot.generated.ChoreoTraj.BLTFlipToBalls;
import static frc.robot.generated.ChoreoTraj.BLeftTrenchFlip;
import static frc.robot.generated.ChoreoTraj.Test;
import static frc.robot.generated.ChoreoTraj.TestAcc;
import frc.robot.subsystems.SuperStructureSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;

import frc.robot.subsystems.drivetrain.DrivetrainSubsystem.SwerveStates;

public class Auto {
    // private final SuperStructureSubsystem superStructure;
    private final DrivetrainSubsystem drivetrain;
    private final SuperStructureSubsystem superStructureSubsystem;
    private final AutoFactory autoFactory;
    private final AutoChooser autoChooser;

    public Auto(DrivetrainSubsystem drivetrain, SuperStructureSubsystem superStructureSubsystem) {
        this.autoChooser = new AutoChooser();

        this.drivetrain = drivetrain;
        this.superStructureSubsystem = superStructureSubsystem;

        this.autoFactory = drivetrain.makeAutoFactory();

    }

    public void configure() {

        autoChooser.addRoutine("Test", this::testCMDRoutine);
        autoChooser.addRoutine("Blue Middle", this::BlueMiddleRoutine);
        autoChooser.addRoutine("Blue Left Local", this::BLTrenchCoral);
        autoChooser.addRoutine("Blue Center Backup", this::BCenterBack);
        SmartDashboard.putData("Auto Chooser", autoChooser);
        RobotModeTriggers.autonomous().whileTrue(autoChooser.selectedCommandScheduler());
    }

    public AutoRoutine testRoutine() {
        final AutoRoutine routine = autoFactory.newRoutine("test");
        final AutoTrajectory test = Test.asAutoTraj(routine);
        Logger.recordOutput("Auto", test.getRawTrajectory().getPoses());

        // When the routine becomes active, reset odometry then follow the trajectory
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        // test.resetOdometry(), // Reset pose to trajectory start
                        test.cmd() // Follow the trajectory
                ));

        return routine;
    }

    public AutoRoutine BlueMiddleRoutine() {
        final AutoRoutine routine = autoFactory.newRoutine("blue_middle");
        final AutoTrajectory test = TestAcc.asAutoTraj(routine);
        Logger.recordOutput("Auto", test.getRawTrajectory().getPoses());

        // When the routine becomes active, reset odometry then follow the trajectory
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        test.resetOdometry(), // Reset pose to trajectory start
                        test.cmd() // Follow the trajectory
                ));

        return routine;
    }

    public AutoRoutine testCMDRoutine() {
        final AutoRoutine routine = autoFactory.newRoutine("testRoutine");
        final AutoTrajectory test = Test.asAutoTraj(routine);
        Logger.recordOutput("Auto", test.getRawTrajectory().getPoses());
        autoFactory.bind("TestCMD", Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TRAVEL);
            Commands.print("ran");
        }));

        // When the routine becomes active, reset odometry then follow the trajectory
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        test.resetOdometry(), // Reset pose to trajectory start

                        test.cmd() // Follow the trajectory
                ));
        test.done().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TRAVEL);
        }));

        return routine;
    }

    public AutoRoutine BLTrenchCoral() {
        final AutoRoutine routine = autoFactory.newRoutine("BLTrench Coral");
        final AutoTrajectory blTrenchFlip = BLeftTrenchFlip.asAutoTraj(routine);
        final AutoTrajectory blcoraltoIntake = BLTFlipToBalls.asAutoTraj(routine);
        final AutoTrajectory blShoot = BLBallToShoot.asAutoTraj(routine);
        // Logger.recordOutput("Auto", test.getRawTrajectory().getPoses());

        // When the routine becomes active, reset odometry then follow the trajectory
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        blTrenchFlip.resetOdometry(), // Reset pose to trajectory start
                        blTrenchFlip.cmd() // Follow the trajectory
                ));
        blTrenchFlip.recentlyDone()
                .onTrue(blcoraltoIntake.cmd().alongWith(Commands.runOnce(() -> {
                    superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.INTAKING);
                })));

        blcoraltoIntake.recentlyDone().onTrue(blShoot.cmd().alongWith(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TRAVEL);
        })));
        blShoot.done().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__FAR);
        }));
        return routine;
    }

    public AutoRoutine BCenterBack() {
        final AutoRoutine routine = autoFactory.newRoutine("BCeter");
        final AutoTrajectory bcenter = BCenterBackup.asAutoTraj(routine);
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        bcenter.resetOdometry(), // Reset pose to trajectory start

                        bcenter.cmd() // Follow the trajectory
                ));
        bcenter.done().onTrue(Commands.runOnce(() -> {
            drivetrain.changeState(SwerveStates.Disabled);
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__CLOSE);
        }));
        return routine;
    }

    public AutoRoutine RCenterBack() {
        final AutoRoutine routine = autoFactory.newRoutine("RCenter");
        final AutoTrajectory bcenter = RCenterBackup.asAutoTraj(routine);
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        bcenter.resetOdometry(), // Reset pose to trajectory start

                        bcenter.cmd() // Follow the trajectory
                ));
        bcenter.done().onTrue(Commands.runOnce(() -> {
            drivetrain.changeState(SwerveStates.Disabled);

            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__CLOSE);
        }));
        return routine;
    }
}
