//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;


import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* A component of a method name.
*/
public abstract class SignaturePartParseNode  extends ParseNode 
{
    /**
    * Whether this must be the last part of the name
    */
    private boolean __Final;
    public boolean getFinal() {
        return __Final;
    }

    public void setFinal(boolean value) {
        __Final = value;
    }

    /**
    * The name of this part
    */
    public abstract String getName();

    /**
    * @param name Name of this part
    */
    public SignaturePartParseNode(IdentifierParseNode name)  {
        super(name.getToken());
    }

    /**
    * @param t Representative token for this part
    */
    public SignaturePartParseNode(Token t)  {
        super(t);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "SignaturePart:");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


