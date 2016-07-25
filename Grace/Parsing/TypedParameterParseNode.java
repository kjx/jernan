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
    public ParseNode getName() throws Exception {
        return _name;
    }

    public void setName(ParseNode value) throws Exception {
        _name = value;
    }

    private ParseNode _type;
    /**
    * Type of the parameter
    */
    public ParseNode getType() throws Exception {
        return _type;
    }

    public void setType(ParseNode value) throws Exception {
        _type = value;
    }

    public TypedParameterParseNode(ParseNode name, ParseNode type) throws Exception {
        super(name);
        this._name = name;
        this._type = type;
    }

    public TypedParameterParseNode(ParseNode name, ParseNode type, Token token) throws Exception {
        super(token);
        this._name = name;
        this._type = type;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
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
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}



