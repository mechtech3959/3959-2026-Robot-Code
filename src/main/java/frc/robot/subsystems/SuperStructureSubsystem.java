package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.conveyor.ConveyorSubsystem;
import frc.robot.subsystems.conveyor.ConveyorSubsystem.ConveyorStates;

 

public class SuperStructureSubsystem extends SubsystemBase {
  private final ConveyorSubsystem conveyor;
  public SuperStructureSubsystem(ConveyorSubsystem conveyor) {
    this.conveyor = conveyor;
  }
   
  public void setIntakeToRun() {
    conveyor.setConveyorState(ConveyorStates.RUN);
  }

  public void setIntakeToStop(){
    conveyor.setConveyorState(ConveyorStates.STOP);
  }    
}
