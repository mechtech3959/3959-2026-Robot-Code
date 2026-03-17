package frc.robot.subsystems.drivetrain;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.PositionVoltage;

import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;


public class ClimbRequest implements SwerveRequest {
    private  final PositionVoltage m_TurnRequest = new PositionVoltage(0);
    private final CoastOut m_driveRequest = new CoastOut();
    public ClimbRequest() {
    }

    @Override
    public StatusCode apply(SwerveDrivetrain.SwerveControlParameters parameters, SwerveModule... modulesToApply) {
        // Your custom logic goes here.
        // You can access global parameters like the current pose estimate
        // (parameters.getPose()) or time (parameters.getCurrentTime()).

        for (SwerveModule module : modulesToApply) {
            // Calculate the specific ModuleRequest for each module based on your logic

            // For example, make all wheels point in a specific direction:

             // Or whatever logic you need

            // Apply the calculated request to the module
            module.apply(m_driveRequest, m_TurnRequest.withPosition(0));
        }
        return StatusCode.OK;
    }

}
