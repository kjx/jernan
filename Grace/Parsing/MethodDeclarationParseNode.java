//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.Token;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
/**
* Parse node for a method declaration
*/
public class MethodDeclarationParseNode  extends ParseNode 
{
    /**
    * Signature of this method
    */
    private SignatureParseNode __Signature;
    public SignatureParseNode getSignature() {
        return __Signature;
    }

    public void setSignature(SignatureParseNode value) {
        __Signature = value;
    }

    private List<ParseNode> _body;
    /**
    * Body of this method
    */
    public List<ParseNode> getBody()  {
        return _body;
    }

    public void setBody(List<ParseNode> value)  {
        _body = value;
    }

    /**
    * 
    */
    private ParseNode __ReturnType;
    public ParseNode getReturnType() {
        return __ReturnType;
    }

    public void setReturnType(ParseNode value) {
        __ReturnType = value;
    }

    public MethodDeclarationParseNode(Token tok)  {
        super(tok);
        _body = new ArrayList<ParseNode>();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "MethodDeclaration: " + getSignature().getName());
        tw.println(prefix + "  Signature:");
        getSignature().debugPrint(tw,prefix + "    ");
        tw.println(prefix + "  Body:");
        for (ParseNode n : _body)
        {
            n.debugPrint(tw,prefix + "    ");
        }
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


