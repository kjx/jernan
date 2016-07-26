//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.MethodHelper;
import java.io.PrintStream;
import java.util.List;
import java.util.Iterator;


import Grace.Execution.Node;
import Grace.Execution.RequestPartNode;

/**
* A part of a method name and its arguments
*/
public class RequestPartNode //kjx really not a Node?
{
    private String name;
    private String baseName;
    private List<Node> generics;
    private List<Node> arguments;

    public RequestPartNode(String name, List<Node> generics, List<Node> arguments) throws Exception {
        this.baseName = name;
        this.name = MethodHelper.ArityNamePart(name, arguments.size());
        this.generics = generics;
        this.arguments = arguments;
    }

    public RequestPartNode(String name, List<Node> generics, List<Node> arguments, boolean allowArityOverloading) throws Exception {
        this.baseName = name;
        this.name = allowArityOverloading ? MethodHelper.ArityNamePart(name, arguments.size()) : name;
        this.generics = generics;
        this.arguments = arguments;
    }

    /**
    * Make this part into a := bind request part
    */
    public void makeBind()  {
        name += ":=(_)";
    }

    /**
    * The name of this partThis property gets the string field name
    */
    public String getName()  {
        return name;
    }

    /**
    * The base name of this partThis property gets the string field baseName
    */
    public String getBaseName()  {
        return baseName;
    }

    /**
    * Generic arguments to this partThis property gets the field generics
    */
    public List<Node> getGenericArguments()  {
        return generics;
    }

    /**
    * Ordinary arguments to this partThis property gets the field arguments
    */
    public List<Node> getArguments() throws Exception {
        return arguments;
    }

}


