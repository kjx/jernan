//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;
import java.util.List;

import Grace.Execution.BlockNode;
import Grace.Execution.Node;
import Grace.Execution.ParameterNode;

/**
* A block expression
*/
public class BlockNode  extends Node 
{
    private List<Node> parameters;
    private List<Node> body;
    private Node _forcedPattern;
    private boolean variadic;

    public BlockNode(Token token, ParseNode source, List<Node> parameters, List<Node> body, Node forcedPattern)  {
        super(token, source);
        this.parameters = parameters;
        this.body = body;
        _forcedPattern = forcedPattern;
        for (Node p : parameters)
        {
            ParameterNode param = p instanceof ParameterNode ? (ParameterNode)p : (ParameterNode)null;
            if (param != null)
                variadic |= param.getVariadic();
         
        }
    }

    /**
    * The parameters of this blockThis property gets the value of the field parameters
    */
    public List<Node> getParameters()  {
        return parameters;
    }

    /**
    * The body of this blockThis property gets the value of the field body
    */
    public List<Node> getBody()  {
        return body;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Block:");
        if (_forcedPattern != null)
        {
            tw.println(prefix + "  Pattern:");
            _forcedPattern.debugPrint(tw,prefix + "    ");
        }
         
        tw.println(prefix + "  Parameters:");
        for (Object __dummyForeachVar1 : parameters)
        {
            Node arg = (Node)__dummyForeachVar1;
            arg.debugPrint(tw,prefix + "    ");
        }
        tw.println(prefix + "  Body:");
        for (Object __dummyForeachVar2 : body)
        {
            Node n = (Node)__dummyForeachVar2;
            n.debugPrint(tw,prefix + "    ");
        }
    }


}


