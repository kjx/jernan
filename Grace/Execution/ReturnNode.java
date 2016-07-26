//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ReturnParseNode;
import java.io.PrintStream;


import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.ReturnNode;

/**
* A return statement
*/
public class ReturnNode  extends Node 
{
    public ReturnNode(Token location, ReturnParseNode source, Node val) throws Exception {
        super(location, source);
        setValue(val);
    }

    /**
    * The returned expression
    */
    private Node __Value;
    public Node getValue() {
        return __Value;
    }

    public void setValue(Node value) {
        __Value = value;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Return:");
        if (getValue() != null)
        {
            tw.println(prefix + "  Value:");
            getValue().debugPrint(tw,prefix + "    ");
        }
         
    }
}


