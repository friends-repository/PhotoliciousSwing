package com.kc.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.kc.utils.PhotoliciousUtils;

public class Convert extends SwingWorker<Integer, String> {

	String inputFolderPath;
	String waterMarkImagePath;
	String outputFolderPath;
	ImageOverlay imageOverlay;
	ResizePic resizePic;
	List<String> currentFolderList;
	List<String> convertedFilesList;
	
	public Convert(String inputFolderPath, String waterMarkImagePath, String outputFolderPath)
	{
		this.inputFolderPath = inputFolderPath;
		this.waterMarkImagePath = waterMarkImagePath;
		this.outputFolderPath = outputFolderPath;
		imageOverlay = new ImageOverlay();
		resizePic = new ResizePic();
		currentFolderList = new ArrayList<String>();
		convertedFilesList = new ArrayList<String>();
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		try
		{
			while(!Thread.currentThread().interrupted())
			{
				File inputFolder = new File(this.inputFolderPath);
	      	  	File watermark = new File(this.waterMarkImagePath);
	      	  	BufferedImage watermarkBuffer = imageOverlay.readImage(watermark.getPath());
	            File[] listOfFiles = PhotoliciousUtils.filterImagesFromFolder(inputFolder.listFiles());
	            if(listOfFiles == null)
	            	return null;  // Added condition check
	            //System.out.println("Start Time"+ new Date());
	         
	            if(listOfFiles.length != convertedFilesList.size())
	        	{
	            	for (File file : listOfFiles) {
	            		if(!convertedFilesList.contains(file.getName()))
	            		{
			          	  //System.out.println(file.getPath());
			          	  BufferedImage image = imageOverlay.readImage(file.getPath());
			          	  BufferedImage resizedImage = resizePic.scaleImage(image, watermarkBuffer.getWidth(), watermarkBuffer.getHeight());
			          	  BufferedImage finalImage = imageOverlay.overlayImages(resizedImage, watermarkBuffer);
			          	  imageOverlay.writeImage(finalImage, outputFolderPath+"//"+file.getName(), "jpeg");
			          	  convertedFilesList.add(file.getName());
	            		}
	            	}
	            	//System.out.println("End Time"+ new Date());
	            }
	            Thread.sleep(5000);
				}
			}catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
				return null;
			}
		catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

			}
		return null;
	}

}
