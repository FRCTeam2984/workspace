package org.usfirst.frc2984;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.RGBImage;

public class CameraTools {
	
    AxisCamera camera;          // the axis camera object (connected to the switch)
    CriteriaCollection cc;      // the criteria for doing the particle filter operation
	
	public CameraTools(){
		camera = AxisCamera.getInstance();  // get an instance ofthe camera
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
	}
	
	
	public void takePic(){
		try {
            ColorImage image = camera.getImage();     // comment if using stored images
            image.write("image.jpg");
            BinaryImage thresholdImage = image.thresholdRGB(0, 45, 25, 255, 0, 47);   // keep only red objects
            thresholdImage.write("thresholdImage.jpg"); 
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
            bigObjectsImage.write("bigObjectsImage.jpg");
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            convexHullImage.write("convexHullImage.jpg");
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);// find filled in rectangles
            filteredImage.write("filteredImage.jpg");
            
            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
            for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x);
            }
            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */
            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
            
        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
	}
	
	public ParticleAnalysisReport track(String file){
		try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "10ft2.jpg"
             * 
             */
            ColorImage image = (file != null ? new RGBImage(file) : camera.getImage());
            BinaryImage thresholdImage = image.thresholdRGB(0, 45, 25, 255, 0, 47);   // keep only green objects
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);// find filled in rectangles
            
            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
            
            /*for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x);
            }
            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());*/
            
            ParticleAnalysisReport close = null;
            for (int i = 0; i < reports.length; i++) {
                if(close == null)
                	close = reports[i];
                else {
                	if(close.center_mass_x_normalized > reports[i].center_mass_x_normalized)
                		close = reports[i];
                }
            }
            
            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
            
            return close;
            
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        } catch (AxisCameraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
