//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import som.interpreter.nodes.ExpressionNode;
import Grace.Parsing.ParseNode;
import Grace.MethodHelper;
import Grace.TranslationContext;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import java.util.Collection;


import Grace.Execution.ImplicitReceiverRequestNode;
import Grace.Execution.InheritsNode;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.SignatureNode;

/**
* An inherits clause
*/
public class InheritsNode  extends Node 
{
    /**
    * The request that is being inherited
    */
    private Node __From;
    public Node getFrom() {
        return __From;
    }

    public void setFrom(Node value) {
        __From = value;
    }

    /**
    * List of aliases on this inherits statement.
    */
    private Map<String, SignatureNode> __Aliases;
    public Map<String, SignatureNode> getAliases() {
        return __Aliases;
    }

    public void setAliases(Map<String, SignatureNode> value) {
        __Aliases = value;
    }

    /**
    * List of excludes on this inherits statement.
    */
    private Set<String> __Excludes;
    public Set<String> getExcludes() {
        return __Excludes;
    }

    public void setExcludes(HashSet<String> value) {
        __Excludes = value;
    }

    public InheritsNode(Token location, 
			ParseNode source, 
			Node from, 
			Map<String, SignatureNode> aliases,
			Collection<String> excludes)  {
        super(location, source);
        setFrom(from);
        setAliases(new HashMap<String, SignatureNode>());
	//KJX - why all this copying?
        for (Map.Entry<String,SignatureNode> x : aliases.entrySet())
            getAliases().put(x.getKey(),x.getValue()); //KJX I don't get it
        setExcludes(new HashSet<String>(excludes)); //KJX nor this
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Inherits: ");
        getFrom().debugPrint(tw,prefix + "    ");
        for (Map.Entry<String,SignatureNode> a : getAliases().entrySet())
            tw.println(prefix + "    alias " + a.getKey() + " = " + a.getValue());
        for (String e : getExcludes())
            tw.println(prefix + "    exclude " + e);
    }


    
    public ExpressionNode trans(final TranslationContext tc) {
       System.out.println("KJX translating inherit node");
       
       Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
       SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

       //not doing alias or excludes
       if ((getAliases().size() + getExcludes().size()) > 0 ) {
    		throw new UnsupportedOperationException( 
					"Sorry, cannot yet traslate alias or exclude subclauses to SOMns at " + Location);
       }
    	   
     
       //KJX note if there's more than one inherit node (and I don't check) then only the last one works
  
       //I think what this needs to do is 
       //-set the tc.mxnBuilder.setSuperClassResolution (SCR) to an expression that resolves to the superclass.
       //-and set tc.mxnBuilder.setSuperclassFactorySend (SFS) to an expression that calls that factory
       
       //Things get confused becuase of the enconding of grqce "classes" as SOMNs classes
       //assuming we have class instantiant method builder cimb 
       //
       //  the SCR must be hacked to return the right NS class 
       //  inherit x   -> _class`x.x 
       //  inherit x(y) -> _class`x_.x: y 
       //  inherit e.x(y) -> e._class`x_.x: y 
       //  for inherit  implicit "x"  - we have SCR is cimb.getImplicitReceiverSend(getSOMnsClassName, source);
       //  for inherit explciit e.x - we have SRC is explicit send of getSOMnsCLassName to e 
       //
       //the SFS is *basially* the same as the top-level method (but changed to an *intializername* and sent to *super*)
       // maybee the builder will do that - or maybee not. and we have to build it by hand.
       
       //
       //  OK so I put all the work into the implicit and explcit request nodes via transAsInheritsClauses
       //
       if (! (getFrom() instanceof RequestNode)) {
    	 	throw new UnsupportedOperationException( 	
					"Can only translate inherits clauses that are requests " +
					" to SOMns at " + Location
					 );
       };
       
       ((RequestNode) getFrom()).transAsInheritsClause(tc);
      
       
       //return self. for some reason I don't understand - kjx 
       //ahh. probalby should return "done"
       return tc.methodBuilder.getSelfRead(source);

    }

}


