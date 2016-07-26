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
* Parse node for an explicit-receiver bracket request
*/
public class ExplicitBracketRequestParseNode  extends ParseNode 
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
    * Receiver  ofthis request
    */
    private ParseNode __Receiver;
    public ParseNode getReceiver() {
        return __Receiver;
    }

    public void setReceiver(ParseNode value) {
        __Receiver = value;
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

    public ExplicitBracketRequestParseNode(Token start, String name, ParseNode receiver, List<ParseNode> arguments)  {
        super(start);
        setName(name);
        setReceiver(receiver);
        setArguments(arguments);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "ExplicitBracketRequest: " + getName());
        tw.println(prefix + "  Receiver:");
        getReceiver().debugPrint(tw,prefix + "    ");
        tw.println(prefix + "  Arguments:");
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


