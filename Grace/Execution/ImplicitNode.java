//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.Node;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;

/**
* A node representing an implicit value arising from an absent
* specification in the source text.
*/
public class ImplicitNode  extends Node 
{
    private String kind = new String();
    /**
    * @param n Kind of implicit this is
    */
    public ImplicitNode(String n) throws Exception {
        super(null, null);
        kind = n;
    }

    /**
    * @param n Kind of implicit this is
    *  @param basis ParseNode location of this Implicit
    */
    public ImplicitNode(String n, ParseNode basis) throws Exception {
        super(basis.getToken(), basis);
        kind = n;
    }

    /**
    * 
    */
    protected String getVisibleName() throws Exception {
        return "Implicit" + kind;
    }

    // This node never appears in the tree, so these methods will
    // never be called.
    /**
    * 
    */

    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Implicit" + kind);
    }

}


