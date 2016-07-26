//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
/**
* Parse node for a interpolated string
*/
public class InterpolatedStringParseNode  extends ParseNode 
{
    private List<ParseNode> _parts;
    /**
    * List of component strings and stringifiables
    */
    public List<ParseNode> getParts()  {
        return _parts;
    }

    public void setParts(List<ParseNode> value)  {
        _parts = value;
    }

    public InterpolatedStringParseNode(Token tok)  {
        super(tok);
        _parts = new ArrayList<ParseNode>();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "InterpolatedString:");
        for (ParseNode n : _parts)
            n.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


