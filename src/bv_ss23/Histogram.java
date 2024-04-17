// BV Ue4 SS2023 Vorgabe
//
// Copyright (C) 2023 by Klaus Jung
// All rights reserved.
// Date: 2023-03-23
 		   	  	  		

package bv_ss23;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.binding.When;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Histogram {
    private static final int grayLevels = 256;

    private GraphicsContext gc;
    private int maxHeight;

    private int[] histogram = new int[grayLevels];

    public Histogram(GraphicsContext gc, int maxHeight) {
        this.gc = gc;
        this.maxHeight = maxHeight;
    }

    public Histogram() {
		// TODO Auto-generated constructor stub
	}

	public int[] getValues() {
        return histogram;
    }

    public void setImageRegion(RasterImage image, int regionStartX, int regionStartY, int regionWidth, int regionHeight) {
        // Reset the histogram array
    		for (int i = 0; i < grayLevels; i++) {
            histogram[i]=0;
       }

        // Iterate over the region defined by the parameters
        for (int y = regionStartY; y < regionStartY + regionHeight; y++) {
            for (int x = regionStartX; x < regionStartX + regionWidth; x++) {
                // Get the pixel index in the argb array, same process as in previous exercises
                int pixelIndex = y * image.width + x;

                // Get the grayscale value of the pixel
                int pixel = image.argb[pixelIndex]; //get color information of the pixel
                int grayValue = (pixel >> 16) & 0xFF;

                // Increment the histogram count for the corresponding gray value
                histogram[grayValue]++;
            }
        }
    }
    
	//erste Säule die ungleich 0
	public Integer getMinimum() {
		for (int i =0; i<histogram.length; i++) {
			if(histogram[i]!=0) {
				return i;
			}
		// Will be used in Exercise 5.
		}
		return null;
		
	
	}
 	//letzte Säule    	  	  		
	public Integer getMaximum() {
		for(int i= histogram.length-1; i>0; i--) {
			if(histogram[i]!= 0) {
				return i;
			}
		}
		// Will be used in Exercise 5.
		return null;
	}
 		   	  	  		
	public Double getMean() {
		int sum = 0;
		int colorValues=0;
		for (int i=0; i< histogram.length;i++) {
			sum += i * histogram[i];
			colorValues+= histogram[i];
		}
		if(colorValues!=0) {
			double mean = (double) sum/colorValues;
			return mean;
		} else { 
			return null;
			
		}
	}
 		   	  	  		
	public Integer getMedian() {
	    int totalValues = 0;
	    for (int frequency : histogram) {
	        totalValues += frequency;
	    }

	    int medianIndex = totalValues / 2;
	    int cumulativeCount = 0;

	    for (int i = 0; i < histogram.length; i++) {
	        cumulativeCount += histogram[i];
	        if (cumulativeCount >= medianIndex) {
	            return i;
	        }
	    }

	    return null; // Handle the case when the histogram is empty
	}
 		   	  	  		
	public Double getVariance() {
	    int totalValues = 0;
	    double mean = 0.0;

	    for (int i = 0; i < histogram.length; i++) {
	        totalValues += histogram[i];
	        mean += i * histogram[i];
	    }

	    if (totalValues > 0) {
	        mean /= totalValues;

	        double squaredDiffSum = 0.0;
	        for (int i = 0; i < histogram.length; i++) {
	            double diff = i - mean;
	            squaredDiffSum += histogram[i] * (diff * diff);
	        }

	        return squaredDiffSum / totalValues;
	    }
		return mean; 
	    
	}
	
 		   	  	  		
	public Double getEntropy() {
	    int totalValues = 0;
	    for (int frequency : histogram) {
	        totalValues += frequency;
	    }

	    if (totalValues > 0) {
	        double entropy = 0.0;
	        for (int i = 0; i < histogram.length; i++) {
	            if (histogram[i] > 0) {
	                double probability = (double) histogram[i] / totalValues;
	                entropy -= probability * (Math.log(probability) / Math.log(2));
	            }
	        }
	        return entropy;
	    }
		return null; 
	}

 		   	  	  		
	public void draw(Color lineColor) {
	  //same process as in ToneCurve class
	    gc.clearRect(0, 0, grayLevels, maxHeight);
	    gc.setStroke(lineColor);
	    gc.setLineWidth(1);

	    // Find the maximum frequency to scale the bars, stores max height of bars
	    int maxFrequency = 0;
	    for (int i = 0; i < grayLevels; i++) {
	        maxFrequency = Math.max(maxFrequency, histogram[i]); //updates maxFrequency if a higher frequency is found
	    }

	    // Calculate the scaling factor for the bars
	  double scaleFactor = (double) (maxHeight) / maxFrequency;

	    // Draw the histogram bars
	    double barWidth = (double) grayLevels / histogram.length;
	    for (int i = 0; i < histogram.length; i++) {
	    	if (histogram[i] != 0) {
				
			
	        double barHeight = histogram[i] * scaleFactor; //how many pixels, how often
	        double x = i * barWidth + 0.5;//equal width for each bar
	        double y = maxHeight - barHeight + 0.5;
	        gc.strokeLine(x, maxHeight - 0.5, x, y);
	    	}
	    }
	}


 		   	  	  		
}
	  	  		






