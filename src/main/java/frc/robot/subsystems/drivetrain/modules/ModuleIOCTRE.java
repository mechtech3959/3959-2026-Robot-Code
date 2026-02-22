package frc.robot.subsystems.drivetrain.modules;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.traits.CommonTalon;
import com.ctre.phoenix6.swerve.SwerveModule;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;

public class ModuleIOCTRE implements ModuleIO {
    private final CommonTalon driveMotor;
    private final CommonTalon turnMotor;
    private final CANcoder cancoder;

    private final StatusSignal<Angle> drivePos;
    private final StatusSignal<AngularVelocity> driveVel;
    private final StatusSignal<Double> driveRef;
    private final StatusSignal<Voltage> driveVolts;
    private final StatusSignal<Current> driveSupplyAmps;
    private final StatusSignal<Current> driveStatorAmps;
    private final StatusSignal<Temperature> driveTemp;

    private final StatusSignal<Angle> turnPos;
    private final StatusSignal<Double> turnRef;
    private final StatusSignal<AngularVelocity> turnVel;
    private final StatusSignal<Voltage> turnVolts;
    private final StatusSignal<Current> turnSupplyAmps;
    private final StatusSignal<Current> turnStatorAmps;
    private final StatusSignal<Temperature> turnTemp;

    private final StatusSignal<Angle> absolutePos;

    public ModuleIOCTRE(SwerveModule<TalonFX, TalonFX, CANcoder> module) {
        this.driveMotor = module.getDriveMotor();
        this.turnMotor = module.getSteerMotor();
        this.cancoder = module.getEncoder();

        drivePos = driveMotor.getPosition();
        driveVel = driveMotor.getVelocity();
        driveRef = driveMotor.getClosedLoopReference();
        driveVolts = driveMotor.getMotorVoltage();
        driveSupplyAmps = driveMotor.getSupplyCurrent();
        driveStatorAmps = driveMotor.getStatorCurrent();
        driveTemp = driveMotor.getDeviceTemp();

        turnPos = turnMotor.getPosition();
        turnVel = turnMotor.getVelocity();
        turnRef = turnMotor.getClosedLoopReference();
        turnVolts = turnMotor.getMotorVoltage();
        turnSupplyAmps = turnMotor.getSupplyCurrent();
        turnStatorAmps = turnMotor.getStatorCurrent();
        turnTemp = turnMotor.getDeviceTemp();

        absolutePos = cancoder.getAbsolutePosition();
    }

    @Override
    public void updateInputs(ModuleIOInputs inputs) {
        BaseStatusSignal.refreshAll(
                drivePos, driveVel, driveRef, driveVolts, driveSupplyAmps, driveStatorAmps, driveTemp,
                turnPos, turnVel, turnRef, turnVolts, turnSupplyAmps, turnStatorAmps, turnTemp,
                absolutePos);

        inputs.drivePositionRad = Units.rotationsToRadians(
                BaseStatusSignal.getLatencyCompensatedValue(drivePos, driveVel).magnitude());
        inputs.driveVelocityRadPerSec = Units.rotationsToRadians(driveVel.getValueAsDouble());
        inputs.driveVelocitySetpointRadPerSec = Units.rotationsToRadians(driveRef.getValueAsDouble());
        inputs.driveAppliedVolts = driveVolts.getValueAsDouble();
        inputs.driveSupplyCurrentAmps = driveSupplyAmps.getValueAsDouble();
        inputs.driveStatorCurrentAmps = driveStatorAmps.getValueAsDouble();
        inputs.driveTemperature = driveTemp.getValueAsDouble();

        inputs.steerPositionRad = Units.rotationsToRadians(
                BaseStatusSignal.getLatencyCompensatedValue(turnPos, turnVel).magnitude());
        inputs.steerVelocityRadPerSec = Units.rotationsToRadians(turnVel.getValueAsDouble());
        inputs.steerPositionSetpointRad = Units.rotationsToRadians(turnRef.getValueAsDouble());
        inputs.steerAppliedVolts = turnVolts.getValueAsDouble();
        inputs.steerSupplyCurrentAmps = turnSupplyAmps.getValueAsDouble();
        inputs.steerStatorCurrentAmps = turnStatorAmps.getValueAsDouble();
        inputs.steerTemperature = turnTemp.getValueAsDouble();

        inputs.steerAbsolutePositionRad = Units.rotationsToRadians(absolutePos.getValueAsDouble());

        inputs.driveConnected = drivePos.getStatus().isOK();
        inputs.steerConnected = turnPos.getStatus().isOK();
        inputs.encoderConnected = absolutePos.getStatus().isOK();
    }
}
