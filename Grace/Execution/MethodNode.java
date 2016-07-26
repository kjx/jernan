//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import Grace.Execution.AnnotationsNode;
import Grace.Execution.IdentifierNode;
import Grace.Execution.MethodNode;
import Grace.Execution.Node;
import Grace.Execution.ObjectConstructorNode;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.ParameterNode;
import Grace.Execution.SignatureNode;

/**
* A method declaration
*/
public class MethodNode  extends Node 
{
    private List<Node> body = new ArrayList<Node>();
    /**
    * Signature of this method
    */
    private SignatureNode __Signature;
    public SignatureNode getSignature() {
        return __Signature;
    }

    public void setSignature(SignatureNode value) {
        __Signature = value;
    }

    /**
    * Whether this method is confidential or not
    */
    private boolean __Confidential;
    public boolean getConfidential() {
        return __Confidential;
    }

    public void setConfidential(boolean value) {
        __Confidential = value;
    }

    /**
    * Whether this method is abstract or not
    */
    private boolean __Abstract;
    public boolean getAbstract() {
        return __Abstract;
    }

    public void setAbstract(boolean value) {
        __Abstract = value;
    }

    /**
    * Whether this method returns a fresh object or not
    */
    private boolean __Fresh;
    public boolean getFresh() {
        return __Fresh;
    }

    public void setFresh(boolean value) {
        __Fresh = value;
    }

    /**
    * The annotations on this method (and its signature).
    */
    private AnnotationsNode __Annotations;
    public AnnotationsNode getAnnotations() {
        return __Annotations;
    }

    public void setAnnotations(AnnotationsNode value) {
        __Annotations = value;
    }

    /**
    * Whether this method should be given the user-facing receiver
    * (true) or the concrete part-object on which it was found
    * (false)
    */
//KJX - 
//     private boolean __UseRealReceiver;
//     public boolean getUseRealReceiver() {
//         return __UseRealReceiver;
//     }
//
//     public void setUseRealReceiver(boolean value) {
//         __UseRealReceiver = value;
//     }

    public MethodNode(Token token, ParseNode source) throws Exception {
        super(token, source);
        if (source == null)
            setAnnotations(new AnnotationsNode(token,null));
         
    }

    /**
    * The name of this methodThis property gets the value of the field name
    */
    public String getName() throws Exception {
        return getSignature().getName();
    }

    /**
    * Add a node to the body of this method
    *  @param node Node to add
    */
    public void add(Node node) throws Exception {
        body.add(node);
    }

    /**
    * The body of this methodThis property gets the value of the field body
    */
    public List<Node> getBody() throws Exception {
        return body;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Method: " + getName());
        tw.println(prefix + "  Signature:");
        getSignature().debugPrint(tw,prefix + "    ");
        if (getConfidential())
        {
            tw.println(prefix + "  Is: Confidential");
        }
        else
        {
            tw.println(prefix + "  Is: Public");
        } 
        if (getFresh())
        {
            tw.println(prefix + "  Fresh: Yes");
        }
        else
        {
            tw.println(prefix + "  Fresh: No");
        } 
        tw.println(prefix + "  Body:");
        for (Object __dummyForeachVar0 : body)
        {
            Node n = (Node)__dummyForeachVar0;
            n.debugPrint(tw,prefix + "    ");
        }
    }
}


