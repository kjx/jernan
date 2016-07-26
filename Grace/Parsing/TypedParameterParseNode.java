//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a typed parameter
*/
public class TypedParameterParseNode  extends ParseNode 
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

    private ParseNode _type;
    /**
    * Type of the parameter
    */
    public ParseNode getType()  {
        return _type;
    }

    public void setType(ParseNode value)  {
        _type = value;
    }

    public TypedParameterParseNode(ParseNode name, ParseNode type)  {
        super(name);
        this._name = name;
        this._type = type;
    }

    public TypedParameterParseNode(ParseNode name, ParseNode type, Token token)  {
        super(token);
        this._name = name;
        this._type = type;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "TypedParameter:");
        tw.println(prefix + "  Name:");
        _name.debugPrint(tw,prefix + "    ");
        tw.println(prefix + "  Type:");
        _type.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}



