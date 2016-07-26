//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import Grace.Execution.DefDeclarationNode;
import Grace.Execution.DialectNode;
import Grace.Execution.ImportNode;
import Grace.Execution.InheritsNode;
import Grace.Execution.MethodNode;
import Grace.Execution.Node;
import Grace.Execution.ObjectConstructorNode;
import Grace.Execution.VarDeclarationNode;

/**
* An object constructor expression
*/
public class ObjectConstructorNode  extends Node 
{
    private List<Node> body = new ArrayList<Node>();
    private Map<String, MethodNode> methods = new HashMap<String, MethodNode>();
    private boolean containsInheritance;
    private List<InheritsNode> inheritsStatements = new ArrayList<InheritsNode>();
    private List<DefDeclarationNode> defs = new ArrayList<DefDeclarationNode>();
    private List<VarDeclarationNode> vars = new ArrayList<VarDeclarationNode>();
    private List<Node> imports = new ArrayList<Node>();
    private Set<String> fieldNames = new HashSet<String>();
    private List<Node> statements = new ArrayList<Node>();
    public ObjectConstructorNode(Token token, ParseNode source) throws Exception {
        super(token, source);
    }

    /**
    * Add a new method or statement to the body of this
    * object
    *  @param node Node to add
    */
    public void add(Node node) throws Exception {
        MethodNode meth = node instanceof MethodNode ? (MethodNode)node : (MethodNode)null;
        InheritsNode i = node instanceof InheritsNode ? (InheritsNode)node : (InheritsNode)null;
        DefDeclarationNode d = node instanceof DefDeclarationNode ? (DefDeclarationNode)node : (DefDeclarationNode)null;
        VarDeclarationNode v = node instanceof VarDeclarationNode ? (VarDeclarationNode)node : (VarDeclarationNode)null;
        ImportNode imp = node instanceof ImportNode ? (ImportNode)node : (ImportNode)null;
        DialectNode dialect = node instanceof DialectNode ? (DialectNode)node : (DialectNode)null;
        if (i != null)
        {
            containsInheritance = true;
            inheritsStatements.add(i);
        }
         
        if (d != null)
        {
            defs.add(d);
            fieldNames.add(d.getName());
        }
         
        if (v != null)
        {
            vars.add(v);
            fieldNames.add(v.getName());
            fieldNames.add(v.getName() + " :=(_)");
        }
         
        body.add(node);
        if (imp != null)
        {
            if (imports == null)
                imports = new ArrayList<Node>();
             
            imports.add(imp);
            return ;
        }
         
        if (dialect != null)
        {
            if (imports == null)
                imports = new ArrayList<Node>();
             
            imports.add(dialect);
            return ;
        }
         
        if (meth == null)
            statements.add(node);
        else
        {
            methods.put(meth.getName(),meth);
        } 
    }

    /**
    * The body of this object constructorThis property gets the value of the field body
    */
    public List<Node> getBody() throws Exception {
        return body;
    }

    /**
    * The methods of this object constructorThis property gets the value of the field methods
    */
    public Map<String, MethodNode> getMethods() throws Exception {
        return methods;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "ObjectConstructor:");
        tw.println(prefix + "  Methods:");
        for (Object __dummyForeachVar0 : methods.keySet())
        {
            String mn = (String)__dummyForeachVar0;
            methods.get(mn).debugPrint(tw, prefix + "    ");
        }
        tw.println(prefix + "  Initialisation code:");
        for (Object __dummyForeachVar1 : body)
        {
            Node n = (Node)__dummyForeachVar1;
            n.debugPrint(tw,prefix + "    ");
        }
    }

}


