//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;

import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an Object
*/
public class ObjectParseNode  extends ParseNode 
{
    private List<ParseNode> _body;
    /**
    * Body of the object
    */
    public List<ParseNode> getBody()  {
        return _body;
    }

    public void setBody(List<ParseNode> value)  {
        _body = value;
    }

    public ObjectParseNode(Token tok)  {
        super(tok);
        _body = new ArrayList<ParseNode>();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
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
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


