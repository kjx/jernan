//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Grace.Parsing.ParseNode;
import Grace.Parsing.Token;

/**
* A type literal
*/
public class TypeNode  extends Node 
{
    private List<SignatureNode> body = new ArrayList<>();
    /**
    * The name of this type literal for debugging
    */
    private String __Name;
    public String getName() {
        return __Name;
    }

    public void setName(String value) {
        __Name = value;
    }

    public TypeNode(Token token, ParseNode source)  {
        super(token, source);
        setName("Anonymous");
    }

    /**
    * The body of this type literalThis property gets the value of the field body
    */
    public List<SignatureNode> getBody()  {
        return body;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Type:");
        tw.println(prefix + "  Methods:");
        for (SignatureNode meth : body)
        {
            meth.debugPrint(tw, prefix + "    ");
        }
    }

}


