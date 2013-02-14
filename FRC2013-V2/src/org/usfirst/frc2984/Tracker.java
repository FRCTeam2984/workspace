/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2984;

/**
 *
 * @author jason
 */
public class Tracker {
    
    private static final double P = .5;
    private static final double I = 0;
    private static final double D = 0;
    
    public Tracker(){
        
    }
    
    /**
     * Returns the speed at which the robot should be turning to align with the target.
     * @param error the error in pixels from the center of the
     *              camera to the center of the backboard
     * @param rate the rate at which the robot is currently going towards its goal.
     * @return how fast the robot should turn
     */
    public double getTurn(double error, double rate){
        return (P * error) - (D * rate);
    }
}
