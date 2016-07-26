//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.Node;

/**
* A placeholder node with no effect
*/
public class NoopNode  extends Node 
{
    public NoopNode(Token location, ParseNode source) throws Exception {
        super(location, source);
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        return GraceObject.Done;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "Noop");
    }

    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>();
    /**
    * 
    */
    protected void addMethods() throws Exception {
        AddMethods(sharedMethods);
    }

}


