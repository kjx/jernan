//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an import
*/
public class ImportParseNode  extends ParseNode 
{
    private ParseNode _path;
    /**
    * Given import path in the syntax
    */
    public ParseNode getPath() {
        return _path;
    }

    public void setPath(ParseNode value)  {
        _path = value;
    }

    private ParseNode _name;
    /**
    * Given "as name" in the syntax
    */
    public ParseNode getName()  {
        return _name;
    }

    public void setName(ParseNode value)  {
        _name = value;
    }

    private ParseNode _type;
    /**
    * Given ": type", if provided
    */
    public ParseNode getType()  {
        return _type;
    }

    public void setType(ParseNode value)  {
        _type = value;
    }

    public ImportParseNode(Token tok, ParseNode path, ParseNode name, ParseNode type) throws Exception {
        super(tok);
        this._path = path;
        this._name = name;
        this._type = type;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Import:");
        tw.println(prefix + "  Path:");
        _path.debugPrint(tw,prefix + "    ");
        tw.println(prefix + "  As:");
        _name.debugPrint(tw,prefix + "    ");
        if (_type != null)
        {
            tw.println(prefix + "  Type:");
            _type.debugPrint(tw,prefix + "    ");
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


