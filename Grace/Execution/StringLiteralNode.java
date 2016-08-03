//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.TranslationContext;

import Grace.Parsing.Token;
import Grace.Parsing.StringLiteralParseNode;
import java.io.PrintStream;


import Grace.Execution.Node;
import Grace.Execution.StringLiteralNode;

import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.source.Source;


/**
* A string literal
*/
public class StringLiteralNode  extends Node 
{
    private StringLiteralParseNode origin;
    public StringLiteralNode(Token location, StringLiteralParseNode source)  {
        super(location, source);
        origin = source;
    }

    /**
    * The string value of this literalThis property gets the value field of the
    * originating parse node
    */
    public String getValue()  {
        return origin.getValue();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "String: " + getValue());
    }
    
    public som.interpreter.nodes.literals.StringLiteralNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

        return new som.interpreter.nodes.literals.StringLiteralNode(getValue(),source);
    }
}


