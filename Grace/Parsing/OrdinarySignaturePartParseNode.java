//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SignaturePartParseNode;
import java.io.PrintStream;
import java.util.List;

/**
* An ordinary literal part of a method signature.
*/
public class OrdinarySignaturePartParseNode  extends SignaturePartParseNode 
{
    /**
    * Ordinary parameters of this part
    */
    private List<ParseNode> __Parameters;
    public List<ParseNode> getParameters() {
        return __Parameters;
    }

    public void setParameters(List<ParseNode> value) {
        __Parameters = value;
    }

    /**
    * Generic parameters of this part
    */
    private List<ParseNode> __GenericParameters;
    public List<ParseNode> getGenericParameters() {
        return __GenericParameters;
    }

    public void setGenericParameters(List<ParseNode> value) {
        __GenericParameters = value;
    }

    private String _name;
    /**
    * This property gets the value of the string field _name
    */
    public String getName() throws Exception {
        return _name;
    }

    /**
    * @param name Name of this part
    */
    public OrdinarySignaturePartParseNode(IdentifierParseNode name) throws Exception {
        super(name);
        _name = name.getName();
        setParameters(new CSList<ParseNode>());
        setGenericParameters(new CSList<ParseNode>());
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Part: " + getName());
        if (getGenericParameters().size() > 0)
        {
            tw.println(prefix + "  Generic Parameters:");
            for (ParseNode p : getGenericParameters())
                p.debugPrint(tw,prefix + "    ");
        }
         
        if (getParameters().size() > 0)
        {
            tw.println(prefix + "  Parameters:");
            for (ParseNode p : getParameters())
                p.debugPrint(tw,prefix + "    ");
        }
         
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


