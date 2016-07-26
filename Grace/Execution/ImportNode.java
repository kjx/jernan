//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;


import Grace.Execution.ImplicitNode;
import Grace.Execution.ImportNode;
import Grace.Execution.Node;

/**
* An import statement
*/
public class ImportNode  extends Node 
{
    private Node type;
    private ImportParseNode origin = new ImportParseNode();
    /**
    * Type annotation of the import statementThis property gets the value of the type field
    */
    public Node getType() throws Exception {
        return type;
    }

    public ImportNode(Token location, ImportParseNode source, Node type) throws Exception {
        super(location, source);
        this.type = type;
        this.origin = source;
    }

    /**
    * Module pathThis property gets the string value of the
    * path field of the originating parse node
    */
    public String getPath() throws Exception {
        return (origin.Path instanceof StringLiteralParseNode ? (StringLiteralParseNode)origin.Path : (StringLiteralParseNode)null).Value;
    }

    /**
    * Bound nameThis property gets the string value of the
    * name field of the originating parse node
    */
    public String getName() throws Exception {
        return (origin.Name instanceof IdentifierParseNode ? (IdentifierParseNode)origin.Name : (IdentifierParseNode)null).Name;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Import:");
        tw.println(prefix + "  Path:");
        tw.println(prefix + "    " + getPath());
        tw.println(prefix + "  As:");
        tw.println(prefix + "    " + getName());
        if (type != null)
        {
            tw.println(prefix + "  Type:");
            type.debugPrint(tw,prefix + "    ");
        }
         
    }
}


