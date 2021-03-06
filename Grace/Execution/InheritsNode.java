//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.MethodHelper;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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


}


