//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.AliasParseNode;
import Grace.Parsing.ExcludeParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for an inherits clause
*/
public class InheritsParseNode  extends ParseNode 
{
    private ParseNode _from;
    /**
    * RHS of the inherits clause
    */
    public ParseNode getFrom() throws Exception {
        return _from;
    }

    public void setFrom(ParseNode value) throws Exception {
        _from = value;
    }

    /**
    * Aliases on this inherits statement
    */
    private CSList<AliasParseNode> __Aliases;
    public CSList<AliasParseNode> getAliases() {
        return __Aliases;
    }

    public void setAliases(CSList<AliasParseNode> value) {
        __Aliases = value;
    }

    /**
    * Exclusions on this inherits statement
    */
    private CSList<ExcludeParseNode> __Excludes;
    public CSList<ExcludeParseNode> getExcludes() {
        return __Excludes;
    }

    public void setExcludes(CSList<ExcludeParseNode> value) {
        __Excludes = value;
    }

    public InheritsParseNode(Token tok, ParseNode expr) throws Exception {
        super(tok);
        _from = expr;
        setAliases(new CSList<AliasParseNode>());
        setExcludes(new CSList<ExcludeParseNode>());
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Inherits:");
        tw.println(prefix + "    From:");
        _from.debugPrint(tw,prefix + "        ");
        for (AliasParseNode ap : getAliases())
        {
            ap.debugPrint(tw,prefix + "    ");
        }
        for (ExcludeParseNode ex : getExcludes())
        {
            ex.debugPrint(tw,prefix + "    ");
        }
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

    /**
    * Add an alias to this inherits statement.
    * 
    *  @param tok Token
    *  @param n New name
    *  @param o Old name
    */
    public void addAlias(Token tok, SignatureParseNode n, SignatureParseNode o) throws Exception {
        getAliases().add(new AliasParseNode(tok,n,o));
    }

    /**
    * Add an exclude to this inherits statement.
    * 
    *  @param tok Token
    *  @param n Name to exclude
    */
    public void addExclude(Token tok, SignatureParseNode n) throws Exception {
        getExcludes().add(new ExcludeParseNode(tok,n));
    }

}


