// BV Ue4 SS2023 Vorgabe
//
// Copyright (C) 2023 by Klaus Jung
// All rights reserved.
// Date: 2023-03-23
 		   	  	  		

package bv_ss23;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import bv_ss23.ImageAnalysisAppController.Visualization;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RasterImage {
 		   	  	  		
	private static final int gray  = 0xffa0a0a0;

	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels
	
	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this(width, height, gray);
	}

	public RasterImage(int width, int height, int argbColor) {
		// creates an empty RasterImage of given size and color
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, argbColor);
	}
	
	public RasterImage(RasterImage image) {
		// copy constructor
		this.width = image.width;
		this.height = image.height;
		argb = image.argb.clone();
	}
 		   	  	  		
	public RasterImage(File file) {
		// creates a RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}
	
	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}
	
	public Image getImage() {
		// returns a JavaFX image
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			return wr;
		}
		return null;
	}
 		   	  	  		
	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		Image image = getImage();
		if(image != null) {
			imageView.setImage(image);
		}
	}
	
	
	// image point operations to be added here

	public void convertToGray() {
		
		for (int i = 0; i<argb.length; i++) {
			int pixel = argb[i];
			int red =(pixel>>16)&0xff;
			int green=(pixel >>8)&0xff;
			int blue= pixel& 0xff;
			int gray= (red+green+blue)/3;
			argb[i]=(0xff <<24)| (gray <<16)|(gray << 8)|gray;
		}
			
		}
		// TODO: convert the image to grayscale


 		   	  	//regionSize is Kernel, benutze Klasse Histogram, erzeuge Histogram istance, set region size, get variance.  		
/*	public RasterImage getOverlayImage(int regionSize, Visualization visualization, double threshold) {
		
		RasterImage overlayImage = new RasterImage(width, height, regionSize);
		Histogram histo = new Histogram();
		histo.setImageRegion(null, regionSize, regionSize, regionSize, regionSize);
		double variance = histo.getVariance();
	//get entropy or get variance
		//enumeration abfragen
		
		// Will be used in Exercise 5. Nothing to do in Exercise 4.
		
		// Create an overlay image that contains half transparent green pixels where a
		// statistical property locally exceeds the given threshold. 
		// Use a sliding window of size regionSize x regionSize.
		// Use "switch(visualization)" to determine, what statistical property should be used
		
		return null;
	}
*/
	public RasterImage getOverlayImage(int regionSize, Visualization visualization, double threshold) {
	    RasterImage overlayImage = new RasterImage(width, height, 0); //alpha value of 0 for transparency

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            int regionStartX = Math.max(0, x - regionSize / 2);
	            int regionStartY = Math.max(0, y - regionSize / 2);
	            int regionEndX = Math.min(width - 1, x + regionSize / 2);
	            int regionEndY = Math.min(height - 1, y + regionSize / 2);//staring and ending coordinates of the region around the current pixel based on the regionSize
	            int regionWidth = regionEndX - regionStartX + 1; //width and height of the region
	            int regionHeight = regionEndY - regionStartY + 1;

	            Histogram histo = new Histogram();
	            histo.setImageRegion(this, regionStartX, regionStartY, regionWidth, regionHeight);
	            double propertyValue = 0;

	            switch (visualization) {
	                case ENTROPY:
	                    propertyValue = histo.getEntropy();
	                    break;
	                case VARIANCE:
	                    propertyValue = histo.getVariance();
	                    break;
	                
	                    
	            }

	            if (propertyValue > threshold) {
	                // Set the pixel to semi-transparent green color
	                int green = (int) (155*0.5); // semi-transparent value
	                int argb = (green << 24) | (0 << 16) | (green << 8) | 0;
	                overlayImage.argb[y * width + x] = argb;
	            }
	        }
	    }

	    return overlayImage;
	}

	public int getGray(int index) {
		int pixel = argb[index];
		return (pixel >> 16)&0xFF;
	
	
	}
	
	}
 		   	  	  		

 		   	  	  		





