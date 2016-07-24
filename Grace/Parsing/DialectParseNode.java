//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a dialect declaration
*/
public class DialectParseNode  extends ParseNode 
{
    private ParseNode _path;
    /**
    * Given import path in the syntax
    */
    public ParseNode getPath() throws Exception {
        return _path;
    }

    public void setPath(ParseNode value) throws Exception {
        _path = value;
    }

    public DialectParseNode(Token tok, ParseNode path) throws Exception {
        super(tok);
        this._path = path;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Dialect:");
        _path.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


