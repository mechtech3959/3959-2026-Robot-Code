package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.conveyor.ConveyorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;

import static frc.robot.subsystems.intake.feed.FeedSubsystem.FeedStates;
import static frc.robot.subsystems.shooter.ShooterSubsystem.ShooterStates;
import static frc.robot.subsystems.intake.IntakeSubsystem.IntakeStates;
import static frc.robot.subsystems.climber.ClimberSubsystem.ClimberStates;
import static frc.robot.subsystems.conveyor.ConveyorSubsystem.ConveyorStates;

public class SuperStructureSubsystem extends SubsystemBase {
  public enum SuperStructureState {
    IDLE,
    INTAKING,
    SHOOTING_AUTO,
    SHOOTING__TELEOP,
    TRAVEL,

    CLIMBING
  }

  private final ConveyorSubsystem conveyor;
  private final IndexerSubsystem indexer;
  private final ShooterSubsystem shooter;
  private final IntakeSubsystem intake;
  private final ClimberSubsystem climber;
  private final DrivetrainSubsystem drivetrain;

  private SuperStructureState currentSuperStructureState = SuperStructureState.IDLE;
  private SuperStructureState requestedSuperStructureState = SuperStructureState.IDLE;

  public SuperStructureSubsystem(ConveyorSubsystem conveyor, ShooterSubsystem shooter, IntakeSubsystem intake,
      ClimberSubsystem climber, IndexerSubsystem indexer, DrivetrainSubsystem drivetrain) {
    this.conveyor = conveyor;
    this.shooter = shooter;
    this.intake = intake;
    this.climber = climber;
    this.indexer = indexer;
    this.drivetrain = drivetrain;
  }

  private void applyState() {
    switch (currentSuperStructureState) {
      case IDLE -> {}
      case INTAKING -> {}
      case SHOOTING_AUTO -> {}
      case SHOOTING__TELEOP -> {}
      case TRAVEL -> {}
      case CLIMBING -> {}
    }
  }

  @Override
  public void periodic() {
  }
}
