package com.springboot.test.util;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.media.jai.JAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.RenderedOp;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.xmp.impl.Base64;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecodeParam;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class TiffUtil {
	
	public static int DISPLAY_WIDTH = 640;
	
	public static final String JAI_ENCODE_ACTION = "encode";
	
	public static final String JAI_ENCODE_FORMAT_JPEG = "JPEG";
	
	private static final Logger logger = LoggerFactory.getLogger(TiffUtil.class);
	
	public void saveTifFile(InputStream inputStream) {
	     ImageDecodeParam imageDecodeParam = new TIFFDecodeParam();
	     ImageDecoder imageDecoder = ImageCodec.createImageDecoder("tiff", inputStream, imageDecodeParam);
	     int size = 0;
	     try {
	         size = imageDecoder.getNumPages();
	         long date = new Date().getTime();
	         String filePath = "C://Users/Administrator/Desktop/developer file" + "/tifFile/";
	         File file = new File(filePath);
	         if (!file.exists()) {
	             file.mkdirs();
	         }
	         for (int i = 0; i < size; i++) {
	             RenderedImage renderedImage = imageDecoder.decodeAsRenderedImage(i);
	             ParameterBlock args = new ParameterBlock();
	             String path = filePath + "/" + date + "--" + (i + 1) + ".TIFF";
	             File file2 = new File(path);
	             TIFFEncodeParam pngEncodeParam = new TIFFEncodeParam();
	             args.addSource(renderedImage);
	             args.add(file2.toString());
	             args.add("TIFF");
	             args.add(pngEncodeParam);
	             RenderedOp r = JAI.create("filestore", args);
	             r.dispose();
	         }
	     } catch (IOException e) {
	         logger.error(e.getMessage());
	     }
	 }
	
	public byte[] resizeImageAsJPG(byte[] pImageData, int pMaxWidth) throws IOException {
	    InputStream imageInputStream = new ByteArrayInputStream(pImageData);
	    // read in the original image from an input stream
	    SeekableStream seekableImageStream = SeekableStream.wrapInputStream(imageInputStream, true);
	    RenderedOp originalImage = JAI.create("stream", seekableImageStream);
//	    ((OpImage) originalImage.getRendering()).setTileCache(null);
	    int origImageWidth = originalImage.getWidth();
	    // now resize the image
	    double scale = 1.0;
	    if (pMaxWidth > 0 && origImageWidth > pMaxWidth) {
	        scale = (double) pMaxWidth / originalImage.getWidth();
	    }
	    ParameterBlock paramBlock = new ParameterBlock();
	    paramBlock.addSource(originalImage); // The source image
	    paramBlock.add(scale); // The xScale
	    paramBlock.add(scale); // The yScale
	    paramBlock.add(0.0); // The x translation
	    paramBlock.add(0.0); // The y translation

	    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING,
	            RenderingHints.VALUE_RENDER_QUALITY);

	    RenderedOp resizedImage = JAI.create("stream", paramBlock, qualityHints);

	    // lastly, write the newly-resized image to an output stream, in a specific encoding
	    ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
	    JAI.create(JAI_ENCODE_ACTION, resizedImage, encoderOutputStream, JAI_ENCODE_FORMAT_JPEG, null);
	    // Export to Byte Array
	    byte[] resizedImageByteArray = encoderOutputStream.toByteArray();
	    return resizedImageByteArray;
	}
	
	public static void main(String[] args) throws IOException {
		File input = new File("C://Users/Administrator/Desktop/test-sample.tif");
		InputStream inputStream = new FileInputStream(input);
		String output = IOUtils.toString(inputStream);
		String outputBase64 = Base64.encode(output);
		System.out.println(outputBase64);
		TiffUtil util = new  TiffUtil();
		util.saveTifFile(inputStream);
	}
	
    public static boolean doTiff2JPEG (String filename, String imageDir) {
	    File file = new File(imageDir + filename);
	    SeekableStream s = null;
	    TIFFDecodeParam param = null;
	    RenderedImage op = null;
	    String simplefilename = filename.substring(0,filename.lastIndexOf("."));
	    RenderedOp image = null;
	    try {
	      s = new FileSeekableStream(file);
	      ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
	      int numofpages = dec.getNumPages();
	      for (int i=0; i< numofpages; i++) {
	        op = new NullOpImage
	           (dec.decodeAsRenderedImage(i),null,null,OpImage.OP_COMPUTE_BOUND);
	        image = JAI.create("stream", op);
	        int width = op.getWidth();
	        int height = op.getHeight();
	        double conversionFactor = (double)DISPLAY_WIDTH / (double)width;
	        int thumbHeight = (int)((double)height * conversionFactor);
	        int thumbWidth = (int)((double)width * conversionFactor);
	        Dimension dim = new Dimension(thumbHeight, thumbWidth);        
	        JAI.setDefaultRenderingSize(dim);
	        JAI.setDefaultTileSize(dim);
	        JAI.create("filestore", 
	           image, imageDir + simplefilename + "." + i + ".png", "png");
	      }
	    } catch (IOException e) {
	      logger.error(e.getMessage());
	    }
	    return true;
	}

}
