//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a parenthesised expression
*/
public class ParenthesisedParseNode  extends ParseNode 
{
    private ParseNode _expr;
    /**
    * Expression in parentheses
    */
    public ParseNode getExpression()  {
        return _expr;
    }

    public void setExpression(ParseNode value)  {
        _expr = value;
    }

    public ParenthesisedParseNode(Token tok, ParseNode expr)  {
        super(tok);
        _expr = expr;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Parenthesised:");
        _expr.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


