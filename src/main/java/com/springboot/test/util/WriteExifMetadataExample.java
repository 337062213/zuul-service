package com.springboot.test.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.IOUtils;

public class WriteExifMetadataExample {

	    public void removeExifMetadata(final File jpegImageFile, final File dst) throws IOException, ImageReadException, ImageWriteException {

	        try(
        		OutputStream fos = new FileOutputStream(dst);
        		OutputStream os = new BufferedOutputStream(fos);
	        	) {
	            new ExifRewriter().removeExifMetadata(jpegImageFile, os);

	        }
	    }

	    public void changeExifMetadata(final File jpegImageFile, final File dst)throws IOException, ImageReadException, ImageWriteException {

	        try(
        		OutputStream fos = new FileOutputStream(dst);
        		OutputStream os = new BufferedOutputStream(fos);
	           ) {
	            TiffOutputSet outputSet = null;
	            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
	            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	            if (null != jpegMetadata) {
	                final TiffImageMetadata exif = jpegMetadata.getExif();
	                if (null != exif) {
	                    outputSet = exif.getOutputSet();
	                }
	            }
	            if (null == outputSet) {
	                outputSet = new TiffOutputSet();
	            }else{
	                final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
	                exifDirectory.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
	                exifDirectory.add(ExifTagConstants.EXIF_TAG_APERTURE_VALUE, RationalNumber.valueOf(6));
	            }
                final double longitude = -74.0; // 74 degrees W (in Degrees East)
                final double latitude = 40 + 43 / 60.0; // 40 degrees N (in Degrees
                outputSet.setGPSInDegrees(longitude, latitude);
	            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,outputSet);
	            os.close();
	        }

	    }
	    
	    public void removeExifTag(final File jpegImageFile, final File dst )throws IOException,ImageReadException, ImageWriteException {

	        try (
        		OutputStream fos = new FileOutputStream(dst);
        		OutputStream os = new BufferedOutputStream(fos);
	        	){
	            TiffOutputSet outputSet = null;
	            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
	            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	            if (null != jpegMetadata) {
	                final TiffImageMetadata exif = jpegMetadata.getExif();
	                if (null != exif) {
	                    outputSet = exif.getOutputSet();
	                }
	            }
	            if (null == outputSet) {
	            	InputStream is = new FileInputStream(jpegImageFile);
	            	OutputStream ros = new FileOutputStream(dst);
	                IOUtils.copy(is, ros);
	            }else{
	                outputSet.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
	                final TiffOutputDirectory exifDirectory = outputSet.getExifDirectory();
	                if (null != exifDirectory) {
	                    exifDirectory.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
	                }
	            }
	            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
	            os.close();
	        }

        }

	    public void setExifGPSTag(final File jpegImageFile, final File dst)throws IOException, ImageReadException, ImageWriteException {
	        try(
        		OutputStream fos = new FileOutputStream(dst);
        		OutputStream os = new BufferedOutputStream(fos);
	        ) {
	            TiffOutputSet outputSet = null;
	            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
	            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	            if (null != jpegMetadata) {
	                final TiffImageMetadata exif = jpegMetadata.getExif();
	                if (null != exif) {
	                    outputSet = exif.getOutputSet();
	            }
	            if (null == outputSet) {
	                outputSet = new TiffOutputSet();
	            }
                final double longitude = -74.0; // 74 degrees W (in Degrees East)
                final double latitude = 40 + 43 / 60.0; // 40 degrees N (in Degrees
                outputSet.setGPSInDegrees(longitude, latitude);
	            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
	            os.close();
	        }
	      }
	  }
 }
