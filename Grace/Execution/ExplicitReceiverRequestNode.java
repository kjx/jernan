//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import CS2JNet.System.StringSupport;
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
    public ExplicitReceiverRequestNode(Token location, ParseNode source, Node receiver) throws Exception {
        super(location, source);
        this.receiver = receiver;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "ExplicitReceiverRequest: " + getName());
        tw.WriteLine(prefix + "  Receiver:");
        receiver.debugPrint(tw,prefix + "    ");
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
        ImplicitReceiverRequestNode rirq = receiver instanceof ImplicitReceiverRequestNode ? (ImplicitReceiverRequestNode)receiver : (ImplicitReceiverRequestNode)null;
        GraceObject rec = new GraceObject();
        if (rirq != null)
        {
            if (StringSupport.equals(rirq.getName(), "self"))
            {
                if (req == null)
                    return null;
                 
                req.IsInterior = true;
                rec = receiver.evaluate(ctx);
            }
            else if (StringSupport.equals(rirq.getName(), "outer"))
            {
                if (req == null)
                    return null;
                 
                req.IsInterior = true;
                rec = ctx.FindReceiver(req, 1);
                if (rec == null)
                {
                    nestRequest(ctx,req);
                    ErrorReporting.RaiseError(ctx, "R2002", new Dictionary<String, String>{ { "method", getName() }, { "found", "" }, { "bind", "no" } }, "LookupError: No receiver found for ${method}");
                }
                 
            }
            else
            {
                rec = receiver.evaluate(ctx);
            }  
        }
        else
        {
            rec = receiver.evaluate(ctx);
        } 
        return rec;
    }

    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>{ { "receiver", new DelegateMethodTyped0<ExplicitReceiverRequestNode>(mReceiver) } };
    /**
    * 
    */
    protected void addMethods() throws Exception {
        super.addMethods();
        AddMethods(sharedMethods);
    }

    private static GraceObject mReceiver(ExplicitReceiverRequestNode self) throws Exception {
        return self.receiver;
    }

}


