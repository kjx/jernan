//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.Parsing.IdentifierParseNode;
import java.io.PrintStream;


import Grace.Execution.IdentifierNode;
import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.ParameterNode;

/**
* A parameter a : b
*/
public class ParameterNode  extends IdentifierNode 
{
    /**
    * The declared type on this parameter
    */
    private Node __Type;
    public Node getType() {
        return __Type;
    }

    public void setType(Node value) {
        __Type = value;
    }

    /**
    * Whether this parameter is variadic *x or not
    */
    private boolean __Variadic;
    public boolean getVariadic() {
        return __Variadic;
    }

    public void setVariadic(boolean value) {
        __Variadic = value;
    }

    public ParameterNode(Token location, IdentifierParseNode source) throws Exception {
        super(location, source);
    }

    public ParameterNode(Token location, IdentifierParseNode source, Node type) throws Exception {
        super(location, source);
        setType(type);
    }

    public ParameterNode(Token location, IdentifierParseNode source, boolean variadic, Node type) throws Exception {
        super(location, source);
        setVariadic(variadic);
        setType(type);
    }

    public ParameterNode(Token location, IdentifierParseNode source, boolean variadic) throws Exception {
        super(location, source);
        setVariadic(variadic);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Parameter: " + getName());
        tw.println(prefix + "  Variadic: " + getVariadic());
        if (getType() != null)
        {
            tw.println(prefix + "  Type: ");
            getType().debugPrint(tw,prefix + "    ");
        }
         
    }


}


