//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.ReturnNode;

/**
* A return statement
*/
public class ReturnNode  extends Node 
{
    public ReturnNode(Token location, ReturnParseNode source, Node val) throws Exception {
        super(location, source);
        setValue(val);
    }

    /**
    * The returned expression
    */
    private Node __Value;
    public Node getValue() {
        return __Value;
    }

    public void setValue(Node value) {
        __Value = value;
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        MethodScope ms = ctx.FindNearestMethod();
        if (ms == null)
            ErrorReporting.RaiseError(ctx, "R2016", new Dictionary<String, String>(), "IllegalReturnError: top-level return");
         
        if (getValue() != null)
            ms.Return(ctx, getValue().evaluate(ctx), this);
        else
            ms.Return(ctx, GraceObject.Done, this); 
        return GraceObject.Done;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "Return:");
        if (getValue() != null)
        {
            tw.WriteLine(prefix + "  Value:");
            getValue().debugPrint(tw,prefix + "    ");
        }
         
    }

    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>{ { "value", new DelegateMethodTyped0<ReturnNode>(mValue) } };
    /**
    * 
    */
    protected void addMethods() throws Exception {
        AddMethods(sharedMethods);
    }

    private static GraceObject mValue(ReturnNode self) throws Exception {
        if (self.getValue() != null)
            return self.getValue();
         
        return new ImplicitNode("Done",self.getOrigin());
    }

}


