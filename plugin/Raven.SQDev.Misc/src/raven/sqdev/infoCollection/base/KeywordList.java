package raven.sqdev.infoCollection.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import raven.sqdev.interfaces.ISaveable;

/**
 * A list containing multiple <code>Keyword</code>s
 * 
 * @author Raven
 * 		
 */
public class KeywordList implements ISaveable {
	
	/**
	 * The sequence indicating the start of the keywordList in the saveable
	 * String format of this class
	 */
	public static final String LIST_START_SAVESEQUENCE = "KeywordList:";
	/**
	 * The sequence indicating the end of the keywordList in the saveable String
	 * format of this class
	 */
	public static final String LIST_END_SAVESEQUENCE = "//KeywordListEnd//";
	/**
	 * The sequence seperating the single keywords in the saveable String format
	 * of this class
	 */
	public static final String LIST_SEPERATOR_SAVESEQUENCE = "%NextListItem%";
	
	/**
	 * The list of keywords where every starting letter has it's own list.
	 * Therefor <code>get('b'-('a'+1))</code> will get the list for the starting
	 * letter b
	 */
	private List<List<Keyword>> keywords;
	
	
	/**
	 * Creates an instance of this <code>KeywordList</code>
	 */
	public KeywordList() {
		keywords = new ArrayList<List<Keyword>>(27);
		
		// initialize a list for every letter
		for (int i = ('a' - 1); i <= 'z'; i++) {
			keywords.add(new ArrayList<Keyword>());
		}
	}
	
	/**
	 * Recreates an instance of this <code>KeywordList</code> from the given
	 * saveFormat. If saveFormat is not considered a valid saveFormat the list
	 * will be initialized as if no parameter was given
	 * 
	 * @param saveFormat
	 *            The saveFormat which should be used to initialize this list
	 *            from
	 */
	public KeywordList(String saveFormat) {
		this();
		
		if (isSaveFormat(saveFormat)) {
			recreateFrom(saveFormat);
		}
	}
	
	/**
	 * Adds a keyword to this list
	 * 
	 * @param keyword
	 *            Th keyword to add
	 */
	public void addKeyword(Keyword keyword) {
		Assert.isNotNull(keyword);
		
		int listIndex;
		
		if (Character.isLetter(keyword.getKeyword().charAt(0))) {
			listIndex = Character.toLowerCase(keyword.getKeyword().charAt(0)) - ('a' - 1);
		} else {
			listIndex = 0;
		}
		
		keywords.get(listIndex).add(keyword);
		
		// sort the list the keywords has been added to
		Collections.sort(keywords.get(listIndex));
	}
	
	/**
	 * removes a keyword from this list
	 * 
	 * @param keyword
	 *            The keyword to remove
	 */
	public void removeKeyword(Keyword keyword) {
		Assert.isNotNull(keyword);
		
		if (this.contains(keyword)) {
			// only add a keyword once
			return;
		}
		
		keywords.get(Character.toLowerCase(keyword.getKeyword().charAt(0)) - ('a' - 1))
				.remove(keyword);
	}
	
	/**
	 * Checks if the given Keyword is contained in this list
	 * 
	 * @param keyword
	 *            The keyword to search for
	 */
	public boolean contains(Keyword keyword) {
		return keywords.get(Character.toLowerCase(keyword.getKeyword().charAt(0)) - ('a' - 1))
				.contains(keyword);
	}
	
	/**
	 * Gets all of the keywords stored in this list as one single list
	 * 
	 * @return The list of keywords contained in this List in alphabetical order
	 */
	public ArrayList<Keyword> getKeywords() {
		ArrayList<Keyword> allKeywords = new ArrayList<Keyword>();
		
		for (List<Keyword> currentList : keywords) {
			allKeywords.addAll(currentList);
		}
		
		return allKeywords;
	}
	
	/**
	 * Gets the keywordList where each starting letter is stored in it's own
	 * subList.<br>
	 * 'a' is index 1
	 */
	public List<List<Keyword>> getKeywordsSorted() {
		return keywords;
	}
	
	/**
	 * Gets the keyword with the respective String representation
	 * 
	 * @param keyword
	 *            The String representation of the desired keyword
	 * @return The desired <code>Keyword</code> or <code>null</code> if none
	 *         could be found
	 */
	public Keyword getKeyword(String keyword) {
		List<Keyword> list = keywords.get(Character.toLowerCase(keyword.charAt(0)) - ('a' - 1));
		
		for (Keyword currentKeyword : list) {
			if (currentKeyword.getKeyword().equals(keyword)) {
				return currentKeyword;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks whether this list contains a keyword with the given String
	 * representation
	 * 
	 * @param keyword
	 *            The String representation of the desired keyword
	 */
	public boolean contains(String keyword) {
		return getKeyword(keyword) != null;
	}
	
	@Override
	public String getSaveableFormat() {
		String saveableFormat = LIST_START_SAVESEQUENCE + "\n\n";
		
		for (Keyword currentKeyword : getKeywords()) {
			saveableFormat += "\n" + currentKeyword.getSaveableFormat();
			
			saveableFormat += "\n\n" + LIST_SEPERATOR_SAVESEQUENCE + "\n";
		}
		
		if (getKeywords().size() > 0) {
			// remove last seperator
			saveableFormat = saveableFormat.substring(0,
					saveableFormat.length() - (LIST_SEPERATOR_SAVESEQUENCE.length() + 1));
		}
		
		saveableFormat += "\n\n" + LIST_END_SAVESEQUENCE;
		
		return saveableFormat.replace("\n", "\r\n");
	}
	
	@Override
	public boolean recreateFrom(String savedFormat) {
		savedFormat = savedFormat.replace("\r\n", "\n");
		
		String listContent = savedFormat.substring(
				savedFormat.indexOf(LIST_START_SAVESEQUENCE) + LIST_START_SAVESEQUENCE.length(),
				savedFormat.indexOf(LIST_END_SAVESEQUENCE)).trim();
				
		for (String currentKeywordContent : listContent.split(LIST_SEPERATOR_SAVESEQUENCE)) {
			currentKeywordContent = currentKeywordContent.trim();
			
			Keyword currentKeyword;
			
			if (currentKeywordContent.contains(SQFCommand.SYNTAX_START_SAVESEQUENCE)) {
				// if the info corresponds to a SQF command
				currentKeyword = new SQFCommand();
			} else {
				if (currentKeywordContent.contains(SQFElement.WIKI_START_SAVESEQUENCE)) {
					// if the info corresponds to a SQFElement
					currentKeyword = new SQFElement();
				} else {
					// else it's just a normal Keyword
					currentKeyword = new Keyword();
				}
			}
			
			if (!currentKeyword.recreateFrom(currentKeywordContent)) {
				return false;
			} else {
				addKeyword(currentKeyword);
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isSaveFormat(String format) {
		if (!format.contains(LIST_START_SAVESEQUENCE) || !format.contains(LIST_END_SAVESEQUENCE)) {
			return false;
		}
		
		int startPos = format.indexOf(LIST_START_SAVESEQUENCE);
		int endPos = format.indexOf(LIST_END_SAVESEQUENCE);
		
		if (endPos < startPos) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the KeywordList for the given starting character
	 * 
	 * @param c
	 *            The starting character the returned list should correspond to
	 * @return The respective list
	 */
	public List<Keyword> getListFor(char c) {
		c = Character.toLowerCase(c);
		
		if (Character.isLetter(c)) {
			return keywords.get(c - ('a' - 1));
		} else {
			return keywords.get(0);
		}
	}
	
}