//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.Node;
import Grace.Execution.SignatureNode;
import Grace.Execution.TypeNode;

/**
* A type literal
*/
public class TypeNode  extends Node 
{
    private List<SignatureNode> body = new List<SignatureNode>();
    /**
    * The name of this type literal for debugging
    */
    private String __Name = new String();
    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    public TypeNode(Token token, ParseNode source) throws Exception {
        super(token, source);
        setName("Anonymous");
    }

    /**
    * The body of this type literalThis property gets the value of the field body
    */
    public List<SignatureNode> getBody() throws Exception {
        return body;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "Type:");
        tw.WriteLine(prefix + "  Methods:");
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ meth : body)
        {
            meth.DebugPrint(tw, prefix + "    ");
        }
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ ret = new GraceType(getName());
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ n : body)
            ret.Add(n);
        return ret;
    }

    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>{ { "signatures", new DelegateMethodTyped0<TypeNode>(mSignatures) } };
    /**
    * 
    */
    protected void addMethods() throws Exception {
        AddMethods(sharedMethods);
    }

    private static GraceObject mSignatures(TypeNode self) throws Exception {
        return GraceVariadicList.Of(self.body);
    }

}


