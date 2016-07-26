//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.StringLiteralParseNode;
import java.io.PrintStream;


import Grace.Execution.Node;
import Grace.Execution.StringLiteralNode;

/**
* A string literal
*/
public class StringLiteralNode  extends Node 
{
    private StringLiteralParseNode origin;
    public StringLiteralNode(Token location, StringLiteralParseNode source) throws Exception {
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
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "String: " + getValue());
    }
}


