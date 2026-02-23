// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.auto.Auto;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drivetrain.DrivetrainIOCTRE;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystems.shooter.ShooterCTREIO;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class RobotContainer {
  private final ShooterCTREIO shooterIO;
  private final ShooterSubsystem shooterSubsystem;
  private final DrivetrainIOCTRE drivetrainIO;
  private final DrivetrainSubsystem drivetrainSubsystem;
  private final CommandXboxController driverController = new CommandXboxController(0);
  private final Auto autom;

  @SuppressWarnings("unchecked")
  public RobotContainer() {
    shooterIO = new ShooterCTREIO();
    shooterSubsystem = new ShooterSubsystem(shooterIO);
    drivetrainIO = new DrivetrainIOCTRE(TunerConstants.DrivetrainConstants, TunerConstants.FrontLeft,
        TunerConstants.FrontRight, TunerConstants.BackLeft, TunerConstants.BackRight);
    drivetrainSubsystem = new DrivetrainSubsystem(drivetrainIO, driverController);
    autom = new Auto(drivetrainSubsystem);

    configureBindings();
  }

  private void configureBindings() {
    autom.configure();
  }
}
