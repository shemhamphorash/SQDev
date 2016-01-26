package raven.sqf.tests;

import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import raven.SQFInjectorProvider;
import raven.sQF.BracketContent;
import raven.sQF.Code;
import raven.sQF.DecContent;
import raven.sQF.Declaration;
import raven.sQF.Model;
import raven.sQF.VarContent;

@RunWith(XtextRunner.class)
@InjectWith(SQFInjectorProvider.class)
@SuppressWarnings("all")
public class SQF_ParserTest {
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;
  
  @Test
  public void TestDeclarations() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("name = \"Miau\";");
      _builder.newLine();
      _builder.append("_andererName = 34;");
      _builder.newLine();
      _builder.append("undNochEiner = name;");
      _builder.newLine();
      _builder.append("test1 = -1 * -(4+-18)^2;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("array = [4,\"Test\",name];");
      _builder.newLine();
      _builder.append("array2 = array;");
      _builder.newLine();
      _builder.append("array3 = +array2;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("if(name == (\"Miau\" + \"Mama\")) then {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("if(true) then {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("if(!(3<4)) then [{");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("test = 4;");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("},{");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("test = 5;");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}];");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("};");
      _builder.newLine();
      _builder.append("}else {");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("if(true) exitWith {test = 5;};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("while{name isEqualTo array} do {test1 = 3; test2 = 4; test3 = test1;};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("for \"i\" from 2 to 8 do {test1 = 4; test2 = 8; test3 = test1; test=i;};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("for \"i\" from 2 to 8 step 4 do {};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("for [{i=0}, {i<4}, {i=i+1}] do {test = i; test2 = \"Mama\";};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("test1 = \"Mama\";");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("test2 = 3;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("test3 = test1;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("test4 = _x;");
      _builder.newLine();
      _builder.append("} forEach [3,4,6];");
      _builder.newLine();
      _builder.newLine();
      _builder.append("switch(name) do {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("case \"Mama\": {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("test1 = 3;");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("test2 = 4;");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("test3 = test1;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("};");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("case \"Trottel\": {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("};");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("default {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("test=5;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("};");
      _builder.newLine();
      _builder.append("};");
      _builder.newLine();
      _builder.newLine();
      _builder.append("[] spawn test1;");
      _builder.newLine();
      _builder.append("miau = [] call test1;");
      _builder.newLine();
      Model _parse = this._parseHelper.parse(_builder);
      this._validationTestHelper.assertNoErrors(_parse);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("name = \"Miau\";");
      _builder_1.newLine();
      _builder_1.append("_andererName = 34;");
      _builder_1.newLine();
      _builder_1.append("undNochEiner = name;");
      _builder_1.newLine();
      _builder_1.append("test1 = -1 * -(4+-18)^2;");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("array = [4,\"Test\",name];");
      _builder_1.newLine();
      _builder_1.append("array2 = array;");
      _builder_1.newLine();
      _builder_1.append("array3 = +array2;");
      _builder_1.newLine();
      final Model model = this._parseHelper.parse(_builder_1);
      EList<Code> _elements = model.getElements();
      final Code dec1 = _elements.get(0);
      Declaration _dec = dec1.getDec();
      String _name = _dec.getName();
      Assert.assertEquals("name", _name);
      Declaration _dec_1 = dec1.getDec();
      BracketContent _brCon = _dec_1.getBrCon();
      DecContent _decCon = _brCon.getDecCon();
      VarContent _singleContent = _decCon.getSingleContent();
      String _string = _singleContent.getString();
      Assert.assertEquals("Miau", _string);
      EList<Code> _elements_1 = model.getElements();
      final Code dec2 = _elements_1.get(1);
      Declaration _dec_2 = dec2.getDec();
      String _name_1 = _dec_2.getName();
      Assert.assertEquals("_andererName", _name_1);
      Declaration _dec_3 = dec2.getDec();
      BracketContent _brCon_1 = _dec_3.getBrCon();
      DecContent _decCon_1 = _brCon_1.getDecCon();
      VarContent _singleContent_1 = _decCon_1.getSingleContent();
      String _num = _singleContent_1.getNum();
      Assert.assertEquals("34", _num);
      EList<Code> _elements_2 = model.getElements();
      final Code dec3 = _elements_2.get(2);
      Declaration _dec_4 = dec3.getDec();
      String _name_2 = _dec_4.getName();
      Assert.assertEquals("undNochEiner", _name_2);
      Declaration _dec_5 = dec3.getDec();
      BracketContent _brCon_2 = _dec_5.getBrCon();
      DecContent _decCon_2 = _brCon_2.getDecCon();
      VarContent _singleContent_2 = _decCon_2.getSingleContent();
      Declaration _reference = _singleContent_2.getReference();
      String _name_3 = _reference.getName();
      Assert.assertEquals("name", _name_3);
      EList<Code> _elements_3 = model.getElements();
      final Code dec4 = _elements_3.get(3);
      Declaration _dec_6 = dec4.getDec();
      String _name_4 = _dec_6.getName();
      Assert.assertEquals("test1", _name_4);
      Declaration _dec_7 = dec4.getDec();
      BracketContent _brCon_3 = _dec_7.getBrCon();
      DecContent _decCon_3 = _brCon_3.getDecCon();
      VarContent _singleContent_3 = _decCon_3.getSingleContent();
      String _unOP = _singleContent_3.getUnOP();
      Assert.assertEquals("-", _unOP);
      Declaration _dec_8 = dec4.getDec();
      BracketContent _brCon_4 = _dec_8.getBrCon();
      DecContent _decCon_4 = _brCon_4.getDecCon();
      VarContent _singleContent_4 = _decCon_4.getSingleContent();
      String _num_1 = _singleContent_4.getNum();
      Assert.assertEquals("1", _num_1);
      Declaration _dec_9 = dec4.getDec();
      BracketContent _brCon_5 = _dec_9.getBrCon();
      DecContent _decCon_5 = _brCon_5.getDecCon();
      EList<VarContent> _nextCon = _decCon_5.getNextCon();
      VarContent _get = _nextCon.get(0);
      String _unOP_1 = _get.getUnOP();
      Assert.assertEquals("-", _unOP_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}