//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.Node;
import Grace.Execution.StringLiteralNode;

/**
* A string literal
*/
public class StringLiteralNode  extends Node 
{
    private StringLiteralParseNode origin = new StringLiteralParseNode();
    public StringLiteralNode(Token location, StringLiteralParseNode source) throws Exception {
        super(location, source);
        origin = source;
    }

    /**
    * The string value of this literalThis property gets the value field of the
    * originating parse node
    */
    public String getValue() throws Exception {
        return origin.Value;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "String: " + getValue());
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        return GraceString.Create(getValue());
    }

    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>{ { "value", new DelegateMethodTyped0<StringLiteralNode>(mValue) } };
    /**
    * 
    */
    protected void addMethods() throws Exception {
        AddMethods(sharedMethods);
    }

    private static GraceObject mValue(StringLiteralNode self) throws Exception {
        return GraceString.Create(self.getValue());
    }

}


