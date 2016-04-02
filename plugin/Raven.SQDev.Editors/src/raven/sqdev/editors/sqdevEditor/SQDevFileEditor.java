package raven.sqdev.editors.sqdevEditor;

import raven.sqdev.editors.BasicCodeEditor;
import raven.sqdev.editors.BasicPartitionScanner;
import raven.sqdev.infoCollection.base.Keyword;
import raven.sqdev.infoCollection.base.KeywordList;
import raven.sqdev.sqdevFile.ESQDevFileAnnotation;
import raven.sqdev.sqdevFile.ESQDevFileAttribute;

/**
 * An editor for the SQDev file type
 * 
 * @author Raven
 * 		
 */
public class SQDevFileEditor extends BasicCodeEditor {
	
	public SQDevFileEditor() {
		super();
		
		setKeywords();
		
		// remove the multi-line comment
		getBasicProvider().getPartitionScanner()
				.removeRule(BasicPartitionScanner.MULTILINE_COMMENT_RULE);
				
		getBasicConfiguration().getKeywordScanner().makeCaseSensitive(false);
	}
	
	/**
	 * Sets the keywords for this editor according to the values of
	 * <code>ESQDevFileAttributes</code> and <code>ESQDevFileAnnotation</code>
	 * 
	 * @see {@linkplain ESQDevFileAttribute}
	 * @see {@linkplain ESQDevFileAnnotation}
	 */
	protected void setKeywords() {
		KeywordList keywordList = new KeywordList();
		
		// add all attributes
		for (ESQDevFileAttribute currentAttribute : ESQDevFileAttribute.values()) {
			keywordList.addKeyword(
					new Keyword(currentAttribute.toString(), currentAttribute.getDescription()));
		}
		
		// add all annotations including the "@"
		for (ESQDevFileAnnotation currentAnnotation : ESQDevFileAnnotation.values()) {
			keywordList.addKeyword(new Keyword("@" + currentAnnotation.toString(),
					currentAnnotation.getDescription()));
		}
		
		// set the keywords
		getBasicConfiguration().getKeywordScanner().setKeywords(keywordList);
	}
	
}
