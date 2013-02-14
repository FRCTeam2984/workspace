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
public class Drivetrain {
    private Jaguar left1;
    private Jaguar left2;
    private Jaguar right1;
    private Jaguar right2;
    
    public Drivetrain(){
        left1 = new Jaguar(RobotMap.LEFT1);
        left2 = new Jaguar(RobotMap.LEFT2);
        right1 = new Jaguar(RobotMap.RIGHT1);
        right2 = new Jaguar(RobotMap.RIGHT2);
    }
    
    public void drive(double throttle, double turn, boolean quickTurn){
        double overPower = 0.0;
        double left = 0.0;
        double right = 0.0;
        
        if(quickTurn)
            overPower = 1.0;
        else
            turn *= Math.abs(throttle);
        
        left = throttle + turn;
        right = throttle - turn;
        
        if(left > 1.0){
            right -= overPower * (left - 1.0);
            left = 1.0;
        }
        else if(right > 1.0){
            left -= overPower * (right - 1.0);
            right = 1.0;
        }
        else if(left > 1.0){
            right += overPower * (-1.0 - left);
            left = -1.0;
        }
        else if(right < -1.0){
            left += overPower * (-1.0 - right);
            right = -1.0;
        }
        
        tankDrive(left, right);
    }
    
    public void tankDrive(double left, double right){
        left1.set(left);
        left2.set(left);
        right1.set(right);
        right2.set(right);
    }
}
