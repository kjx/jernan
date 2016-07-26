//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* A concrete syntax node
*/
public abstract class ParseNode   
{
    private int _line;
    /**
    * Line number this node began on
    */
    public int getLine()  {
        return _line;
    }

    public void setLine(int value)  {
        _line = value;
    }

    private int _column;
    /**
    * Column number this node began at
    */
    public int getColumn()  {
        return _column;
    }

    public void setColumn(int value)  {
        _column = value;
    }

    private ParseNode _comment;
    /**
    * Comment on this node, if any
    */
    public ParseNode getComment()  {
        return _comment;
    }

    public void setComment(ParseNode value)  {
        _comment = value;
    }

    /**
    * Token representing the start or identifying element
    * of this ParseNode.
    */
    private Token __Token;
    public Token getToken() {
        return __Token;
    }

    public void setToken(Token value) {
        __Token = value;
    }

    /**
    * @param tok Token that gave rise to this node
    */
    public ParseNode(Token tok)  {
        this._line = tok.line;
        this._column = tok.column;
        setToken(tok);
    }

    /**
    * @param basis ParseNode that gave rise to this node
    */
    public ParseNode(ParseNode basis)  {
        setToken(basis.getToken());
        this._line = basis._line;
        this._column = basis._column;
    }

    /**
    * Write a human-readable description of this node
    * and its children to a given sink
    *  @param tw Sink to write output into
    *  @param prefix Prefix string to print before each line
    */
    public abstract void debugPrint(PrintStream tw, String prefix)  ;

    /**
    * Write out this node's comment to a stream, if any
    *  @param tw Sink to write output into
    *  @param prefix Prefix string to print before each line
    */
    public void writeComment(PrintStream tw, String prefix)  {
        if (this._comment != null)
        {
            tw.println(prefix + "  Comment:");
            this._comment.debugPrint(tw,prefix + "    ");
        }
         
    }

    /**
    * Double-dispatch visitor for parse nodes
    *  @param visitor Visitor to double-dispatch toReturn type of visitor
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


