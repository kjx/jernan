//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import java.io.PrintStream;

/**
* Parse node for a varargs parameter
*/
public class VarArgsParameterParseNode  extends ParseNode 
{
    private ParseNode _name;
    /**
    * Name of the parameter
    */
    public ParseNode getName()  {
        return _name;
    }

    public void setName(ParseNode value)  {
        _name = value;
    }

    public VarArgsParameterParseNode(ParseNode name)  {
        super(name);
        this._name = name;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "VarArgsParameter:");
        tw.println(prefix + "  Name:");
        _name.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


