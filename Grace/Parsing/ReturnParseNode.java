//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a return statement
*/
public class ReturnParseNode  extends ParseNode 
{
    private ParseNode _returnValue;
    /**
    * Expression returned, if any
    */
    public ParseNode getReturnValue()  {
        return _returnValue;
    }

    public void setReturnValue(ParseNode value)  {
        _returnValue = value;
    }

    public ReturnParseNode(Token tok, ParseNode val)  {
        super(tok);
        _returnValue = val;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Return:");
        if (_returnValue == null)
            tw.println(prefix + "    (nothing)");
        else
            _returnValue.debugPrint(tw,prefix + "    "); 
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


