//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;

import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a type statement
*/
public class TypeStatementParseNode  extends ParseNode 
{
    private ParseNode _baseName;
    /**
    * Name of this type
    */
    public ParseNode getBaseName()  {
        return _baseName;
    }

    public void setBaseName(ParseNode value)  {
        _baseName = value;
    }

    private ParseNode _body;
    /**
    * Value of this type declaration
    */
    public ParseNode getBody()  {
        return _body;
    }

    public void setBody(ParseNode value)  {
        _body = value;
    }

    /**
    * Generic parameters of this type
    */
    private List<ParseNode> genericParameters;
    /**
    * Generic parameters of this type
    */
    public List<ParseNode> getGenericParameters()  {
        return genericParameters;
    }

    public TypeStatementParseNode(Token tok, ParseNode baseName, ParseNode body, List<ParseNode> generics)  {
        super(tok);
        this._baseName = baseName;
        this._body = body;
        this.genericParameters = generics;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "TypeStatement:");
        tw.println(prefix + "  Name:");
        _baseName.debugPrint(tw,prefix + "    ");
        if (genericParameters.size() > 0)
        {
            tw.println(prefix + "  Generic parameters:");
            for (ParseNode n : genericParameters)
                n.debugPrint(tw,prefix + "    ");
        }
         
        tw.println(prefix + "  Body:");
        _body.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor)  {
        return visitor.visit(this);
    }

}


