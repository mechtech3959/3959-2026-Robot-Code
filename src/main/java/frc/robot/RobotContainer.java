// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.conveyor.ConveyorREVIO;
import frc.robot.subsystems.conveyor.ConveyorSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainCTREIO;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem.SwerveState;
import frc.robot.subsystems.indexer.IndexerREVIO;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.intake.IntakeREVIO;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.feed.FeedCTREIO;
import frc.robot.subsystems.intake.feed.FeedSubsystem;
import frc.robot.subsystems.shooter.ShooterCTREIO;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.SuperStructureSubsystem;

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

  private final ConveyorSubsystem conveyorSubsystem;
  private final SuperStructureSubsystem superStructureSubsystem;
  private final CommandXboxController driverController = new CommandXboxController(0);

  public RobotContainer() {

    drivetrainIO = new DrivetrainCTREIO(TunerConstants.DrivetrainConstants,
        TunerConstants.FrontLeft,
        TunerConstants.FrontRight, TunerConstants.BackLeft,
        TunerConstants.BackRight);
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
    superStructureSubsystem = new SuperStructureSubsystem(conveyorSubsystem, shooterSubsystem, intakeSubsystem,
        indexerSubsystem);
    configureBindings();
  }

  public void endTransition() {
    // drivetrainSubsystem.changeState(SwerveState.TeleOp);
  }

  private void configureBindings() {
    /*
     * driverController.a().onTrue(Commands.runOnce(
     * () ->
     * shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.KNOWN_CLOSE,
     * -55)));
     * driverController.b().onTrue(Commands.runOnce(
     * () ->
     * shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.KNOWN_CLOSE,
     * -50)));
     * driverController.x().onTrue(Commands.runOnce(
     * () ->
     * shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.UNKNOWN,
     * 0)));
     * driverController.y().onTrue(Commands.runOnce(
     * () ->
     * shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.KNOWN_CLOSE,
     * -60)));
     */
    // driverController.a().onChange(Commands.runOnce(() -> {
    // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.INTAKE);

    // }));
    /*
     * driverController.b().onChange(Commands.runOnce(() -> {
     * // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.MID_STOW);
     * conveyorSubsystem.changeState(ConveyorSubsystem.ConveyorStates.RUN);
     * }));
     * driverController.x().onChange(Commands.runOnce(() -> {
     * conveyorSubsystem.changeState(ConveyorSubsystem.ConveyorStates.STOP);
     * 
     * // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.STOW);
     * }));
     * driverController.y().onChange(Commands.runOnce(() -> {
     * indexerSubsystem.changeState(IndexerSubsystem.IndexerStates.RUN);
     * 
     * // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.STOW);
     * }));
     * driverController.a().onChange(Commands.runOnce(() -> {
     * indexerSubsystem.changeState(IndexerSubsystem.IndexerStates.STOP);
     * // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.STOW);
     * }));
     */
    driverController.a().onChange(Commands.runOnce(() -> {
      superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.IDLE);
      // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.STOW);
    }));
    driverController.y().onChange(Commands.runOnce(() -> {
      superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.INTAKING);
      // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.STOW);
    }));
    ;
    driverController.x().onChange(Commands.runOnce(() -> {
      superStructureSubsystem.changeState(SuperStructureSubsystem.SuperStructureState.TEST);
      // intakeSubsystem.changeState(IntakeSubsystem.IntakeStates.STOW);
    }));
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
