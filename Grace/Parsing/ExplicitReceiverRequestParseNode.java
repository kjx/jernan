//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;

import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import java.io.PrintStream;

/**
* Parse node for a explicit-receiver request
*/
public class ExplicitReceiverRequestParseNode  extends ParseNode 
{
    private ParseNode _receiver;
    /**
    * Receiver of this request
    */
    public ParseNode getReceiver()  {
        return _receiver;
    }

    public void setReceiver(ParseNode value)  {
        _receiver = value;
    }

    private List<ParseNode> _nameParts;
    /**
    * Parts of this method
    */
    public List<ParseNode> getNameParts()  {
        return _nameParts;
    }

    public void setNameParts(List<ParseNode> value)  {
        _nameParts = value;
    }

    private List<List<ParseNode>> _arguments;
    /**
    * Argument lists of each part
    */
    public List<List<ParseNode>> getArguments()  {
        return _arguments;
    }

    public void setArguments(List<List<ParseNode>> value)  {
        _arguments = value;
    }

    private List<List<ParseNode>> _genericArguments;
    /**
    * Generic argument lists of each part
    */
    public List<List<ParseNode>> getGenericArguments()  {
        return _genericArguments;
    }

    public void setGenericArguments(List<List<ParseNode>> value)  {
        _genericArguments = value;
    }

    public ExplicitReceiverRequestParseNode(ParseNode receiver)  {
        super(receiver);
        this._receiver = receiver;
        _nameParts = new ArrayList<ParseNode>();
        _arguments = new ArrayList<List<ParseNode>>();
        _genericArguments = new ArrayList<List<ParseNode>>();
    }

    /**
    * Add a part to the method requested here
    */
    public void addPart(ParseNode id)  {
        _nameParts.add(id);
        _arguments.add(new ArrayList<ParseNode>());
        _genericArguments.add(new ArrayList<ParseNode>());
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        String name = "";
        for (ParseNode n : _nameParts)
        {
            name += (n instanceof IdentifierParseNode ? (IdentifierParseNode)n : (IdentifierParseNode)null).getName() + " ";
        }
        tw.println(prefix + "ExplicitReceiverRequest: " + name);
        tw.println(prefix + "  Receiver:");
        _receiver.debugPrint(tw,prefix + "    ");
        tw.println(prefix + "  Parts:");
        for (int i = 0;i < _nameParts.size();i++)
        {
            ParseNode partName = _nameParts.get(i);
            List<ParseNode> args = _arguments.get(i);
            tw.println(prefix + "    Part " + (i + 1) + ": ");
            tw.println(prefix + "      Name:");
            partName.debugPrint(tw,prefix + "        ");
            tw.println(prefix + "      Generic arguments:");
            for (ParseNode arg : _genericArguments.get(i))
                arg.debugPrint(tw,prefix + "        ");
            tw.println(prefix + "      Arguments:");
            for (ParseNode arg : args)
                arg.debugPrint(tw,prefix + "        ");
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


