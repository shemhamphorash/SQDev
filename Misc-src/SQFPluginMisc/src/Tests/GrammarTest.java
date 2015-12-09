package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import OutputParser.Grammar;
import OutputParser.ParserRule;

public class GrammarTest {
	
	@Test
	public void isLeftRecursiveTest() {
		Grammar g1 = createLeftRecursiveGrammar();
		assertTrue(g1.isLeftRecursive());
		
		Grammar g2 = createLeftRecursiveGrammar(true);
		assertTrue(g2.isLeftRecursive());
		
		Grammar g3 = createStandardGrammar();
		assertFalse(g3.isLeftRecursive());
	}
	
	@Test
	public void needsLeftFactoringTest() {
		Grammar g1 = createLeftFactorGrammar();
		assertTrue(g1.needsLeftFactoring_I());
		assertFalse(g1.needsLeftFacoring_II());
		
		Grammar g2 = createLeftFactorGrammar(true);
		assertTrue(g2.needsLeftFacoring_II());
		assertFalse(g2.needsLeftFactoring_I());
		
		Grammar g3 = createStandardGrammar();
		assertFalse(g3.needsLeftFactoring());
	}
	
	@Test
	public void leftFactor_ITest() {
		Grammar g1 = createStandardGrammar();
		Grammar g2 = createStandardGrammar();
		
		g1.leftFactor_I();
		
		// Shouldn't have changed
		assertEquals(g2.toString(), g1.toString());
		
		Grammar g3 = createLeftFactorGrammar();
		assertTrue(g3.needsLeftFactoring_I());
		
		g3.leftFactor_I();
		assertFalse(g3.needsLeftFactoring_I());
	}
	
	@Test
	public void leftFactor_IITest() {
		Grammar g1 = createStandardGrammar();
		Grammar g2 = createStandardGrammar();
		
		g1.leftFactor_II();
		
		// Shouldn't have changed
		assertEquals(g2.toString(), g1.toString());
		
		Grammar g3 = createLeftFactorGrammar(true);
		
		assertTrue(g3.needsLeftFacoring_II());
		
		g3.leftFactor_II();
		
		g3.leftFactor_II();
		
		g3.leftFactor_II();
		
		g3.leftFactor_II();
		
		g3.leftFactor_II();
		
		
		g3.sort();
		
		System.out.println(g3);
		
		
		assertFalse(g3.needsLeftFacoring_II());
		
		// TODO: implement test for bracket-leftFactoring
	}
	
	@Test
	public void equalsTest() {
		/*
		 * Grammar g1 = createStandardGrammar(); Grammar g2 = createStandardGrammar();
		 * 
		 * assertTrue(g1.equals(g2));
		 */
	}
	
	@Test
	public void getCommonStartRulesSortedTest() {
		Grammar g = new Grammar();
		
		ParserRule r1 = new ParserRule("Rule1");
		r1.setAsAtomicRule(true);
		ParserRule r2 = new ParserRule("Rule2");
		r2.setAsAtomicRule(true);
		ParserRule dummy1 = new ParserRule("Dummy1");
		ParserRule dummy2 = new ParserRule("Dummy2");
		
		dummy1.addSyntax(dummy2.getName());
		
		ParserRule s1 = new ParserRule("Sub1", r1.getName());
		ParserRule s2 = new ParserRule("Sub2", s1.getName());
		
		r1.addSyntax(s1.getName());
		r1.addSyntax("Dummy1 alt");
		r1.addSyntax("Dummy1 etc");
		
		r2.addSyntax("Dummy1");
		r2.addSyntax(s1.getName());
		
		s1.addSyntax(s2.getName());
		
		s2.addSyntax("Dummy1");
		
		g.addRule(r1);
		g.addRule(r2);
		g.addRule(s1);
		g.addRule(s2);
		g.addRule(dummy1);
		g.addRule(dummy2);
		
		g.createStartRuleForecasts();
		
		ArrayList<String> solution = new ArrayList<String>();
		
		solution.add("Sub1");
		solution.add("Sub2");
		solution.add("Dummy1");
		solution.add("Dummy2");
		
		assertEquals(solution, g.getCommonStartRulesSorted(r1, r2));
	}
	
	// //////////////////////////////////////MISC/////////////////////////////////////////////////
	
