//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

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
    private boolean defer = new boolean();
    private boolean found = new boolean();
    private boolean needsScope = new boolean();
    /**
    * 
    */
    public IfThenRequestNode(Token location, ParseNode source) throws Exception {
        super(location, source);
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        if (defer)
            return super.evaluate(ctx);
         
        BlockNode block = parts[1].Arguments[0] instanceof BlockNode ? (BlockNode)parts[1].Arguments[0] : (BlockNode)null;
        if (block == null)
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
                for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ n : block.getBody())
                {
                    if (n instanceof VarDeclarationNode || n instanceof DefDeclarationNode)
                    {
                        needsScope = true;
                    }
                     
                }
            }
            else
                defer = true; 
            return super.performRequest(ctx, r, req);
        }
         
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ test = parts[0].Arguments[0].Evaluate(ctx);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ b = test instanceof GraceBoolean ? (GraceBoolean)test : (GraceBoolean)null;
        if (b == null)
        {
            defer = true;
            return super.evaluate(ctx);
        }
         
        // FIXME This will reevaluate the condition!
        if (b == GraceBoolean.True)
        {
            if (needsScope)
            {
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ myScope = new LocalScope();
                ctx.Extend(myScope);
                for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ n : block.getBody())
                    n.Evaluate(ctx);
                ctx.Unextend(myScope);
                return GraceObject.Done;
            }
             
            for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ n : block.getBody())
                n.Evaluate(ctx);
        }
         
        return GraceObject.Done;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "IfThenRequest: " + getName());
        if (parts.Count == 1)
        {
            if (parts[0].Arguments.Count == 0 && parts[0].GenericArguments.Count == 0)
                return ;
             
        }
         
        tw.WriteLine(prefix + "  Parts:");
        int i = 1;
        for (Object __dummyForeachVar5 : parts)
        {
            RequestPartNode p = (RequestPartNode)__dummyForeachVar5;
            String partName = p.getName();
            tw.WriteLine(prefix + "    Part " + i + ": ");
            tw.WriteLine(prefix + "      Name: " + p.getName());
            if (p.getGenericArguments().Count != 0)
            {
                tw.WriteLine(prefix + "      Generic arguments:");
                for (Object __dummyForeachVar3 : p.getGenericArguments())
                {
                    Node arg = (Node)__dummyForeachVar3;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            if (p.getArguments().Count != 0)
            {
                tw.WriteLine(prefix + "      Arguments:");
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


