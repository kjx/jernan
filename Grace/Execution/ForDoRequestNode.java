//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.BlockNode;
import Grace.Execution.IdentifierNode;
import Grace.Execution.ImplicitReceiverRequestNode;
import Grace.Execution.Node;
import Grace.Execution.RequestPartNode;

/**
* Specialisation for for-do requests
*/
public class ForDoRequestNode  extends ImplicitReceiverRequestNode 
{
    private boolean defer = new boolean();
    private boolean found = new boolean();
    /**
    * 
    */
    public ForDoRequestNode(Token location, ParseNode source) throws Exception {
        super(location, source);
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        if (defer)
            return super.evaluate(ctx);
         
        BlockNode block = parts[1].Arguments[0] instanceof BlockNode ? (BlockNode)parts[1].Arguments[0] : (BlockNode)null;
        if (block == null || block.getParameters().Count != 1)
        {
            defer = true;
            return super.evaluate(ctx);
        }
         
        if (!found)
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ req = createRequest(ctx);
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ r = GetReceiver(ctx, req);
            if (r == ctx.Prelude)
            {
                found = true;
            }
            else
                defer = true; 
            return super.performRequest(ctx, r, req);
        }
         
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ iterable = parts[0].Arguments[0].Evaluate(ctx);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ gr = iterable instanceof GraceRange ? (GraceRange)iterable : (GraceRange)null;
        if (gr != null)
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ p = block.getParameters()[0];
            IdentifierNode i = p instanceof IdentifierNode ? (IdentifierNode)p : (IdentifierNode)null;
            if (i == null)
                goto end
             
            if (gr.Step < 0)
                goto end
             
            String name = i.getName();
            for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ v = gr.Start;v <= gr.End;v += gr.Step)
            {
                LocalScope l = new LocalScope();
                l.AddLocalDef(name, GraceNumber.Create(v));
                ctx.Extend(l);
                for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ n : block.getBody())
                    n.Evaluate(ctx);
                ctx.Unextend(l);
            }
            return GraceObject.Done;
        }
         
        end:/* [UNSUPPORTED] 'var' as type is unsupported "var" */ doReq = MethodRequest.Single("do", block.evaluate(ctx));
        iterable.Request(ctx, doReq);
        return GraceObject.Done;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "ForDoRequest: " + getName());
        if (parts.Count == 1)
        {
            if (parts[0].Arguments.Count == 0 && parts[0].GenericArguments.Count == 0)
                return ;
             
        }
         
        tw.WriteLine(prefix + "  Parts:");
        int i = 1;
        for (Object __dummyForeachVar3 : parts)
        {
            RequestPartNode p = (RequestPartNode)__dummyForeachVar3;
            String partName = p.getName();
            tw.WriteLine(prefix + "    Part " + i + ": ");
            tw.WriteLine(prefix + "      Name: " + p.getName());
            if (p.getGenericArguments().Count != 0)
            {
                tw.WriteLine(prefix + "      Generic arguments:");
                for (Object __dummyForeachVar1 : p.getGenericArguments())
                {
                    Node arg = (Node)__dummyForeachVar1;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            if (p.getArguments().Count != 0)
            {
                tw.WriteLine(prefix + "      Arguments:");
                for (Object __dummyForeachVar2 : p.getArguments())
                {
                    Node arg = (Node)__dummyForeachVar2;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            i++;
        }
    }

}


