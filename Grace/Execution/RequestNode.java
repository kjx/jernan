//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import som.interpreter.nodes.ExpressionNode;
import Grace.Parsing.ParseNode;
import static Grace.Parsing.Lexer.isOperatorCharacter;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.lang.StringBuilder;

import Grace.TranslationContext;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

/**
* A method request, either explicit or implicit
*/
public abstract class RequestNode  extends Node implements Iterable<RequestPartNode>
{
    private String name;
    /**
    * The name parts making up this request
    */
    protected List<RequestPartNode> parts = new ArrayList<RequestPartNode>();
    public RequestNode(Token location, ParseNode source)  {
        super(location, source);
        this.parts = new ArrayList<RequestPartNode>();
    }

    /**
    * Make this request into a := bind request
    *  @param val Value to assign
    */
    public void makeBind(Node val)  {
	List<Node> tmp = new ArrayList<Node>();
	tmp.add(val); 
        RequestPartNode rpn = 
	    new RequestPartNode(":=", 
				new ArrayList<Node>(), 
				tmp);
        addPart(rpn);
    }

    /**
    * Add another part to this request
    *  @param part Part to append
    */
    public void addPart(RequestPartNode part)  {
        parts.add(part);
    }

    /**
    * The name of the method being requestedThis property gets the value of the field name
    */
    public String getName()  {
        if (name == null)
        {
            //C# name = String.Join(" ", from x in _parts select x.Name)
            name = parts.stream()
                .map(i -> i.getName())
                .collect(Collectors.joining(" "));
        }
        return name;
    }

    /**
    * Get an enumerator giving each part of this request
    * in turn
    */
    public Iterator<RequestPartNode> iterator()  {
	return parts.iterator();
    }

    //name in SOMns format - colon preceeds arguments except for unary mesages
    public String getSOMnsName() {
    	if ((parts.size() == 1) && (parts.get(0).getArguments().size() == 1)) {
    		if (isOperatorCharacter(parts.get(0).getBaseName().charAt(0),null)) {
    				    		return parts.get(0).getBaseName();
    	} }
    StringBuilder sb = new StringBuilder();
    for (RequestPartNode p : parts) {
    	sb.append(p.getBaseName());
    	if (p.getBaseName() != ":=") {
    		for (Object arg : p.getArguments()) {sb.append(":");}
    	}
    }
    return sb.toString();
    }
    
    public String getSOMnsClassName() {
    	return "_class`" + getSOMnsName().replace(":", "_");
    }
    
    // translate to SOM.ExpressionNode
    public void transAsInheritsClause(TranslationContext tc) {
    	throw new UnsupportedOperationException( 	
						"Sorry, cannot yet transAsInheritsClause " +
						this.getClass().getName() +
						" to SOMns at " + Location
						 );
    }

    
}


