//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.AnnotationsNode;
import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.SignatureNode;
import Grace.Execution.SignaturePartNode;

/**
* A method signature
*/
public class SignatureNode  extends Node implements IEnumerable<SignaturePartNode>
{
    private String _name = new String();
    /**
    * Name of the method
    */
    public String getName() throws Exception {
        if (_name == null)
        {
            _name = String.Join(" ");
        }
         
        return _name;
    }

    /**
    * Parts of the method name
    */
    private IList<SignaturePartNode> __Parts = new IList<SignaturePartNode>();
    public IList<SignaturePartNode> getParts() {
        return __Parts;
    }

    public void setParts(IList<SignaturePartNode> value) {
        __Parts = value;
    }

    /**
    * The return type of this method.
    */
    private Node __ReturnType;
    public Node getReturnType() {
        return __ReturnType;
    }

    public void setReturnType(Node value) {
        __ReturnType = value;
    }

    /**
    * True if this signature is an exact list of literal parts.
    */
    public boolean Linear = true;
    /**
    * All "is" annotations on this signature.
    */
    private AnnotationsNode __Annotations;
    public AnnotationsNode getAnnotations() {
        return __Annotations;
    }

    public void setAnnotations(AnnotationsNode value) {
        __Annotations = value;
    }

    public SignatureNode(Token location, SignatureParseNode source) throws Exception {
        super(location, source);
        setParts(new List<SignaturePartNode>());
        setAnnotations(new AnnotationsNode(location, source != null ? source.Annotations : null));
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        tw.WriteLine(prefix + "Signature: " + getName());
        if (getAnnotations() != null && getAnnotations().getCount() > 0)
        {
            tw.WriteLine(prefix + "  Annotations:");
            getAnnotations().debugPrint(tw,prefix + "    ");
            tw.WriteLine(prefix + "  Parts:");
        }
         
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : getParts())
            p.DebugPrint(tw, prefix + "    ");
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        return null;
    }

    /**
    * Add a part to this method name
    */
    public void addPart(SignaturePartNode spn) throws Exception {
        getParts().Add(spn);
        if (!(spn instanceof OrdinarySignaturePartNode))
            Linear = false;
         
    }

    /**
    * Get an enumerator giving each part of this signature in turn.
    */
    public IEnumerator<SignaturePartNode> getEnumerator() throws Exception {
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : getParts())
        {
        }
    }

    /**
    * Get an enumerator giving each part of this signature in turn.
    */
    System.Collections.IEnumerator system___Collections___IEnumerable___GetEnumerator() throws Exception {
        return getEnumerator();
    }

    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>{ { "parts", new DelegateMethodTyped0<SignatureNode>(mParts) }, { "returnType", new DelegateMethodTyped0<SignatureNode>(mReturnType) } };
    /**
    * 
    */
    protected void addMethods() throws Exception {
        AddMethods(sharedMethods);
    }

    private static GraceObject mParts(SignatureNode self) throws Exception {
        return GraceVariadicList.Of(self.getParts());
    }

    private static GraceObject mReturnType(SignatureNode self) throws Exception {
        if (self.getReturnType() != null)
            return self.getReturnType();
         
        return new ImplicitNode("Unknown",self.getOrigin());
    }

}


