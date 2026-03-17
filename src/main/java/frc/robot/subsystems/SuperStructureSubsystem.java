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
import static frc.robot.subsystems.indexer.IndexerSubsystem.IndexerStates;

public class SuperStructureSubsystem extends SubsystemBase {
    public enum SuperStructureState {
        IDLE,
        INTAKING,
        SHOOTING_AUTO,
        SHOOTING__TELEOP,
        TRAVEL,
        PREP_CLIMB,
        CLIMBING,
        STARTING_CONFIG,
        TEST
    }

    private final ConveyorSubsystem conveyor;
    private final IndexerSubsystem indexer;
    private final ShooterSubsystem shooter;
    private final IntakeSubsystem intake;
    private final ClimberSubsystem climber;
    // private final DrivetrainSubsystem drivetrain;

    private SuperStructureState currentSuperStructureState = SuperStructureState.IDLE;
    private SuperStructureState requestedSuperStructureState = SuperStructureState.IDLE;

    public SuperStructureSubsystem(ConveyorSubsystem conveyor, ShooterSubsystem shooter, IntakeSubsystem intake,
            IndexerSubsystem indexer, ClimberSubsystem climber) {
        this.conveyor = conveyor;
        this.shooter = shooter;
        this.intake = intake;
        this.climber = climber;
        this.indexer = indexer;
        // this.drivetrain = drivetrain;
    }

    private void applyState() {

        switch (currentSuperStructureState) {

            case IDLE -> {
                intake.changeState(IntakeStates.INTAKE, FeedStates.STOP);
                conveyor.changeState(ConveyorStates.STOP);
                indexer.changeState(IndexerStates.STOP);
                shooter.changeState(ShooterStates.REST);
            }
            case INTAKING -> {
                intake.changeState(IntakeStates.INTAKE, FeedStates.PERCENTOUTPUT, 0.5);
                conveyor.changeState(ConveyorStates.RUN);
                // indexer.changeState(IndexerStates.RUN);
                // shooter.changeState(ShooterStates.INTAKE);
            }
            case SHOOTING_AUTO -> {
            }
            case SHOOTING__TELEOP -> {
            }
            case TRAVEL -> {
                intake.changeState(IntakeStates.MID_STOW, FeedStates.STOP);
                conveyor.changeState(ConveyorStates.STOP);
                indexer.changeState(IndexerStates.STOP);
                shooter.changeState(ShooterStates.REST);
            }
            case CLIMBING -> {
                intake.changeState(IntakeStates.STOW, FeedStates.STOP);
                conveyor.changeState(ConveyorStates.STOP);
                indexer.changeState(IndexerStates.STOP);
                shooter.changeState(ShooterStates.REST);
                climber.changeState(ClimberStates.CLIMB);

            }
            case PREP_CLIMB -> {
                climber.changeState(ClimberStates.CLEAR_INTAKE);
                intake.changeState(IntakeStates.STOW, FeedStates.STOP);
                conveyor.changeState(ConveyorStates.STOP);
                indexer.changeState(IndexerStates.STOP);
                shooter.changeState(ShooterStates.REST);

            }
            case STARTING_CONFIG -> {
                 climber.changeState(ClimberStates.STARTING_CONFIG);
                intake.changeState(IntakeStates.STOW, FeedStates.STOP);
                conveyor.changeState(ConveyorStates.STOP);
                indexer.changeState(IndexerStates.STOP);
                shooter.changeState(ShooterStates.REST);
            }
            case TEST -> {
                intake.changeState(IntakeStates.INTAKE, FeedStates.PERCENTOUTPUT, 0.0);
                conveyor.changeState(ConveyorStates.RUN);
                indexer.changeState(IndexerStates.RUN);
                shooter.changeState(ShooterStates.KNOWN_CLOSE);
            }
            default -> System.out.println("Error in SuperStructure Subsystem: State applied to "
                    + "non-existing option/undefined error.");
        }
    }

    public void changeState(SuperStructureState newState) {
        this.requestedSuperStructureState = newState;
        this.currentSuperStructureState = requestedSuperStructureState;
    }

    @Override
    public void periodic() {
        applyState();
    }
}
