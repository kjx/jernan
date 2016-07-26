//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


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
    public String getValue()  {
        return _value;
    }

    public void setValue(String value)  {
        _value = value;
    }

    private String raw;
    /**
    * Literal string as written, without
    * escape processing
    */
    public String getRaw()  {
        return raw;
    }

    public StringLiteralParseNode(Token tok)  {
        super(tok);
        StringToken comm = tok instanceof StringToken ? (StringToken)tok : (StringToken)null;
        _value = comm.getValue();
        raw = comm.getRaw();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "StringLiteral: " + raw);
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


