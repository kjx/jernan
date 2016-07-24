//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import java.io.PrintStream;

/**
* Parse node for a implicit-receiver request
*/
public class ImplicitReceiverRequestParseNode  extends ParseNode 
{
    private CSList<ParseNode> _nameParts;
    private String _name;
    /**
    * Name of this method
    */
    public String getName() throws Exception {
        return _name;
    }

    /**
    * Parts of this method
    */
    public CSList<ParseNode> getNameParts() throws Exception {
        return _nameParts;
    }

    public void setNameParts(CSList<ParseNode> value) throws Exception {
        _nameParts = value;
    }

    private CSList<CSList<ParseNode>> _arguments;
    /**
    * Argument lists of each part
    */
    public CSList<CSList<ParseNode>> getArguments() throws Exception {
        return _arguments;
    }

    public void setArguments(CSList<CSList<ParseNode>> value) throws Exception {
        _arguments = value;
    }

    private CSList<CSList<ParseNode>> _genericArguments;
    /**
    * Generic argument lists of each part
    */
    public CSList<CSList<ParseNode>> getGenericArguments() throws Exception {
        return _genericArguments;
    }

    public void setGenericArguments(CSList<CSList<ParseNode>> value) throws Exception {
        _genericArguments = value;
    }

    public ImplicitReceiverRequestParseNode(ParseNode id) throws Exception {
        super(id);
        _nameParts = new CSList<ParseNode>();
        _arguments = new CSList<CSList<ParseNode>>();
        _genericArguments = new CSList<CSList<ParseNode>>();
        addPart(id);
    }

    /**
    * Add a part to the method requested here
    */
    public void addPart(ParseNode id) throws Exception {
        _nameParts.add(id);
        _arguments.add(new CSList<ParseNode>());
        _genericArguments.add(new CSList<ParseNode>());
        String partname = ((IdentifierParseNode)id).getName();
        if (_name == null)
            _name = partname;
        else
            _name += " " + partname; 
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        String name = "";
        for (ParseNode n : _nameParts)
        {
            name += (n instanceof IdentifierParseNode ? (IdentifierParseNode)n : (IdentifierParseNode)null).getName() + " ";
        }
        tw.println(prefix + "ImplicitReceiverRequest: " + name);
        tw.println(prefix + "  Arguments:");
        for (int i = 0;i < _nameParts.size();i++)
        {
            ParseNode partName = _nameParts.get(i);
            CSList<ParseNode> args = _arguments.get(i);
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
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


