/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2984;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotCode extends SimpleRobot {

	private Joystick controller1;
	private DriveTrain drive;
	private int regressionMultiplier;
	private final static double JOYSTICK_SENSITIVITY = 1;

	public void robotInit() {
		controller1 = new Joystick(1);
		drive = new DriveTrain();
		regressionMultiplier = 3;
	}

	/**
	 * This function is called once each time the robot enters autonomous mode.
	 */
	public void autonomous() {
	}

	/**
	 * This function is called once each time the robot enters operator control.
	 */
	public void operatorControl() {
		while (isEnabled() && isOperatorControl()) {
			drive.drive(regression(controller1.getRawAxis(2)),
					regression(controller1.getRawAxis(1))); 

			if (controller1.getRawButton(6)) {
				regressionMultiplier += 2;
				System.out.println(regressionMultiplier);
				Timer.delay(.5);
			}

			if (controller1.getRawButton(8)) {
				if (regressionMultiplier > 1)
					regressionMultiplier -= 2;
				System.out.println(regressionMultiplier);
				Timer.delay(.5);
			}

			/*
			 * if (controller1.getRawAxis(2) > .75 || controller1.getRawAxis(2)
			 * < -.75) { drive.drive(0,0); Timer.delay(2); }
			 */
			
			
		}
	}

	public double regression(double d) {
		double multiplier = d;
		for (int c = 1; c < regressionMultiplier; c++) {
			multiplier *= d;
		}
		return JOYSTICK_SENSITIVITY * multiplier + (1 - JOYSTICK_SENSITIVITY)
				* d;
	}
}
