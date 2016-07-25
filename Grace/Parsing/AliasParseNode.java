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
* Parse node for an alias clause
*/
public class AliasParseNode  extends ParseNode 
{
    /**
    * Newly-created name
    */
    private SignatureParseNode __NewName;
    public SignatureParseNode getNewName() {
        return __NewName;
    }

    public void setNewName(SignatureParseNode value) {
        __NewName = value;
    }

    /**
    * Name that is aliased
    */
    private SignatureParseNode __OldName;
    public SignatureParseNode getOldName() {
        return __OldName;
    }

    public void setOldName(SignatureParseNode value) {
        __OldName = value;
    }

    public AliasParseNode(Token tok, SignatureParseNode n, SignatureParseNode o) throws Exception {
        super(tok);
        setNewName(n);
        setOldName(o);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Alias:");
        tw.println(prefix + "  New name:");
        getNewName().debugPrint(tw,prefix + "    ");
        tw.println(prefix + "  Old name:");
        getOldName().debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


