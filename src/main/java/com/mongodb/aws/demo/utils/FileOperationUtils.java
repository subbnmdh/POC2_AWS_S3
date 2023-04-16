package com.mongodb.aws.demo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import javax.swing.SpinnerDateModel;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

public class FileOperationUtils {

	
	public static String UploadImage(MultipartFile file,String bucketName,AmazonS3 s3Client,long rollno)
	{
		if(validateFile(file)) {
		try
		{
			
			String filename=rollno+"_"+file.getOriginalFilename().trim();
			//System.out.println("Filename: "+filename+"\n\tFile Content type: "+file.getContentType());
			File f=new File(filename);
			//System.out.println("\n\tAbsolute FilePath: "+f.getAbsolutePath());
			FileOutputStream fos=new FileOutputStream(f);
			fos.write(file.getBytes());
			String[] splitfilenames = file.getOriginalFilename().split(".");
			for(String s: splitfilenames)
			System.out.println("\n\t"+s);
			String fileKey=rollno+".jpg";
			s3Client.putObject(bucketName, fileKey, f);
			URL url = s3Client.getUrl(bucketName, fileKey);
			f.delete();
			fos.close();
			return ""+url;
		}
		catch(Exception ex) {System.out.println(ex.getMessage()); return null;}
		}
		else
			return "Failed to Validate the file ";
	}
	
	public static String deleteImage(AmazonS3 s3Client,String bucketName,String filename)
	{
		try
		{
			s3Client.deleteObject(bucketName, filename);
			return "Image File "+filename+" Deleted Successfully";
		}
		catch(Exception ex)
		{
			return ex.getMessage();
		}
	}
	private static boolean validateFile(MultipartFile file)
	{
		if(file.isEmpty())
			return false;
		if(!file.getContentType().contains("image/jpeg"))
			return false;
		return true;
	}
}
