//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;
import Grace.Parsing.AliasParseNode;
import Grace.Parsing.ExcludeParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.Token;
import java.io.PrintStream;
import java.util.List;

/**
* Parse node for a uses statement
*/
public class UsesParseNode  extends ParseNode 
{
    private ParseNode _from;
    /**
    * RHS of the uses clause
    */
    public ParseNode getFrom()  {
        return _from;
    }

    public void setFrom(ParseNode value)  {
        _from = value;
    }

    /**
    * Aliases on this uses statement
    */
    private List<AliasParseNode> __Aliases;
    public List<AliasParseNode> getAliases() {
        return __Aliases;
    }

    public void setAliases(List<AliasParseNode> value) {
        __Aliases = value;
    }

    /**
    * Exclusions on this uses statement
    */
    private List<ExcludeParseNode> __Excludes;
    public List<ExcludeParseNode> getExcludes() {
        return __Excludes;
    }

    public void setExcludes(List<ExcludeParseNode> value) {
        __Excludes = value;
    }

    public UsesParseNode(Token tok, ParseNode expr)  {
        super(tok);
        _from = expr;
        setAliases(new ArrayList<AliasParseNode>());
        setExcludes(new ArrayList<ExcludeParseNode>());
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Uses:");
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
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

    /**
    * Add an alias to this uses statement.
    * 
    *  @param tok Token
    *  @param n New name
    *  @param o Old name
    */
    public void addAlias(Token tok, SignatureParseNode n, SignatureParseNode o)  {
        getAliases().add(new AliasParseNode(tok,n,o));
    }

    /**
    * Add an exclude to this uses statement.
    * 
    *  @param tok Token
    *  @param n Name to exclude
    */
    public void addExclude(Token tok, SignatureParseNode n)  {
        getExcludes().add(new ExcludeParseNode(tok,n));
    }

}


