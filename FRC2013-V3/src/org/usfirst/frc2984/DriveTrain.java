package org.usfirst.frc2984;

import edu.wpi.first.wpilibj.Jaguar;

public class DriveTrain
{
	private Jaguar left1;
	private Jaguar left2;
	private Jaguar right1;
	private Jaguar right2;
	public static final int LEFT1 = 9;
    public static final int LEFT2 = 10;
    public static final int RIGHT1 = 1;
    public static final int RIGHT2 = 2;
/*    public static final int SHOOTER1 = 7;
    public static final int SHOOTER2 = 8;
    public static final int BANDS = 3;
	public static final int INSERTER = 5;		*/
	
	public DriveTrain()
	{
		left1 = new Jaguar(LEFT1);
		left2 = new Jaguar(LEFT2);
		right1 = new Jaguar(RIGHT1);
		right2 = new Jaguar(RIGHT2);
	}
	public void drive(double throttle, double turn) {
		double left = throttle + turn;
		double right = -throttle + turn;
		left1.set(-left);
		left2.set(-left);
		right1.set(-right);
		right2.set(-right);
	}
}
