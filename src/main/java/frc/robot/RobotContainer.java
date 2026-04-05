// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.SuperStructureSubsystem;
import frc.robot.subsystems.climber.ClimberCTREIO;
import frc.robot.subsystems.climber.ClimberSubsystem;
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
import frc.robot.subsystems.led.LEDCTREIO;
import frc.robot.subsystems.led.LEDHandler;
import frc.robot.subsystems.led.LEDSubsystem;
import frc.robot.subsystems.shooter.ShooterCTREIO;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.vision.VisionLimelightIO;
import frc.robot.subsystems.vision.VisionSubsystem;
import frc.robot.util.ShooterMap;

public class RobotContainer {

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
    private final LEDCTREIO ledIO;
    private final LEDSubsystem ledSubsystem;
    private final LEDHandler ledHandler;

    private final CommandXboxController driverController = new CommandXboxController(0);
    private final CommandXboxController shooterStopperController = new CommandXboxController(1);
    private final SendableChooser<Command> autoChooser;

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
        // CAD Values for LL4 are 0.70750176 Height,0 Center, -0.04948936 Backwards, YAW
        // 0,Pitch 20, Roll 0
        // CAD Values for LL2+ are 0.6968871 Height, 0.2286 Right, -0.31140908
        // Backwards, YAW 180, Pitch 0, Roll 0
        // 22, 0.703
        visionLimelightFront = new VisionLimelightIO("limelight-front", "LL4", 0.70750176, 20.0, 0);
        // 0.8
        visionLimelightBack = new VisionLimelightIO("limelight-back", "LL2+", 0.6968871, 0.0, 180);

        visionSubsystem = new VisionSubsystem(drivetrainSubsystem, visionLimelightFront, visionLimelightBack);
        climberIO = new ClimberCTREIO();
        climberSubsystem = new ClimberSubsystem(climberIO);
        superStructureSubsystem = new SuperStructureSubsystem(conveyorSubsystem,
                shooterSubsystem, intakeSubsystem,
                indexerSubsystem, climberSubsystem, drivetrainSubsystem);
        shooterMap = new ShooterMap();
        ledIO = new LEDCTREIO();
        ledSubsystem = new LEDSubsystem(ledIO);
        ledHandler = new LEDHandler(ledSubsystem, superStructureSubsystem, intakeSubsystem,
                shooterSubsystem, drivetrainSubsystem);
        drivetrainSubsystem.configureAutoBuilder();
        NamedCommands.registerCommand("ShootClose", Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__CLOSE);
        }));
        NamedCommands.registerCommand("IntakeOn", Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.INTAKING);
        }));
        NamedCommands.registerCommand("IntakeOff", Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TRAVEL);
        }));
        NamedCommands.registerCommand("ShootFar", Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__FAR);
        }));
        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);
        ledHandler.handleLEDs();
        configureBindings();
    }

    // In RobotContainer.java
    public void prepareForAuto() {
        drivetrainSubsystem.changeState(SwerveStates.Disabled);
    }

    public void endTransition() {
        drivetrainSubsystem.changeState(SwerveStates.TeleOp);
    }

    public void resetAllianceHeading() {
        drivetrainSubsystem.resetAllianceHeading();
    }

    public void estimatedDistance() {
        double distance = visionSubsystem.getDistanceToTarget();
        Logger.recordOutput("distance from tag", distance);
        shooterSubsystem.setEstimatedRPS(shooterMap.getShooterSpeedForDistance(distance));

    }

    public Command autoCenter() {
        return Commands.sequence(
                Commands.runOnce(() -> {
                    drivetrainSubsystem.changeState(SwerveStates.AutoBack);
                }),
                Commands.waitSeconds(2.5),

                Commands.runOnce(() -> {
                    drivetrainSubsystem.changeState(SwerveStates.Disabled);

                    superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__CLOSE);

                })

        );
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

    private Command intakeCommand() {
        return Commands.startEnd(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.INTAKING);
        }, () -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TRAVEL);

        });
    }

    private void configureBindings() {

        driverController.start().onChange(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.STARTING_CONFIG);

        }));
        driverController.leftBumper().toggleOnTrue(intakeCommand());
   //     driverController.a().onChange(Commands.runOnce(() -> {
     //       drivetrainSubsystem.changeState(SwerveStates.Heading);
       // }));
       // driverController.b().onChange(Commands.runOnce(() -> {
         //   drivetrainSubsystem.changeState(SwerveStates.TeleOp);
       // }));
       driverController.leftTrigger().whileTrue(Commands.startEnd(()->{
        drivetrainSubsystem.changeState(SwerveStates.Heading);
       }, ()->{
        drivetrainSubsystem.changeState(SwerveStates.TeleOp);
       })); 
       // Single press Y = prep climb
        //driverController.y().onTrue(Commands.runOnce(() -> {
         //   superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.PREP_CLIMB);
       // }));

        // Double press Y = actual climb
  //      driverController.y().multiPress(2, 0.5).onTrue(Commands.runOnce(() -> {
    //        drivetrainSubsystem.changeState(SwerveStates.Climb);
      //      superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.CLIMBING);
       // }));
        driverController.rightTrigger().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__FAR);
        }));
        driverController.rightBumper().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING__CLOSE);
        }));
        driverController.x().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING_STOP);
        }));
        shooterStopperController.x().onTrue(Commands.runOnce(() -> {
            superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.SHOOTING_STOP);
        }));
        shooterStopperController.start().onTrue(Commands.runOnce(() -> {
            drivetrainSubsystem.seedField();
        }));

    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
