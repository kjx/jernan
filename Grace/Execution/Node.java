//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;

/**
* An abstract executable representation of a piece of
* source code
*/
public abstract class Node //KJX removed extending GraceObject
{
    /**
    * The original source code location whence this
    * Node originate
    */
    public Token Location;
    private ParseNode parseNode;
    /**
    * The ParseNode whence this Node originatedThis property gets the value of the field parseNode
    */
    public ParseNode getOrigin() throws Exception {
        return parseNode;
    }

    /**
    * @param location Token spawning this node
    *  @param source ParseNode spawning this node
    */
    public Node(Token location, ParseNode source) throws Exception {
        this.Location = location;
        this.parseNode = source;
    }

    /**
    * @param source ParseNode spawning this node
    */
    public Node(ParseNode source) throws Exception {
        this.parseNode = source;
    }

    /**
    * Writes a textual debugging representation of this node
    * 
    *  @param tw Destination for debugging string
    *  @param prefix String to prepend to each line
    */
    public abstract void debugPrint(PrintStream tw, String prefix) throws Exception ;

    /**
    * Gets the name used for user-visible tasks (such as
    * visitors) for this node.
    */
    protected String getVisibleName() throws Exception {
        String name = this.getClass().getName(); 
        return "KJX-WTFFF" + name.substring(0, name.length() - 4); ///WTFFFF
    }
    
}


