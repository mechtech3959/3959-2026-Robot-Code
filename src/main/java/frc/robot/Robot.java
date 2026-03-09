// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.util.ProjectileTrajectory;

public class Robot extends LoggedRobot {
  private final RobotContainer m_robotContainer;

  public Robot() {
    Logger.recordMetadata("ProjectName", "2026Comp"); // Set a metadata value

    if (isReal()) {
      Logger.addDataReceiver(new WPILOGWriter()); // Log to a USB stick ("/U/logs")
      Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
    } else {
      Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables

      setUseTiming(false); // Run as fast as possible
      // Pull the replay log from AdvantageScope (or prompt the user)
     // String logPath = LogFileUtil.findReplayLog();
     // Logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
      // Save outputs to a new log
     // Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
    }

    m_robotContainer = new RobotContainer();
    Logger.start();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void disabledExit() {
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void autonomousExit() {
    m_robotContainer.endTransition();
  }

  @Override
  public void teleopInit() {
    ProjectileTrajectory.calculateTrajectory(((double)Units.feetToMeters(3)));
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void teleopExit() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void testExit() {
  }
}
