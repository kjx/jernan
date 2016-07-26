//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;


import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an exclude clause
*/
public class ExcludeParseNode  extends ParseNode 
{
    /**
    * Name of excluded method
    */
    private SignatureParseNode __Name;
    public SignatureParseNode getName() {
        return __Name;
    }

    public void setName(SignatureParseNode value) {
        __Name = value;
    }

    public ExcludeParseNode(Token tok, SignatureParseNode n)  {
        super(tok);
        setName(n);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Exclude:");
        getName().debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}



