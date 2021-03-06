//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.AnnotationsParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a def declaration
*/
public class DefDeclarationParseNode  extends ParseNode 
{
    private ParseNode _name;
    /**
    * Name of the def
    */
    public ParseNode getName()  {
        return _name;
    }

    public void setName(ParseNode value)  {
        _name = value;
    }

    private ParseNode _val;
    /**
    * Value of the def
    */
    public ParseNode getValue()  {
        return _val;
    }

    public void setValue(ParseNode value)  {
        _val = value;
    }

    private ParseNode _type;
    /**
    * Type of the def, if any
    */
    public ParseNode getType()  {
        return _type;
    }

    public void setType(ParseNode value)  {
        _type = value;
    }

    private AnnotationsParseNode _annotations;
    /**
    * Annotations of the def, if any
    */
    public AnnotationsParseNode getAnnotations()  {
        return _annotations;
    }

    public void setAnnotations(AnnotationsParseNode value)  {
        _annotations = value;
    }

    public DefDeclarationParseNode(Token tok, ParseNode name, ParseNode val, ParseNode type, AnnotationsParseNode annotations)  {
        super(tok);
        this._name = name;
        this._val = val;
        this._type = type;
        this._annotations = annotations;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "DefDeclaration:");
        tw.println(prefix + "  Name:");
        _name.debugPrint(tw,prefix + "    ");
        if (_type != null)
        {
            tw.println(prefix + "  Type:");
            _type.debugPrint(tw,prefix + "    ");
        }
         
        if (_annotations != null)
        {
            tw.println(prefix + "  Anontations:");
            _annotations.debugPrint(tw,prefix + "    ");
        }
         
        tw.println(prefix + "  Value:");
        _val.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}



