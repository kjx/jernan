//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an operator
*/
public class OperatorParseNode  extends ParseNode 
{
    private ParseNode _left;
    /**
    * LHS of the operator
    */
    public ParseNode getLeft() throws Exception {
        return _left;
    }

    public void setLeft(ParseNode value) throws Exception {
        _left = value;
    }

    private ParseNode _right;
    /**
    * RHS of the operator
    */
    public ParseNode getRight() throws Exception {
        return _right;
    }

    public void setRight(ParseNode value) throws Exception {
        _right = value;
    }

    /**
    * The name (symbol) of the operator
    */
    public String name;
    /**
    * The name (symbol) of the operator
    */
    public String getName() throws Exception {
        return name;
    }

    public OperatorParseNode(Token tok, String name, ParseNode l, ParseNode r) throws Exception {
        super(tok);
        this.name = name;
        _left = l;
        _right = r;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Operator: " + name);
        _left.debugPrint(tw,prefix + "    ");
        _right.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


