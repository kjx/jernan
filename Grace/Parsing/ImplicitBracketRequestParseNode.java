//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
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
    private CSList<ParseNode> __Arguments;
    public CSList<ParseNode> getArguments() {
        return __Arguments;
    }

    public void setArguments(CSList<ParseNode> value) {
        __Arguments = value;
    }

    public ImplicitBracketRequestParseNode(Token start, String name, CSList<ParseNode> arguments) throws Exception {
        super(start);
        setName(name);
        setArguments(arguments);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "ImplicitBracketRequest: " + getName());
        tw.println(prefix + "  Parts:");
        for (ParseNode arg : getArguments())
            arg.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