	/**
	 * Creates a standard grammar with the given amount of rules
	 * 
	 * @param ruleNumber
	 *            How many rules should be added?
	 * @param sort
	 *            Should the grammar get sorted by default?
	 * @return The created grammar
	 */
	public static Grammar createStandardGrammar(int ruleNumber, boolean sort) {
		Grammar standardGrammar = new Grammar();
		
		// create rules and add them to the new grammar
		for (int i = 1; i <= ruleNumber; i++) {
			ParserRule rule = new ParserRule("test_Rule" + i);
			
			rule.addSyntax("This Test Syntax");
			rule.addSyntax("Another (Test | Syntax)");
			
			standardGrammar.addRule(rule);
		}
		
		// add empty standard rules to match the previous added syntax
		ParserRule rule1 = new ParserRule("This");
		ParserRule rule2 = new ParserRule("Test");
		ParserRule rule3 = new ParserRule("Syntax");
		ParserRule rule4 = new ParserRule("Another");
		
		rule1.setAsAtomicRule(true);
		rule2.setAsAtomicRule(true);
		rule3.setAsAtomicRule(true);
		rule4.setAsAtomicRule(true);
		
		standardGrammar.addRule(rule1);
		standardGrammar.addRule(rule2);
		standardGrammar.addRule(rule3);
		standardGrammar.addRule(rule4);
		
		if (sort) {
			standardGrammar.sort();
		}
		
		standardGrammar.createRuleForecast();
		standardGrammar.createStartRuleForecasts();
		
		return standardGrammar;
	}
	
	/**
	 * Creates a standard grammar with the given amount of rules and their dummyRules (grammar will
	 * be sorted)
	 * 
	 * @param ruleNumber
	 *            How many rules should be added?
	 * @return The created grammar
	 */
	public static Grammar createStandardGrammar(int ruleNumber) {
		return createStandardGrammar(ruleNumber, true);
	}
	
	/**
	 * Creates a standard grammar with 3 rules and their dummyRules (grammar gets sorted)
	 * 
	 * @return The created grammar
	 */
	public static Grammar createStandardGrammar() {
		return createStandardGrammar(3, true);
	}
	
	/**
	 * Creates a left recursive grammar
	 * 
	 * @param direct
	 *            Should the left recursion be direct?
	 * @return The created grammar
	 */
	public static Grammar createLeftRecursiveGrammar(boolean direct) {
		Grammar g = createStandardGrammar();
		
		// add left recursive rule
		
		if (direct) {
			ParserRule leftRule = new ParserRule("LeftRecRule");
			leftRule.setAsAtomicRule(true);
			leftRule.addLineToRuleContent(leftRule.getName());
			
			g.addRule(leftRule);
		} else {
			g.getRule(0).addSyntax("LeftRecRule");
			ParserRule leftRule = new ParserRule("LeftRecRule", g.getRule(0).getName());
			leftRule.setAsAtomicRule(true);
			leftRule.addSyntax(g.getRule(0).getName());
			
			g.addRule(leftRule);
		}
		
		g.sort();
		g.createRuleForecast();
		g.createStartRuleForecasts();
		
		return g;
	}
	
	/**
	 * Creates a indirect left recursive grammar
	 * 
	 * @return The created grammar
	 */
	public static Grammar createLeftRecursiveGrammar() {
		return createLeftRecursiveGrammar(false);
	}
	
