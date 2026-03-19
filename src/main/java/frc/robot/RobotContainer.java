// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.SuperStructureSubsystem;
import frc.robot.subsystems.climber.ClimberCTREIO;
import frc.robot.subsystems.conveyor.ConveyorREVIO;
import frc.robot.subsystems.conveyor.ConveyorSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainCTREIO;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem.SwerveStates;
import frc.robot.subsystems.indexer.IndexerREVIO;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.intake.IntakeREVIO;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.feed.FeedCTREIO;
import frc.robot.subsystems.intake.feed.FeedSubsystem;
import frc.robot.subsystems.shooter.ShooterCTREIO;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.vision.VisionLimelightIO;
import frc.robot.subsystems.vision.VisionSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.auto.Auto;
import frc.robot.util.ShooterMap;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class RobotContainer {
    private final Auto auton;

    private final DrivetrainCTREIO drivetrainIO;
    private final DrivetrainSubsystem drivetrainSubsystem;
    private final ShooterCTREIO shooterIO;
    private final ShooterSubsystem shooterSubsystem;
    private final FeedCTREIO feedIO;
    private final FeedSubsystem feedSubsystem;
    private final IntakeREVIO intakeIO;
    private final IntakeSubsystem intakeSubsystem;
    private final IndexerREVIO indexerIO;
    private final IndexerSubsystem indexerSubsystem;
    private final ConveyorREVIO conveyorIO;
    private final VisionLimelightIO visionLimelightFront;
    private final VisionLimelightIO visionLimelightBack;
    private final VisionSubsystem visionSubsystem;
    private final ClimberCTREIO climberIO;
    private final ClimberSubsystem climberSubsystem;
    private final ShooterMap shooterMap;
    private final ConveyorSubsystem conveyorSubsystem;
    private final SuperStructureSubsystem superStructureSubsystem;
    private final CommandXboxController driverController = new CommandXboxController(0);

    public RobotContainer() {

        @SuppressWarnings("unchecked")
        SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>[] modules = new SwerveModuleConstants[] {
                TunerConstants.FrontLeft,
                TunerConstants.FrontRight,
                TunerConstants.BackLeft,
                TunerConstants.BackRight
        };

        drivetrainIO = new DrivetrainCTREIO(TunerConstants.DrivetrainConstants,
                modules);
        drivetrainSubsystem = new DrivetrainSubsystem(drivetrainIO,
                driverController);

        indexerIO = new IndexerREVIO();
        indexerSubsystem = new IndexerSubsystem(indexerIO);
        conveyorIO = new ConveyorREVIO();

        conveyorSubsystem = new ConveyorSubsystem(conveyorIO);

        shooterIO = new ShooterCTREIO();
        shooterSubsystem = new ShooterSubsystem(shooterIO);

        feedIO = new FeedCTREIO();
        feedSubsystem = new FeedSubsystem(feedIO);
        intakeIO = new IntakeREVIO();
        intakeSubsystem = new IntakeSubsystem(intakeIO, feedSubsystem);
        // 22, 0.703
        visionLimelightFront = new VisionLimelightIO("limelight-front", "LL4", 0.703, 20.0, 0);
        visionLimelightBack = new VisionLimelightIO("limelight-back", "LL2+", 0.8, 0.0, 180);

        visionSubsystem = new VisionSubsystem(drivetrainSubsystem, visionLimelightFront, visionLimelightBack);
        climberIO = new ClimberCTREIO();
        climberSubsystem = new ClimberSubsystem(climberIO);
        superStructureSubsystem = new SuperStructureSubsystem(conveyorSubsystem,
                shooterSubsystem, intakeSubsystem,
                indexerSubsystem, climberSubsystem, drivetrainSubsystem);
        auton = new Auto(drivetrainSubsystem, superStructureSubsystem);
        shooterMap = new ShooterMap();

        configureBindings();
    }

    public void endTransition() {
        drivetrainSubsystem.changeState(SwerveStates.TeleOp);
    }

    public void estimatedDistance() {
        double distance = visionSubsystem.getDistanceToTarget();
        Logger.recordOutput("distance from tag", distance);
        shooterSubsystem.setEstimatedRPS(shooterMap.getShooterSpeedForDistance(distance));

    }

    private Command controllerRumbleCommand() {
        return Commands.startEnd(
                () -> {
                    driverController.getHID().setRumble(RumbleType.kBothRumble, 1.0);
                },
                () -> {
                    driverController.getHID().setRumble(RumbleType.kBothRumble, 0.0);
                });
    }

    private Command controllerDoubleRumbleCommand() {
        return controllerRumbleCommand().withTimeout(0.5).andThen(controllerRumbleCommand().withTimeout(0.5));
    }

    private void configureBindings() {

        auton.configure();

        driverController.start().onChange(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.STARTING_CONFIG);

        }));
        driverController.leftBumper().whileTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.INTAKING);

        }).alongWith(controllerRumbleCommand())).onFalse(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TRAVEL);

        }));
        driverController.rightBumper().toggleOnTrue(Commands.runOnce(() -> {
            drivetrainSubsystem.changeState(SwerveStates.Heading);
        })).toggleOnTrue(Commands.runOnce(() -> {
            drivetrainSubsystem.changeState(SwerveStates.TeleOp);
        }));
        // Single press B = prep climb
        driverController.y().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.PREP_CLIMB);
        }));

        // Double press B = actual climb
        driverController.y().multiPress(2, 0.5).onTrue(Commands.runOnce(() -> {
            // drivetrainSubsystem.changeState(SwerveStates.climb);
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.CLIMBING);
        }));
              driverController.b().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__FAR);
        }));
          driverController.a().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__CLOSE);
        }));  driverController.x().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING_STOP);
        }));

        // driverController.b().onChange(Commands.runOnce(() -> {
        // superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TEST);
        // }));

        /*
         * driverController.a().onChange(Commands.runOnce(() -> {
         * drivetrainSubsystem.changeState(SwerveState.Heading);
         * }));
         * driverController.b().onChange(Commands.runOnce(() -> {
         * drivetrainSubsystem.changeState(SwerveState.TeleOp);
         * }));
         * driverController.x().onChange(Commands.runOnce(() -> {
         * drivetrainSubsystem.changeState(SwerveState.Brake);
         * }));
         * driverController.y().onChange(Commands.runOnce(() -> {
         * drivetrainSubsystem.changeState(SwerveState.VisionHeading);
         * }));
         */
        /*
         * driverController.a().onTrue(Commands.runOnce(
         * () -> intakeSubsystem.setIntakeState(IntakeStates.TEST,
         * FeedSubsystem.FeedStates.PERCENTOUTPUT, 0.25)));
         * driverController.b().onTrue(Commands.runOnce(
         * () -> intakeSubsystem.setIntakeState(IntakeStates.TEST,
         * FeedSubsystem.FeedStates.PERCENTOUTPUT, 0.5)));
         * driverController.x().onTrue(Commands.runOnce(
         * () -> intakeSubsystem.setIntakeState(IntakeStates.TEST,
         * FeedSubsystem.FeedStates.PERCENTOUTPUT, 0.75)));
         * driverController.y().onTrue(Commands.runOnce(
         * () -> intakeSubsystem.setIntakeState(IntakeStates.TEST,
         * FeedSubsystem.FeedStates.PERCENTOUTPUT, 1)));
         */
    }

}
