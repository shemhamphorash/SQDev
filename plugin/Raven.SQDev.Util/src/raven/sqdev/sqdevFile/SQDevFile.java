package raven.sqdev.sqdevFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import raven.sqdev.exceptions.IllegalAccessStateException;
import raven.sqdev.exceptions.SQDevCoreException;
import raven.sqdev.exceptions.SQDevFileIsInvalidException;
import raven.sqdev.exceptions.SQDevFileNoSuchAttributeException;
import raven.sqdev.util.StringUtils;

/**
 * A <code>SQDevFile</code> contains some project specific information for the
 * plugin to use.<br>
 * This kind of file has to follow a <b>strict syntax:</b><br>
 * <li>Every information has to end with a newLine</li>
 * <li>Every information must be specified in it's own line</li><br>
 * <li>An attribute has to assign it's value via <code>=</code></li><br>
 * <li>An annotation has to start with <code>@</code></li>
 * <li>Every attribute may only be used once</li>
 * <li>Every attribute has to be closed with a semicolon on the same line<(li>
 * 
 * @author Raven
 * 		
 */
public class SQDevFile extends File {
	
	private static final long serialVersionUID = 9142251132106001327L;
	
	/**
	 * Creates a new <code>SQDevFile</code> on the given path
	 * 
	 * @param path
	 *            A pathname string
	 * @throws FileNotFoundException
	 *             The constructor will check if the given path can be resolved.
	 *             If not it will throw this exception. <b>This file can't be
	 *             non-existant!</b>
	 * @throws IllegalAccessStateException
	 *             If the file is not readable or writable this exception will
	 *             be thrown
	 */
	public SQDevFile(String path) throws FileNotFoundException, IllegalAccessStateException {
		super(path);
		
		if (!this.exists()) {
			throw new FileNotFoundException("The file " + getPath() + " does not exist!");
		}
		
		if (!this.canRead()) {
			throw new IllegalAccessStateException(
					"The SQDevFile " + getPath() + " is not readable!");
		}
		
		if (!this.canWrite()) {
			throw new IllegalAccessStateException(
					"The SQDevFile " + getPath() + " is not writable!");
		}
	}
	
	/**
	 * Creates a new <code>SQDevFile</code> on the given path
	 * 
	 * @param path
	 *            An absolute, hierarchical URI with a scheme equal to "file", a
	 *            non-empty path component, and undefined authority, query, and
	 *            fragment components
	 * @throws FileNotFoundException
	 *             The constructor will check if the given path can be resolved.
	 *             If not it will throw this exception. <b>This file can't be
	 *             non-existant!</b>
	 * @throws IllegalAccessStateException
	 *             If the file is not readable or writable this exception will
	 *             be thrown
	 */
	public SQDevFile(URI path) throws FileNotFoundException, IllegalAccessStateException {
		super(path);
		
		if (!this.exists()) {
			throw new FileNotFoundException("The file " + getPath() + " does not exist!");
		}
		
		if (!this.canRead()) {
			throw new IllegalAccessStateException(
					"The SQDevFile " + getPath() + " is not readable!");
		}
		
		if (!this.canWrite()) {
			throw new IllegalAccessStateException(
					"The SQDevFile " + getPath() + " is not writable!");
		}
	}
	
	/**
	 * Converts the given file to a <code>SQDevFile</code>
	 * 
	 * @param file
	 *            The file to be converted. <b>Must exist!</b>
	 * @throws FileNotFoundException
	 *             The constructor will check if the given path can be resolved.
	 *             If not it will throw this exception. <b>This file can't be
	 *             non-existant!</b>
	 * @throws IllegalAccessStateException
	 *             If the file is not readable or writable this exception will
	 *             be thrown
	 */
	public SQDevFile(File file) throws FileNotFoundException, IllegalAccessStateException {
		this(file.getPath());
	}
	
	/**
	 * Returns a reader that has this file as an input.
	 * 
	 * @return The created reader
	 * @throws FileNotFoundException
	 */
	public BufferedReader openReader() throws FileNotFoundException {
		return new BufferedReader(new FileReader(this));
	}
	
	/**
	 * Returns a writer that operates on this file
	 * 
	 * @return The created writer
	 * @throws IOException
	 */
	public BufferedWriter openWriter() throws IOException {
		return new BufferedWriter(new FileWriter(this));
	}
	
	/**
	 * Gets the attributes that can be specified in this file
	 * 
	 * @return
	 */
	public ESQDevFileAttribute[] getAttributes() {
		return ESQDevFileAttribute.values();
	}
	
