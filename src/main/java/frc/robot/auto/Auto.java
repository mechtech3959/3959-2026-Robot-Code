package frc.robot.auto;

import org.littletonrobotics.junction.Logger;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import static frc.robot.generated.ChoreoTraj.Test;
import static frc.robot.generated.ChoreoTraj.TestAcc;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;

public class Auto {
   // private final SuperStructureSubsystem superStructure;
    private final DrivetrainSubsystem drivetrain;
    private final AutoFactory autoFactory;
    private final AutoChooser autoChooser;

    public Auto(DrivetrainSubsystem drivetrain) {
        this.autoChooser = new AutoChooser();

        this.drivetrain = drivetrain;

        this.autoFactory = drivetrain.makeAutoFactory();
    }

    public void configure() {

        autoChooser.addRoutine("Test", this::testRoutine);
        autoChooser.addRoutine("Blue Middle", this::BlueMiddleRoutine);

        SmartDashboard.putData("Auto Chooser", autoChooser);
        RobotModeTriggers.autonomous().whileTrue(autoChooser.selectedCommandScheduler());
    }

    public AutoRoutine testRoutine() {
        final AutoRoutine routine = autoFactory.newRoutine("test");
        final AutoTrajectory test = Test.asAutoTraj(routine);
        Logger.recordOutput("Auto", test.getRawTrajectory().getPoses());

        // When the routine becomes active, reset odometry then follow the trajectory
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        // test.resetOdometry(), // Reset pose to trajectory start
                        test.cmd() // Follow the trajectory
                ));

        return routine;
    }

    public AutoRoutine BlueMiddleRoutine() {
        final AutoRoutine routine = autoFactory.newRoutine("blue_middle");
        final AutoTrajectory test = TestAcc.asAutoTraj(routine);
        Logger.recordOutput("Auto", test.getRawTrajectory().getPoses());

        // When the routine becomes active, reset odometry then follow the trajectory
        routine.active().onTrue(
                Commands.sequence(
                        Commands.print("Started the routine!"),
                        test.resetOdometry(), // Reset pose to trajectory start
                        test.cmd() // Follow the trajectory
                ));

        return routine;
    }

}
