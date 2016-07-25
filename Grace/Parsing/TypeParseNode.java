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
* Parse node for a type
*/
public class TypeParseNode  extends ParseNode 
{
    private List<ParseNode> _body;
    /**
    * Body of this type
    */
    public List<ParseNode> getBody() throws Exception {
        return _body;
    }

    public void setBody(List<ParseNode> value) throws Exception {
        _body = value;
    }

    /**
    * Name of this type for debugging
    */
    private String __Name;
    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    public TypeParseNode(Token tok, List<ParseNode> body) throws Exception {
        super(tok);
        this._body = body;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Type:");
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