	/**
	 * Gets the attributes that can be specified in this file as a String array
	 * 
	 * @return
	 */
	public String[] getAttributesAsString() {
		ESQDevFileAttribute[] attributes = getAttributes();
		
		String[] strAttributes = new String[attributes.length];
		
		for (int i = 0; i < attributes.length; i++) {
			strAttributes[i] = attributes[i].toString();
		}
		
		return strAttributes;
	}
	
	/**
	 * Gets the annotations that can be specified in this file
	 * 
	 * @return
	 */
	public ESQDevFileAnnotation[] getAnnotations() {
		return ESQDevFileAnnotation.values();
	}
	
	/**
	 * Gets the annotations that can be specified in this file as a String array
	 * 
	 * @return
	 */
	public String[] getAnnotationsAsString() {
		ESQDevFileAnnotation[] annotations = getAnnotations();
		
		String[] strAnnotations = new String[annotations.length];
		
		for (int i = 0; i < annotations.length; i++) {
			strAnnotations[i] = annotations[i].toString();
		}
		
		return strAnnotations;
	}
	
	/**
	 * Check if this file is valid
	 * 
	 * @return
	 */
	public boolean isValid() {
		String completeFile = "";
		try {
			String currentLine;
			while ((currentLine = openReader().readLine()) != null) {
				completeFile += currentLine;
				
				if (currentLine.equals("\n") || currentLine.trim().startsWith("//")) {
					// skip blank lines and comments
					continue;
				} else {
					// check if non-empty lines contains valid information
					if (ESQDevFileAttribute.isAttribute(currentLine)) {
						if (!currentLine.contains("=") || !currentLine.contains(";")) {
							// An attribute has to contain a "=" as well as a
							// ";"
							return false;
						}
					} else {
						if (ESQDevFileAnnotation.isAnnotation(currentLine)) {
							if (!currentLine.contains(" ")) {
								// an annotation always has to specify an
								// argument
								return false;
							} else {
								String argument = currentLine.substring(currentLine.indexOf(" "))
										.trim();
										
								if (!argument.startsWith("\"") || !argument.endsWith("\"")) {
									// The argument has to be encapsulated in
									// quotation marks
									return false;
								}
							}
						} else {
							// if it's neither an attribute nor an annotation
							return false;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		for (String currentAttribute : getAttributesAsString()) {
			if (StringUtils.countMatches(completeFile, currentAttribute) > 1) {
				// Each attribute may only be specified once
				return false;
			}
		}
		
		if (!completeFile.endsWith("\n")) {
			// file has to end with a newLine
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the complete file content as a String
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getContent() throws FileNotFoundException, IOException {
		String completeFile = "";
		
		String currentLine;
		while ((currentLine = openReader().readLine()) != null) {
			completeFile += currentLine;
		}
		
		return completeFile;
	}
	
	/**
	 * Gets the the value of the given attribute
	 * 
	 * @param attribute
	 *            The attribute that should be obtained
	 * @return The given attribute with the set value
	 * @throws SQDevFileIsInvalidException
	 *             This exception is thrown when the this file is invalid
	 */
	public ESQDevFileAttribute getAttribute(ESQDevFileAttribute attribute)
			throws SQDevFileIsInvalidException {
		if (!isValid()) {
			throw new SQDevFileIsInvalidException();
		}
		
		String content;
		try {
			content = getContent();
		} catch (IOException e) {
			throw new SQDevCoreException(e);
		}
		
		if (!content.contains(attribute.toString())) {
			if (!attribute.hasDefault()) {
				throw new SQDevFileNoSuchAttributeException(
						"The attribute \"" + attribute.toString() + "\" is not specified in "
								+ getPath() + " nor does it have a default value");
			} else {
				// if the attribute is not specified it will have it's default
				// value
				attribute.setValue(attribute.getDefault());
				return attribute;
			}
		}
		
		boolean found = false;
		String detectedLine = "";
		
		while (!found) {
			detectedLine = content.substring(content.indexOf(attribute.toString()));
			detectedLine = detectedLine.substring(0, detectedLine.indexOf("\n"));
			
			
			String detectedAttribute = detectedLine.substring(0, detectedLine.indexOf("=")).trim();
			
			found = detectedAttribute.equals(attribute.toString());
			
			if (!found) {
				// trim content with each iteration to prevent an endless loop
				content = content.substring(content.indexOf(detectedLine));
			}
		}
		
		// get the value and store it in attribute
		String value = detectedLine
				.substring(detectedLine.indexOf("=") + 1, detectedLine.indexOf(";")).trim();
				
		attribute.setValue(value);
		
		return attribute;
	}
	
}
