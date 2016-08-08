//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.Parsing.SignatureParseNode;

import static Grace.Parsing.Lexer.isOperatorCharacter;

import java.io.PrintStream;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import Grace.Execution.AnnotationsNode;
import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.SignatureNode;
import Grace.Execution.SignaturePartNode;

/**
* A method signature
*/
public class SignatureNode  extends Node implements Iterable<SignaturePartNode>
{
    private String _name;
    /**
    * Name of the method
    */
    public String getName() {
        if (_name == null)
        {
            //C# _name = String.Join(" ", from p in _parts select p.Name)

            _name = getParts().stream()
                .map(i -> i.getName())
                .collect(Collectors.joining(" "));
        }
        return _name;
    }

    //name in SOMns format - colon preceeds Parameters except for unary mesages
    public String getSOMnsName() {
    	if ((getParts().size() == 1) && (((OrdinarySignaturePartNode)getParts().get(0)).getParameters().size() == 1)) {
    		if (isOperatorCharacter(getParts().get(0).getName().charAt(0),null)) {
    			//probably wrong test due to prefix operators! 
    				    		return ((OrdinarySignaturePartNode)getParts().get(0)).getBaseName();
    	} }
    StringBuilder sb = new StringBuilder();
    for (SignaturePartNode pp : getParts()) {
    	OrdinarySignaturePartNode p = (OrdinarySignaturePartNode)pp;
    	sb.append(p.getBaseName());
    	if (p.getBaseName() != ":=") {
    		for (Object arg : p.getParameters()) {sb.append(":");}
    	}
    }
    return sb.toString();
    }
    
    //name for SOMns Class if this method is "fresh" (i.e. is a class decl)
    public String getSOMnsClassName() {
    	return "_class`" + getSOMnsName().replace(":", "_");
    }
    
    /**
    * Parts of the method name
    */
    private List<SignaturePartNode> __Parts;
    public List<SignaturePartNode> getParts() {
        return __Parts;
    }

    public void setParts(List<SignaturePartNode> value) {
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

    public SignatureNode(Token location, SignatureParseNode source)  {
        super(location, source);
        setParts(new ArrayList<SignaturePartNode>());
        setAnnotations(new AnnotationsNode(location, source != null ? source.getAnnotations() : null));
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Signature: " + getName());
        if (getAnnotations() != null && getAnnotations().size() > 0)
        {
            tw.println(prefix + "  Annotations:");
            getAnnotations().debugPrint(tw,prefix + "    ");
            tw.println(prefix + "  Parts:");
        }
         
        for (SignaturePartNode p : getParts())
            p.debugPrint(tw, prefix + "    ");
    }

    /**
    * Add a part to this method name
    */
    public void addPart(SignaturePartNode spn)  {
        getParts().add(spn);
        if (!(spn instanceof OrdinarySignaturePartNode))
            Linear = false;
         
    }

    /**
    * Get an enumerator giving each part of this signature in turn.
    */
    public Iterator<SignaturePartNode> iterator() {
	return getParts().iterator();
    }

}


