//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.OperatorToken;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import java.io.PrintStream;

/**
* Parse node for a prefix operator
*/
public class PrefixOperatorParseNode  extends ParseNode 
{
    private String _name;
    /**
    * Name (symbol) of the operator
    */
    public String getName() throws Exception {
        return _name;
    }

    public void setName(String value) throws Exception {
        _name = value;
    }

    private ParseNode _receiver;
    /**
    * Receiver of the operator request
    */
    public ParseNode getReceiver() throws Exception {
        return _receiver;
    }

    public void setReceiver(ParseNode value) throws Exception {
        _receiver = value;
    }

    public PrefixOperatorParseNode(OperatorToken tok, ParseNode expr) throws Exception {
        super(tok);
        this._name = tok.getName();
        this._receiver = expr;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "PrefixOperator: " + _name);
        _receiver.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


