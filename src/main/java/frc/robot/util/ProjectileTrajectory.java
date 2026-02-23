package frc.robot.util;

import edu.wpi.first.math.util.Units;

public class ProjectileTrajectory {
    public static double wheelDiameter = 0.1016; // meters 106.1-mm 4-inches
    public static double gravity = 9.81; // m/s^2
    public static double shooterAngle = Units.degreesToRadians(50); // radians
    public static double shooterHeight = 0.7112; // meters
    public static double targetHeight = Units.inchesToMeters(72); // meters
    // the target area is the distance from the center of the target to the edge of the target, which is the diameter of the target
    public static double targetArea = Units.inchesToMeters(41.73); // meters 41.73-inches 1.06-meters
    public static double ballWeight = 0.226796; // kg 5 lbs
    public static double ballDiameter = 0.1501; // meters 150.1-mm 5.91-inches

    public static double RPMToVelocityPerSecond(double rps) {
        return (rps * Math.PI * wheelDiameter);
    }

    public void calculateTrajectory(double distanceToTarget) {
        // Calculate the initial velocity needed to hit the target
        double initialVelocity = Math.sqrt((gravity * distanceToTarget * distanceToTarget) / (2 * (targetHeight - shooterHeight) * Math.cos(shooterAngle) * Math.cos(shooterAngle)));

        // Convert the initial velocity to RPM
        double rps = initialVelocity / (Math.PI * wheelDiameter);
        double rpm = rps * 60;

        System.out.println("Initial Velocity: " + initialVelocity + " m/s");
        System.out.println("Required RPM: " + rpm);
    }

}
