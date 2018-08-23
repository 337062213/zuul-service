package com.springboot.test.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class FDFS {
	
	public static void many2one(List<String> bookFilePaths, String toPath,String distFileName) {
		
	    if (bookFilePaths != null && bookFilePaths.size() > 0) {
	    	
		     File[] files = new File[bookFilePaths.size()];
		     for(int i = 0; i < bookFilePaths.size(); i++){
		         files[i] =  new File(bookFilePaths.get(i));
		     }
		     
	         if (files != null && files.length > 0) {
	     
			      try {
				       List<PlanarImage> pages = new ArrayList<>(files.length - 1);
				       FileSeekableStream[] stream = new FileSeekableStream[files.length];
				       for (int i = 0; i < files.length; i++) {
				            stream[i] = new FileSeekableStream(
				            files[i].getCanonicalPath());
				       }
				       PlanarImage firstPage = JAI.create("stream", stream[0]);
				       for (int i = 1; i < files.length; i++) {
					        PlanarImage page = JAI.create("stream", stream[i]);
					        pages.add(page);
					        }
				       TIFFEncodeParam param = new TIFFEncodeParam();
				       File f = new File(toPath);
				       if(!f.exists()){
				        f.mkdirs();
				       }
				       OutputStream os = new FileOutputStream(toPath + File.separator+ distFileName);
				       ImageEncoder enc = ImageCodec.createImageEncoder("tiff",
				         os, param);
				       param.setExtraImages(pages.iterator());
				       enc.encode(firstPage);
				       for (int i = 0; i < files.length; i++) {
					        stream[i].close();
					           if(files[i].isFile()&&files[i].exists()){
					         files[i].delete();
					        }
				       }
				       os.close();
				      } catch (IOException e) {
				       e.printStackTrace();
				      }
	      
	     }
	     
	   }
	    
   } 
	
	public static void main(String[] args) {
		String toPath = "C:\\Users\\Administrator\\Desktop";
		String distFileName = "sjghjfsg.tiff";
		List<String> listFile = new ArrayList<>();
		listFile.add("C:\\Users\\Administrator\\Desktop\\tiff/6.3.3.tif");
		listFile.add("C:\\Users\\Administrator\\Desktop\\tiff/6.3.4.tif");
		listFile.add("C:\\Users\\Administrator\\Desktop\\tiff/9.5.5.tif");
		listFile.add("C:\\Users\\Administrator\\Desktop\\tiff/9.6.5.tif");
		listFile.add("C:\\Users\\Administrator\\Desktop\\tiff/9.6.4.tif");
		FDFS.many2one(listFile, toPath, distFileName);
	}

}
