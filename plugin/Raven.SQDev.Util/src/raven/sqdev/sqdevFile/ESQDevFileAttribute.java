package raven.sqdev.sqdevFile;

/**
 * Contains all valid attributes that can be used in a {@linkplain #SDevFile}
 * 
 * @author Raven
 * 		
 */
public enum ESQDevFileAttribute {
	/**
	 * The attribute specifying whether the project should get exported/synced
	 * every time a file changes
	 */
	AUTOEXPORT {
		@Override
		public String toString() {
			return "autoExport";
		}
		
		@Override
		public String getDefault() {
			return "false";
		}
		
		@Override
		public String getDescription() {
			return "The attribute specifying whether the project "
					+ "should get exported/synced every time a file changes";
		}
	},
	/**
	 * The attribute defining where the project should be exported to
	 */
	EXPORTDIRECTORY {
		@Override
		public String toString() {
			return "exportDirectory";
		}
		
		@Override
		public String getDefault() {
			// export location has to specified
			return null;
		}
		
		@Override
		public String getDescription() {
			return "The attribute defining where the project should be exported to";
		}
	},
	/**
	 * The attribute defining the terrain the mission should play on
	 */
	TERRAIN {
		@Override
		public String toString() {
			return "terrain";
		}
		
		@Override
		public String getDefault() {
			// a map has to be specified
			return null;
		}
		
		@Override
		public String getDescription() {
			return "The attribute defining the terrain the mission should play on";
		}
		
	},
	
	/**
	 * The attribute defining to which profile this mission/project should be
	 * associated to
	 */
	PROFILE {
		@Override
		public String toString() {
			return "profile";
		}
		
		@Override
		public String getDefault() {
			// if the profile is not specified then it's unclear where this
			// project should get exported to
			return null;
		}
		
		@Override
		public String getDescription() {
			return "The attribute defining to which profile this "
					+ "mission/project should be associated to";
		}
	};
	
	/**
	 * The value of this attribute
	 */
	protected String value;
	
	/**
	 * Gets the current value of this attribute
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value of this object
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the default value for this attribute
	 * 
	 * @return The default value or <code>null</code> if there is no default
	 *         value
	 */
	public abstract String getDefault();
	
	/**
	 * Checks if this attribute has a default value
	 * 
	 * @return
	 */
	public boolean hasDefault() {
		return (getDefault() == null) ? false : true;
	}
	
	/**
	 * Checks if the given line describes a valid attribute.<br>
	 * Can also be used to check single keywords
	 * 
	 * @param inputLine
	 *            The line/word to be checked
	 * @return <code>True</code> if the given line describes a valid attribute.
	 */
	public static boolean isAttribute(String inputLine) {
		inputLine = inputLine.trim();
		
		if (inputLine.contains("\n")) {
			// an attribute can only be specified in one line
			inputLine = inputLine.substring(0, inputLine.indexOf("\n"));
		}
		
		String possibleAttribute = (inputLine.contains("="))
				? inputLine.substring(0, inputLine.indexOf("=")).trim() : inputLine.trim();
				
		if (possibleAttribute.contains(" ")) {
			// if there is still more than one word it can't be an attribute
			return false;
		}
		
		for (ESQDevFileAttribute current : ESQDevFileAttribute.values()) {
			if (current.toString().equals(possibleAttribute)) {
				// if it matches one of the specified attributes it obviously is
				// an attribute
				return true;
			}
		}
		
		// if it coudn't be found it can't be a valid attribute
		return false;
	}
	
	/**
	 * Gets the description for this attribute
	 */
	public abstract String getDescription();
}
