//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;


import Grace.Execution.BlockNode;
import Grace.Execution.DefDeclarationNode;
import Grace.Execution.ImplicitReceiverRequestNode;
import Grace.Execution.Node;
import Grace.Execution.RequestPartNode;
import Grace.Execution.VarDeclarationNode;

/**
* Specialisation for if-then requests
*/
public class IfThenRequestNode  extends ImplicitReceiverRequestNode 
{
    private boolean defer;
    private boolean found;
    private boolean needsScope;
    /**
    * 
    */
    public IfThenRequestNode(Token location, ParseNode source) throws Exception {
        super(location, source);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "IfThenRequest: " + getName());
        if (parts.size() == 1)
        {
            if (parts.get(0).getArguments().size() == 0 && parts.get(0).getGenericArguments().size() == 0)
                return ;
             
        }
         
        tw.println(prefix + "  Parts:");
        int i = 1;
        for (Object __dummyForeachVar5 : parts)
        {
            RequestPartNode p = (RequestPartNode)__dummyForeachVar5;
            String partName = p.getName();
            tw.println(prefix + "    Part " + i + ": ");
            tw.println(prefix + "      Name: " + p.getName());
            if (p.getGenericArguments().size() != 0)
            {
                tw.println(prefix + "      Generic arguments:");
                for (Object __dummyForeachVar3 : p.getGenericArguments())
                {
                    Node arg = (Node)__dummyForeachVar3;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            if (p.getArguments().size() != 0)
            {
                tw.println(prefix + "      Arguments:");
                for (Object __dummyForeachVar4 : p.getArguments())
                {
                    Node arg = (Node)__dummyForeachVar4;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            i++;
        }
    }

}