	/**
	 * Creates a grammar that has to get left factored
	 * 
	 * @param LF_II
	 *            Should the LF_II algorithm should be needed?
	 * @param leftRecursive
	 *            Should the grammar be left recursive (Will always be LF_II)
	 * @return The created grammar
	 */
	public static Grammar createLeftFactorGrammar(boolean LF_II, boolean leftRecursive) {
		// TODO: implement bracket alts
		Grammar g;
		
		if (leftRecursive) {
			g = createLeftRecursiveGrammar();
		} else {
			g = createStandardGrammar();
			
			if (LF_II) {
				// add rules for the need of being left factored
				ParserRule rule1 = new ParserRule("Rec1_1");
				rule1.setAsAtomicRule(true);
				rule1.addSyntax("Rec1_2 Test");
				rule1.addSyntax("Syntax");
				
				ParserRule rule2 = new ParserRule("Rec1_2");
				rule2.setAsAtomicRule(true);
				rule2.addSyntax("TestStart");
				rule2.addSyntax("Dummy Test");
				
				ParserRule rule3 = new ParserRule("Rec2_1");
				rule3.setAsAtomicRule(true);
				rule3.addSyntax("Rec2_2");
				rule3.addSyntax("Syntax");
				
				ParserRule rule4 = new ParserRule("Rec2_2");
				rule4.setAsAtomicRule(true);
				rule4.addSyntax("TestStart");
				rule4.addSyntax("Syntax");
				
				ParserRule rule5 = new ParserRule("Rec3_Main");
				rule5.setAsAtomicRule(true);
				
				ParserRule rule6 = new ParserRule("Rec3_Sub", rule5.getName());
				rule6.setAsAtomicRule(true);
				
				ParserRule rule7 = new ParserRule("Rec3_1", rule6.getName());
				rule7.setAsAtomicRule(true);
				
				ParserRule rule8 = new ParserRule("Rec3_2", rule6.getName());
				rule8.setAsAtomicRule(true);
				
				ParserRule rule9 = new ParserRule("Rec4_1");
				rule9.setAsAtomicRule(true);
				
				// create empty dummy rule to match syntax "testStart"
				ParserRule dummyRule = new ParserRule("testStart", "Rec1_2");
				dummyRule.setAsAtomicRule(true);
				
				rule5.addSyntax(rule6.getName());
				rule5.addSyntax("TestStart");
				
				rule6.addSyntax(rule7.getName());
				rule6.addSyntax(rule8.getName());
				
				rule7.addSyntax("Miau");
				rule7.addSyntax("Dummy Test");
				
				rule8.addSyntax("Dummy test_Rule1");
				rule8.addSyntax("Miau2");
				
				ParserRule rule10 = new ParserRule("Rec4_2");
				rule10.setAsAtomicRule(true);
				
				ParserRule rule11 = new ParserRule("Rec5_1");
				rule11.setAsAtomicRule(true);
				
				ParserRule rule12 = new ParserRule("Rec5_2");
				rule12.setAsAtomicRule(true);
				
				ParserRule rule13 = new ParserRule("Rec5_3");
				rule13.setAsAtomicRule(true);
				
				ParserRule rule14 = new ParserRule("Rec5_4");
				rule14.setAsAtomicRule(true);
				
				rule9.addSyntax("TestStart");
				rule9.addSyntax("Syntax Dummy");
				
				rule10.addSyntax(rule9.getName() + " Miau");
				rule10.addSyntax("Miau2 Miau");
				rule10.addSyntax("Syntax");
				
				rule11.addSyntax("Miau (" + rule12.getName() + " | " + rule13.getName() + " | "
						+ rule14.getName() + ")");
				
				rule12.addSyntax("Dummy Test");
				
				rule13.addSyntax("Dummy Miau");
				
				rule14.addSyntax("Dummy");
				
				// create dummyRule to match syntax "Dummy"
				ParserRule dummy = new ParserRule("Dummy");
				dummy.setAsAtomicRule(true);
				
				// add an alt to rule syntax
				g.getRule("Syntax").addSyntax("Dummy");
				
				ParserRule miau1 = new ParserRule("Miau");
				ParserRule miau2 = new ParserRule("Miau2");
				
				g.addRule(rule1);
				g.addRule(rule2);
				g.addRule(rule3);
				g.addRule(rule4);
				g.addRule(rule5);
				g.addRule(rule6);
				g.addRule(rule7);
				g.addRule(rule8);
				g.addRule(rule9);
				g.addRule(rule10);
				g.addRule(rule11);
				g.addRule(rule12);
				g.addRule(rule13);
				g.addRule(rule14);
				g.addRule(dummyRule);
				g.addRule(dummy);
				g.addRule(miau1);
				g.addRule(miau2);
			} else {
				ParserRule rule1 = new ParserRule("Rec");
				rule1.setAsAtomicRule(true);
				rule1.addSyntax("Dummy Test");
				rule1.addSyntax("Syntax Test");
				rule1.addSyntax("Dummy Syntax");
				
				ParserRule dummy = new ParserRule("Dummy", rule1.getName());
				dummy.setAsAtomicRule(true);
				
				g.addRule(rule1);
				g.addRule(dummy);
			}
			
			g.sort();
			g.createRuleForecast();
			g.createStartRuleForecasts();
		}
		
		return g;
	}
	
	/**
	 * Creates a grammar that has to get left factored
	 * 
	 * @param LF_II
	 *            Should the LF_II algorithm should be needed?
	 * @return The created grammar
	 */
	public static Grammar createLeftFactorGrammar(boolean LF_II) {
		return createLeftFactorGrammar(LF_II, false);
	}
	
	/**
	 * Creates a grammar that has to get left factored (LF_I)
	 * 
	 * @return The created grammar
	 */
	public static Grammar createLeftFactorGrammar() {
		return createLeftFactorGrammar(false, false);
	}
	
}