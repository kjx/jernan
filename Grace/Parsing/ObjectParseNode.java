//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an Object
*/
public class ObjectParseNode  extends ParseNode 
{
    private CSList<ParseNode> _body;
    /**
    * Body of the object
    */
    public CSList<ParseNode> getBody() throws Exception {
        return _body;
    }

    public void setBody(CSList<ParseNode> value) throws Exception {
        _body = value;
    }

    public ObjectParseNode(Token tok) throws Exception {
        super(tok);
        _body = new CSList<ParseNode>();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Object:");
        for (ParseNode n : _body)
        {
            n.debugPrint(tw,prefix + "    ");
        }
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


