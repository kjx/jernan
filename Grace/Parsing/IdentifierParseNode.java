//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.BindToken;
import Grace.Parsing.IdentifierToken;
import Grace.Parsing.OpenBracketToken;
import Grace.Parsing.OperatorToken;
import Grace.Parsing.OuterKeywordToken;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SelfKeywordToken;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an identifier
*/
public class IdentifierParseNode  extends ParseNode 
{
    private String _name;
    /**
    * Name of this identifier
    */
    public String getName()  {
        return _name;
    }

    public void setName(String value)  {
        _name = value;
    }

    public IdentifierParseNode(Token tok)  {
        super(tok);
        IdentifierToken it = tok instanceof IdentifierToken ? (IdentifierToken)tok : (IdentifierToken)null;
        _name = it.getName();
    }

    public IdentifierParseNode(Token tok, String name)  {
        super(tok);
        _name = name;
    }

    public IdentifierParseNode(OperatorToken tok)  {
        super(tok);
        _name = tok.getName();
    }

    public IdentifierParseNode(OpenBracketToken tok)  {
        super(tok);
        _name = tok.getName() + tok.getOther();
    }

    public IdentifierParseNode(SelfKeywordToken tok)  {
        super(tok);
        _name = "self";
    }

    public IdentifierParseNode(OuterKeywordToken tok)  {
        super(tok);
        _name = "outer";
    }

    public IdentifierParseNode(BindToken tok)  {
        super(tok);
        _name = ":=";
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Identifier: " + _name);
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}



