//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;


/**
* Parse node for a block
*/
public class BlockParseNode  extends ParseNode 
{
    private List<ParseNode> _parameters;
    /**
    * Parameters of the block
    */
    public List<ParseNode> getParameters()  {
        return _parameters;
    }

    public void setParameters(List<ParseNode> value)  {
        _parameters = value;
    }

    private List<ParseNode> _body;
    /**
    * Body of the block
    */
    public List<ParseNode> getBody()  {
        return _body;
    }

    public void setBody(List<ParseNode> value)  {
        _body = value;
    }

    public BlockParseNode(Token tok)  {
        super(tok);
        _body = new ArrayList<ParseNode>();
        _parameters = new ArrayList<ParseNode>();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Block:");
        tw.println(prefix + "  Parameters:");
        for (ParseNode n : _parameters)
        {
            n.debugPrint(tw,prefix + "    ");
        }
        tw.println(prefix + "  Body:");
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


