// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.shooter.ShooterCTREIO;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class RobotContainer {
  private final ShooterCTREIO shooterIO;
  private final ShooterSubsystem shooterSubsystem;
  CommandXboxController driverController = new CommandXboxController(0);

  public RobotContainer() {
    shooterIO = new ShooterCTREIO();
    shooterSubsystem = new ShooterSubsystem(shooterIO);
    configureBindings();
  }

  private void configureBindings() {
    driverController.a().onTrue(Commands.runOnce(
        () -> shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.KNOWN_CLOSE, -55)));
    driverController.b().onTrue(Commands.runOnce(
        () -> shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.KNOWN_CLOSE, -50)));
    driverController.x().onTrue(Commands.runOnce(
        () -> shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.UNKNOWN, 0)));
    driverController.y().onTrue(Commands.runOnce(
        () -> shooterSubsystem.ChangeShooterState(ShooterSubsystem.ShooterMode.KNOWN_CLOSE, -60)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
