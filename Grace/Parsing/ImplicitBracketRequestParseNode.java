//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;

import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an implicit-receiver bracket request
*/
public class ImplicitBracketRequestParseNode  extends ParseNode 
{
    /**
    * Name of this method
    */
    private String __Name;
    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    /**
    * Arguments to this request
    */
    private List<ParseNode> __Arguments;
    public List<ParseNode> getArguments() {
        return __Arguments;
    }

    public void setArguments(List<ParseNode> value) {
        __Arguments = value;
    }

    public ImplicitBracketRequestParseNode(Token start, String name, List<ParseNode> arguments)  {
        super(start);
        setName(name);
        setArguments(arguments);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "ImplicitBracketRequest: " + getName());
        tw.println(prefix + "  Parts:");
        for (ParseNode arg : getArguments())
            arg.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


