//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Grace.Parsing.AnnotationsParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.SignaturePartParseNode;
import Grace.Parsing.Token;
import java.io.PrintStream;


/**
* Declared signature of a method
*/
public class SignatureParseNode  extends ParseNode 
{
    private List<SignaturePartParseNode> _parts = new ArrayList<SignaturePartParseNode>();
    /**
    * Parts in this signature
    * This property gets the value of the string field _parts
    */
    public List<SignaturePartParseNode> getParts() throws Exception {
        return _parts;
    }

    /**
    * Given return type of this method signature
    */
    private ParseNode __ReturnType;
    public ParseNode getReturnType() {
        return __ReturnType;
    }

    public void setReturnType(ParseNode value) {
        __ReturnType = value;
    }

    private String _name;
    /**
    * Name of the method described by this signature
    * This value is computed on demand and cached.
    */
    public String getName() throws Exception {
        if (_name == null)
        {
	    //C# _name = String.Join(" ", from p in _parts select p.Name)

	    _name = _parts.stream()
		.map(i -> i.toString())
		.collect(Collectors.joining(" "));


        }
         
        return _name;
    }

    /**
    * "is" annotations on this method signature.
    */
    private AnnotationsParseNode __Annotations;
    public AnnotationsParseNode getAnnotations() {
        return __Annotations;
    }

    public void setAnnotations(AnnotationsParseNode value) {
        __Annotations = value;
    }

    /**
    * @param t Representative token of this signature
    */
    public SignatureParseNode(Token t) throws Exception {
        super(t);
    }

    /**
    * Add an additional part to this signature
    * 
    *  @param part Part to add
    */
    public void addPart(SignaturePartParseNode part) throws Exception {
        _parts.add(part);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Signature: " + getName());
        for (SignaturePartParseNode p : _parts)
            p.debugPrint(tw,prefix + "    ");
        if (getAnnotations() != null)
        {
            tw.println(prefix + "  Annotations:");
            getAnnotations().debugPrint(tw,prefix + "    ");
        }
         
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


