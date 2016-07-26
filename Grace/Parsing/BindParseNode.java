//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a bind :=
*/
public class BindParseNode  extends ParseNode 
{
    private ParseNode _left;
    /**
    * LHS of :=
    */
    public ParseNode getLeft()  {
        return _left;
    }

    public void setLeft(ParseNode value)  {
        _left = value;
    }

    private ParseNode _right;
    /**
    * RHS of :=
    */
    public ParseNode getRight()  {
        return _right;
    }

    public void setRight(ParseNode value)  {
        _right = value;
    }

    public BindParseNode(Token tok, ParseNode l, ParseNode r)  {
        super(tok);
        _left = l;
        _right = r;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Bind:");
        _left.debugPrint(tw,prefix + "    ");
        _right.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


