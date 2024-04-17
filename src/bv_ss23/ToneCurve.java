// BV Ue4 SS2023 Vorgabe
//
// Copyright (C) 2023 by Klaus Jung
// All rights reserved.
// Date: 2023-03-23
 		   	  	  		

package bv_ss23;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ToneCurve {
 		   	  	  		
	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    
    private int[] grayTable = new int[grayLevels];
 		   	  	  		
	public int[] getGrayTable() {
		return grayTable;
	}

	public ToneCurve(GraphicsContext gc) {
		this.gc = gc;
	}
	
	public void updateTable(int brightness, double contrast, double gamma) {
	    // brightness change
	    for (int i = 0; i < grayLevels; i++) {
	        int newBrightness = i + brightness;
	        newBrightness = Math.max(0, Math.min(grayLevels - 1, newBrightness)); // Ensure the value is within the valid range of 256 graylevel values
	        grayTable[i] = newBrightness; //assigns value of brightness to the i'th index of graytable array.
	    }

	    
	    // newContrast = 2*(128-256/2)+256/2=128 middle value unchanged
	    // contrast
	    for (int i = 0; i < grayLevels; i++) {
	        								// difference between graylevel and contrsst and middle value, multiplay by contrast value to scale
	    	double newContrast = contrast * (grayTable[i] - grayLevels / 2) + grayLevels / 2;//add scaled difference to middle value
	        newContrast = Math.max(0, Math.min(grayLevels - 1, newContrast)); // Ensure the value is within the valid range
	        grayTable[i] = (int) newContrast;
	    }

	    

	    // gamma correction
	    
	    
	    double y = 1.0 / gamma; // Exponentenwert

	    for (int i = 0; i < grayLevels; i++) {
	        double adjustedGamma = (255.0 * Math.pow(grayTable[i], y)) / Math.pow(255.0, y);
	        int newGamma = (int) Math.round(adjustedGamma);
	        newGamma = Math.max(0, Math.min(grayLevels - 1, newGamma));
	        grayTable[i] = newGamma;
	    }


	}




		
		// TODO: Fill the grayTable[] array to map gay input values to gray output values.
		// It will be used as follows: grayOut = grayTable[grayIn].
		//
		// Use brightness, contrast, and gamma settings.
		//
		// See "Gammakorrektur" at slide no. 20 of 
		// http://home.htw-berlin.de/~barthel/veranstaltungen/GLDM/vorlesungen/04_GLDM_Bildmanipulation1_Bildpunktoperatoren.pdf
		//
		// First apply the brightness change, afterwards the contrast modification and finally the gamma correction.

	
	
	public void applyTo(RasterImage image) {
	    
	  // image.convertToGray();

	    int[] argb = image.argb; //retrieve array of ARGB pixel values
	    for (int i = 0; i < argb.length; i++) {
	    	// int grayIn = argb[i] & 0xFF;
	    	int grayIn = image.getGray(i); // Grayscale value already obtained from image conversion
	        // Map the gray input value to gray output value using the grayTable
	        int grayOut = grayTable[grayIn];

	        // Set gray value for all RGB components
	        argb[i] = (0xFF000000) | (grayOut << 16) | (grayOut << 8) | grayOut;
	    }
	}

	public void draw(Color lineColor) {
	  
	    gc.clearRect(0, 0, grayLevels, grayLevels);
	    gc.setStroke(lineColor);
	    gc.setLineWidth(3);

	    // initiate tone curve
	    int[] Table = new int[grayLevels]; //store values for the tone curve
	    for (int i = 0; i < grayLevels; i++) { // initialize Table array with tone curve values
	        Table[i] = grayLevels - grayTable[i]; // flip the tone curve into correct alignment
	    }

	    // Draw the tone curve, adjust by 0.5
	    double shift = 0.5;
	    gc.beginPath();
	    gc.moveTo(shift, Table[0] + shift); //sets initial position to being drawing

	    for (int i = 1; i < grayLevels; i++) {
	        gc.lineTo(i + shift, Table[i] + shift);//horizontal axis, vertical axis
	    }

	    gc.stroke();
	}


 		   	  	  		
}
 		   	  	  		




