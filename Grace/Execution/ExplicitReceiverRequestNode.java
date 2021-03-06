//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;


import Grace.Execution.ExplicitReceiverRequestNode;
import Grace.Execution.ImplicitReceiverRequestNode;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

/**
* A method request with a syntactic receiver
*/
public class ExplicitReceiverRequestNode  extends RequestNode 
{
    private Node receiver;
    public ExplicitReceiverRequestNode(Token location, ParseNode source, Node receiver)  {
        super(location, source);
        this.receiver = receiver;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "ExplicitReceiverRequest: " + getName());
        tw.println(prefix + "  Receiver:");
        receiver.debugPrint(tw,prefix + "    ");
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


