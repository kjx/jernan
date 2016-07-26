//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;


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
    private List<Node> body = new List<Node>();
    private Dictionary<String, MethodNode> methods = new Dictionary<String, MethodNode>();
    private boolean containsInheritance;
    private List<InheritsNode> inheritsStatements = new List<InheritsNode>();
    private List<DefDeclarationNode> defs = new List<DefDeclarationNode>();
    private List<VarDeclarationNode> vars = new List<VarDeclarationNode>();
    private List<Node> imports = new List<Node>();
    private HashSet<String> fieldNames = new HashSet<String>();
    private List<Node> statements = new List<Node>();
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
            inheritsStatements.Add(i);
        }
         
        if (d != null)
        {
            defs.Add(d);
            fieldNames.Add(d.getName());
        }
         
        if (v != null)
        {
            vars.Add(v);
            fieldNames.Add(v.getName());
            fieldNames.Add(v.getName() + " :=(_)");
        }
         
        body.Add(node);
        if (imp != null)
        {
            if (imports == null)
                imports = new List<Node>();
             
            imports.Add(imp);
            return ;
        }
         
        if (dialect != null)
        {
            if (imports == null)
                imports = new List<Node>();
             
            imports.Add(dialect);
            return ;
        }
         
        if (meth == null)
            statements.Add(node);
        else
        {
            methods[meth.getName()] = meth;
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
    public Dictionary<String, MethodNode> getMethods() throws Exception {
        return methods;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "ObjectConstructor:");
        tw.println(prefix + "  Methods:");
        for (Object __dummyForeachVar0 : methods.Keys)
        {
            String mn = (String)__dummyForeachVar0;
            methods[mn].DebugPrint(tw, prefix + "    ");
        }
        tw.println(prefix + "  Initialisation code:");
        for (Object __dummyForeachVar1 : body)
        {
            Node n = (Node)__dummyForeachVar1;
            n.debugPrint(tw,prefix + "    ");
        }
    }

}


