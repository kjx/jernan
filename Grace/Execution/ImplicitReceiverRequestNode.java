//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;


import Grace.StringSupport;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

/**
* A method request with no syntactic receiver
*/
public class ImplicitReceiverRequestNode  extends RequestNode 
{
    public ImplicitReceiverRequestNode(Token location, ParseNode source)  {
        super(location, source);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "ImplicitReceiverRequest: " + getName());
        if (parts.size() == 1)
        {
            if (parts.get(0).getArguments().size() == 0 && parts.get(0).getGenericArguments().size() == 0)
                return ;
             
        }
         
        tw.println(prefix + "  Parts:");
        int i = 1;
        for (Object __dummyForeachVar2 : parts)
        {
            RequestPartNode p = (RequestPartNode)__dummyForeachVar2;
            String partName = p.getName();
            tw.println(prefix + "    Part " + i + ": ");
            tw.println(prefix + "      Name: " + p.getName());
            if (p.getGenericArguments().size() != 0)
            {
                tw.println(prefix + "      Generic arguments:");
                for (Object __dummyForeachVar0 : p.getGenericArguments())
                {
                    Node arg = (Node)__dummyForeachVar0;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            if (p.getArguments().size() != 0)
            {
                tw.println(prefix + "      Arguments:");
                for (Object __dummyForeachVar1 : p.getArguments())
                {
                    Node arg = (Node)__dummyForeachVar1;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            i++;
        }
    }

}


