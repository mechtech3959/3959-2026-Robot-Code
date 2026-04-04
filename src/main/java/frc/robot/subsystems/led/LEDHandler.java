package frc.robot.subsystems.led;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SuperStructureSubsystem;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class LEDHandler {
    private final LEDSubsystem ledSubsystem;
    private final SuperStructureSubsystem superStructureSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final DrivetrainSubsystem drivetrainSubsystem;
    private final Trigger match_EndTrigger;
    private final Trigger shootingTrigger;
    private final Trigger intakingTrigger;
    private final Trigger intakingAndShootingTrigger;
    private final Trigger neitherIntakingNorShootingTrigger;
    private final Trigger headingLockTrigger;

    public LEDHandler(LEDSubsystem ledSubsystem, SuperStructureSubsystem superStructureSubsystem,
            IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem,
            DrivetrainSubsystem drivetrainSubsystem) {
        this.ledSubsystem = ledSubsystem;
        this.superStructureSubsystem = superStructureSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.drivetrainSubsystem = drivetrainSubsystem;
        match_EndTrigger = RobotModeTriggers.disabled()
                .and(() -> (DriverStation.getMatchTime() == 0));
        shootingTrigger = new Trigger(() -> shooterSubsystem.getShooterState() != ShooterSubsystem.ShooterStates.REST);
        intakingTrigger = new Trigger(() -> intakeSubsystem.getIntakeState() == IntakeSubsystem.IntakeStates.INTAKE);
        intakingAndShootingTrigger = new Trigger(
                () -> (shooterSubsystem.getShooterState() != ShooterSubsystem.ShooterStates.REST)
                        && (intakeSubsystem.getIntakeState() == IntakeSubsystem.IntakeStates.INTAKE));
        neitherIntakingNorShootingTrigger = new Trigger(
                () -> (shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterStates.REST)
                        && (intakeSubsystem.getIntakeState() != IntakeSubsystem.IntakeStates.INTAKE));
        headingLockTrigger = new Trigger(() -> drivetrainSubsystem.getSwerveState() == DrivetrainSubsystem.SwerveStates.Heading);
    }

    public void handleLEDs() {

        match_EndTrigger
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeMainLEDState(LEDSubsystem.MainLEDStates.MATCH_END)));
        RobotModeTriggers.disabled().and(match_EndTrigger.negate())
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeMainLEDState(LEDSubsystem.MainLEDStates.DISABLED)));
        RobotModeTriggers.autonomous()
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeMainLEDState(LEDSubsystem.MainLEDStates.AUTO)));
        RobotModeTriggers.teleop()
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeMainLEDState(LEDSubsystem.MainLEDStates.TELEOP)));
        intakingTrigger.and(shootingTrigger.negate())
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeSecondSecondaryLEDState(LEDSubsystem.SecondSecondaryLEDStates.INTAKING)));
        shootingTrigger.and(intakingTrigger.negate())
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeSecondSecondaryLEDState(LEDSubsystem.SecondSecondaryLEDStates.SHOOTING)));
        neitherIntakingNorShootingTrigger
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeSecondSecondaryLEDState(LEDSubsystem.SecondSecondaryLEDStates.NEITHER)));
        intakingAndShootingTrigger
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeSecondSecondaryLEDState(LEDSubsystem.SecondSecondaryLEDStates.INTAKING_AND_SHOOTING)));
        headingLockTrigger
                .onTrue(Commands.runOnce(() -> ledSubsystem.changeSecondaryLEDState(LEDSubsystem.SecondaryLEDStates.HEADING_LOCK)));
        
    }
}
