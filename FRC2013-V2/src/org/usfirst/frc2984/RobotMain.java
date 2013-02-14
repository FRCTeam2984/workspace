/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2984;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends SimpleRobot {
    
    Drivetrain2 drivetrain;
    Joystick driveControls;
    Joystick driveControls2;
    Solenoid wedge1;
    Solenoid wedge2;
    Compressor comp;
    Encoder en1;
    Gyro gyro1;
    CameraTools cam;
	private long startTime;
    private final static double JOYSTICK_SENSITIVITY = 1;
    private final static double TRACKING_ERROR = .05;
    
    public void robotInit(){
        drivetrain = new Drivetrain2();
        driveControls = new Joystick(1);
        driveControls2 = new Joystick(2);
        gyro1 = new Gyro(1);
        wedge1 = new Solenoid(1);
        wedge2 = new Solenoid(2);
        //comp = new Compressor(15,1);
        en1 = new Encoder(13,14);
        en1.setDistancePerPulse(.0524);//For 6in diameter wheel
        en1.start();
        
        //cam = new CameraTools();
        
        /*for(int y = 0; y < 100; y++){
        	int time = System;
        }*/
        
    }

    public void operatorControl() {
    	
    	double shooterSpeed = -.8;

    	drivetrain.setShooter1(shooterSpeed);
    	drivetrain.setShooter2(-1.0);
        
        //comp.start();
        
        while(isOperatorControl() && isEnabled()){
            
         
            
            if(driveControls.getRawButton(6))  {
                drivetrain.fire();
            }
            
            if(driveControls.getRawButton(2)){
            	if(shooterSpeed > -1.0) shooterSpeed -= .01;
            	drivetrain.setShooter1(shooterSpeed);
            	//drivetrain.setShooter2(shooterSpeed);
            	System.out.println("Shooter speed: " + shooterSpeed);
            	Timer.delay(.1);
            }
            
            
            if(driveControls.getRawButton(1)){
            	if(shooterSpeed < 0) shooterSpeed += .01;
            	drivetrain.setShooter1(shooterSpeed);
            	//drivetrain.setShooter2(shooterSpeed);
            	System.out.println("Shooter speed: " + shooterSpeed);
            	Timer.delay(.1);
            }
            
            
            /*
            if(driveControls.getRawButton(2)){
            	en1.reset();
            	System.out.println("Encoder 1 Reset:" + en1.getDistance());
            }
            
            if(driveControls.getRawButton(1)){
            	System.out.println(en1.getDistance());
            }*/
            
            if(driveControls.getRawButton(3)){
            	System.out.println(gyro1.getAngle());
            }
            
            if(driveControls.getRawButton(4)){
            	gyro1.reset();
            	System.out.println("Gyro 1 Reset:" + gyro1.getAngle());
            	
            }
            
            
            if(driveControls.getRawButton(10) && System.currentTimeMillis() - startTime > 2000){
            	startTime = System.currentTimeMillis();
            	//cam.takePic();
            }
            
            drivetrain.drive(regression(driveControls.getRawAxis(2)), regression(driveControls.getRawAxis(1)));
            //drivetrain.drive(driveControls.getRawAxis(2), driveControls.getRawAxis(1));
            
            Timer.delay(.01);
        }
    }
    
    public double regression(double d){
        return JOYSTICK_SENSITIVITY * (d*d*d) + (1-JOYSTICK_SENSITIVITY) * d;
    }
    
    public void autonomous() {
        while (isAutonomous() && isEnabled()) {
            ParticleAnalysisReport r = cam.track(null);
            
            boolean move = false;
            
            if(r != null){
            	
            	//int off = r.imageWidth - r.center_mass_x;
            	
            	if(Math.abs(r.center_mass_x_normalized) < TRACKING_ERROR ){
            		//TODO: Shoot
            		drivetrain.drive(0,0);
            	} else if(move) drivetrain.drive(r.center_mass_x_normalized,r.center_mass_x_normalized);
            	
            	System.out.println(r.center_mass_x_normalized);
            	
            	
            	
            }
            
            
            
            
            
            Timer.delay(.1);
        }
    }
}
