//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

/**
* A method request on the inbuilt prelude
*/
public class PreludeRequestNode  extends RequestNode 
{
    public PreludeRequestNode(Token location, ParseNode source) throws Exception {
        super(location, source);
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "PreludeRequest: " + getName());
        if (parts.Count == 1)
        {
            if (parts[0].Arguments.Count == 0 && parts[0].GenericArguments.Count == 0)
                return ;
             
        }
         
        tw.WriteLine(prefix + "  Parts:");
        int i = 1;
        for (Object __dummyForeachVar2 : parts)
        {
            RequestPartNode p = (RequestPartNode)__dummyForeachVar2;
            String partName = p.getName();
            tw.WriteLine(prefix + "    Part " + i + ": ");
            tw.WriteLine(prefix + "      Name: " + p.getName());
            if (p.getGenericArguments().Count != 0)
            {
                tw.WriteLine(prefix + "      Generic arguments:");
                for (Object __dummyForeachVar0 : p.getGenericArguments())
                {
                    Node arg = (Node)__dummyForeachVar0;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            if (p.getArguments().Count != 0)
            {
                tw.WriteLine(prefix + "      Arguments:");
                for (Object __dummyForeachVar1 : p.getArguments())
                {
                    Node arg = (Node)__dummyForeachVar1;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            i++;
        }
    }

    /**
    * 
    */
    protected GraceObject getReceiver(EvaluationContext ctx, MethodRequest req) throws Exception {
        return ctx.Prelude;
    }

}


