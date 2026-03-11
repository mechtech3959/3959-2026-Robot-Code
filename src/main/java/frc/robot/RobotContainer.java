// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.climber.ClimberCTREIO;
import frc.robot.subsystems.climber.ClimberSubsystem;

public class RobotContainer {

    private final CommandXboxController driver = new CommandXboxController(0);
    private final ClimberCTREIO climberIO = new ClimberCTREIO();
    private final ClimberSubsystem climber = new ClimberSubsystem(climberIO);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        driver.a().onTrue(climber.runOnce(() -> climber.setClimberState(ClimberSubsystem.ClimberStates.HOME)));
        driver.b().onTrue(climber.runOnce(() -> climber.setClimberState(ClimberSubsystem.ClimberStates.CLEAR_INTAKE)));
        driver.x().onTrue(climber.runOnce(() -> climber.setClimberState(ClimberSubsystem.ClimberStates.CLIMB)));
    }

}
