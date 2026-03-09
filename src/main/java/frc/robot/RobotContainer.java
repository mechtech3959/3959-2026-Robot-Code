// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.auto.Auto;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drivetrain.DrivetrainCTREIO;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystems.shooter.ShooterCTREIO;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.intake.*;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeStates;
import frc.robot.subsystems.intake.feed.*;

public class RobotContainer {
  // private final ShooterCTREIO shooterIO;
  // private final ShooterSubsystem shooterSubsystem;
  // private final DrivetrainCTREIO drivetrainIO;
  // private final DrivetrainSubsystem drivetrainSubsystem;
  private final FeedCTREIO feedIO;
  private final FeedSubsystem feedSubsystem;
  private final IntakeREVIO intakeIO;
  private final IntakeSubsystem intakeSubsystem;
  private final CommandXboxController driverController = new CommandXboxController(0);

  // private final Auto autom;

  public RobotContainer() {
    // shooterIO = new ShooterCTREIO();
    // shooterSubsystem = new ShooterSubsystem(shooterIO);
    // drivetrainIO = new DrivetrainCTREIO(TunerConstants.DrivetrainConstants,
    // TunerConstants.FrontLeft,
    // TunerConstants.FrontRight, TunerConstants.BackLeft,
    // TunerConstants.BackRight);
    // drivetrainSubsystem = new DrivetrainSubsystem(drivetrainIO,
    // driverController);
    feedIO = new FeedCTREIO();
    feedSubsystem = new FeedSubsystem(feedIO);
    intakeIO = new IntakeREVIO();
    intakeSubsystem = new IntakeSubsystem(intakeIO, feedSubsystem);
    // autom = new Auto(drivetrainSubsystem);

    configureBindings();
  }

  private void configureBindings() {
    driverController.a().onTrue(Commands.runOnce(
        () -> intakeSubsystem.setIntakeState(IntakeStates.TEST, FeedSubsystem.FeedStates.PERCENTOUTPUT, 0.25)));
    driverController.b().onTrue(Commands.runOnce(
        () -> intakeSubsystem.setIntakeState(IntakeStates.TEST, FeedSubsystem.FeedStates.PERCENTOUTPUT, 0.5)));
    driverController.x().onTrue(Commands.runOnce(
        () -> intakeSubsystem.setIntakeState(IntakeStates.TEST, FeedSubsystem.FeedStates.PERCENTOUTPUT, 0.75)));
    driverController.y().onTrue(Commands.runOnce(
        () -> intakeSubsystem.setIntakeState(IntakeStates.TEST, FeedSubsystem.FeedStates.PERCENTOUTPUT, 1)));
  }

}
