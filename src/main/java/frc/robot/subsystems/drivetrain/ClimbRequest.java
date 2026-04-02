package frc.robot.subsystems.drivetrain;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.controls.CoastOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;

// A SwerveRequest that applies a constant coast to the drive motors and holds the turning motors straight, used for climbing
//DO NOT USE THIS NORMALLY, CTRE EXPLICITLY WARNED US NOT TO USE THIS, ONLY USE THIS IN THE CLIMBING STATE OF THE DRIVETRAIN.
public class ClimbRequest implements SwerveRequest {
    private final PositionVoltage m_TurnRequest = new PositionVoltage(0);
    private final CoastOut m_driveRequest = new CoastOut();

    public ClimbRequest() {
    }

    @SafeVarargs
    @Override
    public final StatusCode apply(SwerveDrivetrain.SwerveControlParameters parameters,
            SwerveModule<?, ?, ?>... modulesToApply) {


        for (SwerveModule<?, ?, ?> rawModule : modulesToApply) {
            // Cast to the concrete module type expected by this request.
            @SuppressWarnings("unchecked")
            SwerveModule<TalonFX, TalonFX, CANcoder> module = (SwerveModule<TalonFX, TalonFX, CANcoder>) rawModule;

 

            // Apply the calculated request to the module
            module.apply(m_driveRequest, m_TurnRequest.withPosition(0.5));
        }
        return StatusCode.OK;
    }

}
