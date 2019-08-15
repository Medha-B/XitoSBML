package jp.ac.keio.bio.fun.xitosbml.image;

import java.util.ArrayList;
import java.util.HashMap;

import org.scijava.vecmath.Point3d;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;

/**
 * Spatial SBML Plugin for ImageJ.
 *
 * @author Kaito Ii <ii@fun.bio.keio.ac.jp>
 * @author Akira Funahashi <funa@bio.keio.ac.jp>
 * Date Created: Feb 21, 2017
 *
 * The Class SpatialImage, which is a class for handling spatial image in XitoSBML.
 */
public class SpatialImage {
	
	/** The raw data of spatial image in 1D array. */
	private byte[] raw;
	
	/** The width of an image. */
	private int width;
	
	/** The height of an image. */
	private int height;
	
	/** The depth of an image. */
	private int depth;
	
	/** The img as an ImageJ object. */
	private ImagePlus img;
	
	/** The hash map of domain types. */
	private HashMap<String, Integer> hashDomainTypes;
	
	/** The hash map of sampled value. */
	private HashMap<String, Integer> hashSampledValue;
	
	/** The hash map of domain num. */
	private HashMap<String,Integer> hashDomainNum;
	
	/** The adjacents list. */
	private ArrayList<ArrayList<String>> adjacentsList;
	
	/** The title of the image. */
	public String title;
	
	/** The unit of a CoordinateComponent */
	private String unit;
	
	/** The hash domain InteriorPoint. */
	private HashMap<String,Point3d> hashDomInteriorPt;
	
	/** The delta. */
	private Point3d delta = new Point3d();
	
	/**
	 * Instantiates a new spatial image given image object.
     * SpatialImage object is generated with given image, sampled value
	 * (pixel value of a SampledVolume) and domain types.
	 * Unit of spatial image will be adjusted by reading file information from
	 * given image.
	 *
	 * @param hashSampledValue the hash map of sampled value, that is a pixel value of a SampledVolume
	 * @param hashDomainTypes the hash map of domain types
	 * @param img the image as an ImageJ object
	 */
	public SpatialImage(HashMap<String, Integer> hashSampledValue, HashMap<String, Integer> hashDomainTypes, ImagePlus img){
		this.setWidth(img.getWidth());
		this.setHeight(img.getHeight());
		this.setDepth(img.getImageStackSize());
		this.img = img;
		this.setHashSampledValue(hashSampledValue);
		this.setHashDomainTypes(hashDomainTypes);
		delta.x = img.getFileInfo().pixelWidth;
		delta.y = img.getFileInfo().pixelHeight;
		delta.z = img.getFileInfo().pixelDepth;
		setUnit();
		if(img.getFileInfo().unit != null) 
			adjustUnit(img.getFileInfo().unit);
		setRawImage();
	}	
	
	/**
	 * Instantiates a new spatial image given image object.
	 * SpatialImage object is generated with given image and sampled value
	 * (pixel value of a SampledVolume).
	 * Unit of spatial image will be adjusted by reading file information from
	 * given image.
	 *
	 * @param hashSampledValue the hash map of sampled value, that is a pixel value of a SampledVolume
	 * @param img the image as an ImageJ object
	 */
	public SpatialImage(HashMap<String, Integer> hashSampledValue, ImagePlus img){	//only for model editing
		this.setWidth(img.getWidth());
		this.setHeight(img.getHeight());
		this.setDepth(img.getImageStackSize());
		this.img = img;
		this.setHashSampledValue(hashSampledValue);
		delta.x = img.getFileInfo().pixelWidth;
		delta.y = img.getFileInfo().pixelHeight;
		delta.z = img.getFileInfo().pixelDepth;
		setUnit();
		if(img.getFileInfo().unit != null) 
			adjustUnit(img.getFileInfo().unit);
		setRawImage();
	}	
	
	/**
	 * Converts image object to an 1D array (raw data).
     * Z-stack images (3D image) will also be converted to an 1D array.
	 */
	private void setRawImage(){
		byte[] slice = null;   
		raw = new byte[width * height * depth];
    	ImageStack stack = img.getStack(); 	
    	for(int i = 1 ; i <= depth ; i++){
        	slice = (byte[]) stack.getPixels(i);
        	System.arraycopy(slice, 0, getRaw(), (i-1) * getHeight() * getWidth(), getHeight() * getWidth());
    	} 
	}
	
	/**
	 * Sets the image.
	 *
	 * @param image the new image as an ImageJ object
	 */
	public void setImage(ImagePlus image){
		this.img = image;
		setRawImage();
	}

	/**
	 * Gets the image.
	 *
	 * @return the image as an ImageJ object
	 */
	public ImagePlus getImage(){
		return img;
	}

	/**
	 * Update image.
	 *
	 * @param imStack the image stack (ImageStack) object
	 */
	public void updateImage(ImageStack imStack){
		depth = imStack.getSize();
		img.setStack(imStack);
		setRawImage();
	}

