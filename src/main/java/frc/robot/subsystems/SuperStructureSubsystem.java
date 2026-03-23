package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.conveyor.ConveyorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;
import org.littletonrobotics.junction.Logger;

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
        SHOOTING__CLOSE,
        SHOOTING__FAR,
        SHOOTING_STOP,
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
    private final DrivetrainSubsystem drivetrain;

    private SuperStructureState currentSuperStructureState = SuperStructureState.IDLE;
    // private SuperStructureState requestedSuperStructureState =
    // SuperStructureState.IDLE;

    public SuperStructureSubsystem(ConveyorSubsystem conveyor, ShooterSubsystem shooter, IntakeSubsystem intake,
            IndexerSubsystem indexer, ClimberSubsystem climber, DrivetrainSubsystem drivetrain) {
        this.conveyor = conveyor;
        this.shooter = shooter;
        this.intake = intake;
        this.climber = climber;
        this.indexer = indexer;
        this.drivetrain = drivetrain;
    }

    private void applyState() {

        switch (currentSuperStructureState) {

            case IDLE -> {
                intake.changeState(FeedStates.STOP);
                conveyor.changeState(ConveyorStates.STOP);
                indexer.changeState(IndexerStates.STOP);
                shooter.changeState(ShooterStates.REST);
            }
            case INTAKING -> intaking();
            case SHOOTING_AUTO -> {
            }
            case SHOOTING__CLOSE -> closeShoot();
            case SHOOTING__FAR -> farShoot();
            case SHOOTING_STOP -> stopShooting();
            case TRAVEL -> travel();
            case CLIMBING -> climbing();
            case PREP_CLIMB -> prep_Climb();
            case STARTING_CONFIG ->
                starting_Config();

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

    public void intaking() {

        if (intake.getState() == IntakeStates.INTAKE) {
            return;
        }
        indexer.changeState(IndexerStates.STOP);
        conveyor.changeState(ConveyorStates.STOP);
        shooter.changeState(ShooterStates.REST);
       // if (climber.getState() != ClimberStates.HOME)
       //     climber.changeState(ClimberStates.HOME);
       // if (climber.getPosition() < 90) {
            intake.changeState(IntakeStates.INTAKE, FeedStates.RUN, 0.5);

       // }
        // if (intake.getState() != IntakeStates.INTAKE)
        // intaking();

    }

    public void starting_Config() {
        if (intake.getState() == IntakeStates.STOW && climber.getState() == ClimberStates.STARTING_CONFIG) {
            return;
        }
        indexer.changeState(IndexerStates.STOP);
        conveyor.changeState(ConveyorStates.STOP);
        if (intake.getPosition() > 0.1 && climber.getPosition() < 90) {
            intake.changeState(IntakeStates.STOW, FeedStates.STOP);

        } else if (intake.getPosition() < 0.05 && (climber.getState() != ClimberStates.STARTING_CONFIG)) {
            climber.changeState(ClimberStates.STARTING_CONFIG);
        } else if (intake.getPosition() > 0.05 && climber.getPosition() > 90) {
            climber.changeState(ClimberStates.HOME);
        } else {
            // starting_Config();
        }

    }

    public void travel() {
        if (intake.getState() == IntakeStates.MID_STOW) {
            return;
        }
        indexer.changeState(IndexerStates.STOP);
        conveyor.changeState(ConveyorStates.STOP);
     //   if (climber.getState() != ClimberStates.HOME)
        //    climber.changeState(ClimberStates.HOME);
     //   if (climber.getPosition() < 90) {
            intake.changeState(IntakeStates.MID_STOW, FeedStates.STOP);
     //   }
        // travel();

    }

    public void prep_Climb() {
        if (intake.getState() == IntakeStates.STOW && climber.getState() == ClimberStates.CLEAR_INTAKE) {
            return;
        }
        indexer.changeState(IndexerStates.STOP);
        conveyor.changeState(ConveyorStates.STOP);
        climber.changeState(ClimberStates.CLEAR_INTAKE);
        if (intake.getState() != IntakeStates.STOW && climber.getPosition() < 90)
            intake.changeState(IntakeStates.STOW, FeedStates.STOP);
        // if (climber.isAtTarget()) {
        // intake.changeState(IntakeStates.STOW, FeedStates.STOP);
        // } // else {
        // prep_Climb();
        // }
    }

    public void climbing() {
        if (climber.getState() == ClimberStates.CLIMB) {
            return;
        }
        indexer.changeState(IndexerStates.STOP);
        conveyor.changeState(ConveyorStates.STOP);
        shooter.changeState(ShooterStates.REST);
        drivetrain.changeState(DrivetrainSubsystem.SwerveStates.Climb);

        if (intake.getState() == IntakeStates.STOW) {
            climber.changeState(ClimberStates.CLIMB);
        }

        // else {
        // climbing();
        // }
    }

    public void autoShoot() {
      //  intake.changeState(IntakeStates.MID_STOW, FeedStates.STOP);
        indexer.changeState(IndexerStates.RUN);
        conveyor.changeState(ConveyorStates.RUN);
        shooter.changeState(ShooterStates.AUTO);
    }

    public void closeShoot() {
      //  intake.changeState(IntakeStates.MID_STOW, FeedStates.STOP);
        indexer.changeState(IndexerStates.RUN);
        conveyor.changeState(ConveyorStates.RUN);
        shooter.changeState(ShooterStates.KNOWN_CLOSE);

    }

    public void farShoot() {
      //  intake.changeState(IntakeStates.MID_STOW, FeedStates.STOP);
        indexer.changeState(IndexerStates.RUN);
        conveyor.changeState(ConveyorStates.RUN);
        shooter.changeState(ShooterStates.KNOWN_FAR);

    }

    public void stopShooting() {
    //    intake.changeState(IntakeStates.MID_STOW, FeedStates.STOP);
        indexer.changeState(IndexerStates.STOP);
        conveyor.changeState(ConveyorStates.STOP);
        shooter.changeState(ShooterStates.REST);
    }

    public void changeState(SuperStructureState newState) {
        // this.requestedSuperStructureState = newState;
        this.currentSuperStructureState = newState;
    }

    @Override
    public void periodic() {
        Logger.recordOutput("SuperStructure State", currentSuperStructureState);
        applyState();
    }
}
