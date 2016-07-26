//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.Parsing.DialectParseNode;
import java.io.PrintStream;


import Grace.Execution.DialectNode;
import Grace.Execution.Node;
import Grace.Execution.ObjectConstructorNode;

/**
* A dialect statement
*/
public class DialectNode  extends Node 
{
    private DialectParseNode origin;
    public DialectNode(Token location, DialectParseNode source, ObjectConstructorNode module) throws Exception {
        super(location, source);
        origin = source;
        setModule(module);
    }

    /**
    * Module pathThis property gets the string value of the
    * path field of the originating parse node
    */
    public String getPath() throws Exception {
        return (origin.Path instanceof StringLiteralParseNode ? (StringLiteralParseNode)origin.Path : (StringLiteralParseNode)null).Value;
    }

    /**
    * The module in which this dialect statement appears.
    */
    private ObjectConstructorNode __Module;
    public ObjectConstructorNode getModule() {
        return __Module;
    }

    public void setModule(ObjectConstructorNode value) {
        __Module = value;
    }


    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Dialect:");
        tw.println(prefix + "    " + getPath());
    }

}


