/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2984;

import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author jason
 */
public class Drivetrain2 {

	private Jaguar left1;
    private Jaguar left2;
    private Jaguar right1;
    private Jaguar right2;
    private Jaguar shooter1;
    private Jaguar shooter2;
    private Jaguar launcher;
    private final double LAUNCHER_SPEED = .50;
    private final long LAUNCHER_TIME = 440;
    
    public Drivetrain2(){
        left1 = new Jaguar(RobotMap.LEFT1);
        left2 = new Jaguar(RobotMap.LEFT2);
        right1 = new Jaguar(RobotMap.RIGHT1);
        right2 = new Jaguar(RobotMap.RIGHT2);
        shooter1 = new Jaguar(RobotMap.SHOOTER1);
        shooter2 = new Jaguar(RobotMap.SHOOTER2);
        launcher = new Jaguar(RobotMap.INSERTER);
        firing = false;
        
    }
    
    public void drive(double throttle, double turn){
        double left = 0.0;
        double right = 0.0;
        
        
        left = -throttle - turn;
        right = throttle - turn;
        
        tankDrive(left, right);
    }
    
    public void setShooter1(double d){
        shooter1.set(d);
    }
    
    public void setShooter2(double d){
        shooter2.set(d);
    }
    
    public void tankDrive(double left, double right){
        left1.set(left);
        left2.set(left);
        right1.set(right);
        right2.set(right);
    }
    
    private boolean firing;

	public void fire() {
		
		if(firing)
			return;
		else firing = true;
		
		Thread t = new Thread(){
			public void run(){
				launcher.set(LAUNCHER_SPEED);
				try {
					Thread.sleep(LAUNCHER_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					launcher.set(0);
					firing = false;
				}
				
			}
		};
		t.start();
		
	}
}
