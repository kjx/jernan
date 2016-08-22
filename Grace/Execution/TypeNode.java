//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import Grace.SOMBridge;
import Grace.TranslationContext;
import Grace.Parsing.ParseNode;
import Grace.Parsing.Token;
import som.VM;
import som.compiler.AccessModifier;
import som.interpreter.nodes.ExpressionNode;
import som.vmobjects.SSymbol;

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

    public ExpressionNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

     System.out.println("KJX found a type node, ignoring it, really should crash");
        
        return tc.methodBuilder.getSelfRead(source);
   }
    
    
}