	/**
	 * Gets the width of spatial image.
	 *
	 * @return the width of spatial image
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of spatial image.
	 *
	 * @param width the new width of spatial image
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height of spatial image.
	 *
	 * @return the height of spatial image
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of spatial image.
	 *
	 * @param height the new height of spatial image
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the depth of spatial image.
	 *
	 * @return the depth of spatial image
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Sets the depth of spatial image.
	 *
	 * @param depth the new depth of spatial image
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Gets the hash sampled value of spatial image.
	 *
	 * @return the hash sampled value of spatial image
	 */
	public HashMap<String, Integer> getHashSampledValue() {
		return hashSampledValue;
	}

	/**
	 * Sets the hash sampled value of spatial image.
	 *
	 * @param hashSampledValue the hash sampled value of spatial image
	 */
	public void setHashSampledValue(HashMap<String, Integer> hashSampledValue) {
		this.hashSampledValue = hashSampledValue;
	}

	/**
	 * Gets the raw data of spatial image in 1D array.
	 *
	 * @return the raw of spatial image
	 */
	public byte[] getRaw() {
		return raw;
	}

	/**
	 * Sets the raw data of spatial image in 1D array.
	 *
	 * @param raw the new raw data of spatial image in 1D array
	 */
	public void setRaw(byte[] raw) {
		this.raw = raw;
	}

	/**
	 * Gets the hash map of domain types of spatial image.
	 *
	 * @return the hash map of domain types of spatial image
	 */
	public HashMap<String, Integer> getHashDomainTypes() {
		return hashDomainTypes;
	}

	/**
	 * Sets the hash map of domain types of spatial image.
	 *
	 * @param hashDomainTypes the hash map of domain types of spatial image
	 */
	public void setHashDomainTypes(HashMap<String, Integer> hashDomainTypes) {
		this.hashDomainTypes =  hashDomainTypes;
	}
	
	/**
	 * Creates the hash map of domain types of spatial image.
	 * The value of domain type (spatialDimensions) will be set depending on
	 * the value of spatial ID reference (SpIdRef).
	 */
	public void createHashDomainTypes() {
		hashDomainTypes = new HashMap<String, Integer>();
		for (String s : hashSampledValue.keySet()) {
			if (s.contains("membrane") && depth > 1)
				hashDomainTypes.put(s, 2);
			else if(s.contains("membrane"))
				hashDomainTypes.put(s, 1);
			else if(depth > 1)
				hashDomainTypes.put(s, 3);
			else
				hashDomainTypes.put(s, 2);
		}
	}
	
	/**
	 * Gets the hash map of domain num of spatial image.
	 *
	 * @return the hash map of domain num of spatial image
	 */
	public HashMap<String, Integer> getHashDomainNum() {
		return hashDomainNum;
	}
	
	/**
	 * Sets the hash map of domain num of spatial image.
	 *
	 * @param hashDomainNum the hash map of domain num of spatial image
	 */
	public void setHashDomainNum(HashMap<String,Integer> hashDomainNum) {
		this.hashDomainNum = hashDomainNum;
	}

	/**
	 * Gets the adjacents list of spatial image.
	 *
	 * @return the adjacents list of spatial image
	 */
	public ArrayList<ArrayList<String>> getAdjacentsList() {
		return adjacentsList;
	}

	/**
	 * Sets the adjacents list of spatial image.
	 *
	 * @param adjacentsList the new adjacents list of spatial image
	 */
	public void setAdjacentsList(ArrayList<ArrayList<String>> adjacentsList) {
		this.adjacentsList = adjacentsList;
	}

	/**
	 * Save image as TIFF file.
	 *
	 * @param path the path to the directory
	 * @param name the name of TIFF file
	 */
	public void saveAsImage(String path, String name){
		FileSaver fs = new FileSaver(img);
		if(name == null) return;
		if(depth > 1)
			fs.saveAsTiffStack(path + "/" + name + ".tiff");
		else
			fs.saveAsTiff(path + "/" + name + ".tiff");
	}

	/**
	 * Gets the unit of a CoordinateComponent.
	 *
	 * @return the unit of a CoordinateComponent
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * Sets the unit of a CoordinateComponent.
	 */
	public void setUnit() {
		this.unit = "um";
	}

	/**
	 * Adjust unit of a CoordinateComponent.
	 *
	 * @param unit the unit of a CoodinateComponent
	 */
	//adjust img info to um
	private void adjustUnit(String unit){
		if(unit.equals("nm")){
			delta.x /= 1000;
			delta.y /= 1000;
			delta.z /= 1000;
		}
	}
	
	/**
	 * Gets the delta of spatial image.
	 *
	 * @return the delta of spatial image
	 */
	public Point3d getDelta(){
		return delta;
	}
	
	/**
	 * Sets the delta of spatial image.
	 *
	 * @param delta the new delta of spatial image
	 */
	public void setDelta(Point3d delta) {
		this.delta = delta;
	}
	
	/**
	 * Gets the hash map of domain InteriorPoint of spatial image.
	 *
	 * @return the hash map of domain InteriorPoint of spatial image
	 */
	public HashMap<String, org.scijava.vecmath.Point3d> getHashDomInteriorPt() {
		return hashDomInteriorPt;
	}

	/**
	 * Sets the hash map of domain InteriorPoint of spatial image.
	 *
	 * @param hashDomInteriorPt the hash dom InteriorPoint of spatial image
	 */
	public void setHashDomInteriorpt(HashMap<String,Point3d> hashDomInteriorPt) {
		this.hashDomInteriorPt = hashDomInteriorPt;
	}

}
