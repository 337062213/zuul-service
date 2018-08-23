package com.springboot.test.util;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class Splitter {
	
	private static final String className = Splitter.class.getName();
	
	private static final Logger logger = LoggerFactory.getLogger(Splitter.class);

	public static byte[] getBytesForMultiPageTIFF(List<RenderedImage> images) throws IOException {
		long startTime = System.currentTimeMillis();
		final String methodName = "getBytesForMultiPageTIFF() ";
		byte[] bytes = new byte[0];
		if (images.size() < 1)
		return bytes;
		logger.info( className + methodName + " : Getting the first rendered page/image");
		RenderedImage firstImage =  images.get(0);
		images.remove(0);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();){
			
			TIFFEncodeParam param = new TIFFEncodeParam();
			param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
			ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF", out, param);
			if (images.size() > 0) {
			    param.setExtraImages(images.iterator());
			}
			encoder.encode(firstImage);
			bytes = out.toByteArray();
	
		} catch (IOException ex) {
		    logger.error(ex.getMessage());
		}
		long endTime = System.currentTimeMillis();
		logger.info("Time taken (ms):" + (endTime-startTime));
		return bytes;
	}

}
