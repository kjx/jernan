//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.Parsing.IdentifierParseNode;
import java.io.PrintStream;


import Grace.Execution.Node;

/**
* A bare identifier
*/
public abstract class IdentifierNode  extends Node 
{
    private IdentifierParseNode origin;
    public IdentifierNode(Token location, IdentifierParseNode source) throws Exception {
        super(location, source);
        origin = source;
    }

    /**
    * The name of this identifierThis property gets the name field of the originating
    * parse node
    */
    public String getName() throws Exception {
        return origin.getName();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Identifier: " + getName());
    }
}


