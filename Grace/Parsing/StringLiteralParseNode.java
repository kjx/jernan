//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.StringToken;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a string literal
*/
public class StringLiteralParseNode  extends ParseNode 
{
    private String _value;
    /**
    * String value after escape processing
    */
    public String getValue() throws Exception {
        return _value;
    }

    public void setValue(String value) throws Exception {
        _value = value;
    }

    private String raw;
    /**
    * Literal string as written, without
    * escape processing
    */
    public String getRaw() throws Exception {
        return raw;
    }

    public StringLiteralParseNode(Token tok) throws Exception {
        super(tok);
        StringToken comm = tok instanceof StringToken ? (StringToken)tok : (StringToken)null;
        _value = comm.getValue();
        raw = comm.getRaw();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "StringLiteral: " + raw);
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


