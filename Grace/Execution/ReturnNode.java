//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.TranslationContext;
import Grace.Parsing.Token;
import som.compiler.Lexer.SourceCoordinate;
import som.interpreter.nodes.ExpressionNode;
import Grace.Parsing.ReturnParseNode;
import java.io.PrintStream;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.ReturnNode;

/**
* A return statement
*/
public class ReturnNode  extends Node 
{
    public ReturnNode(Token location, ReturnParseNode source, Node val)  {
        super(location, source);
        setValue(val);
    }
 
    /**
    * The returned expression
    */
    private Node __Value;
    public Node getValue() {
        return __Value;
    }

    public void setValue(Node value) {
        __Value = value;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Return:");
        if (getValue() != null)
        {
            tw.println(prefix + "  Value:");
            getValue().debugPrint(tw,prefix + "    ");
        }
         
    }
    
    //tried th following. didn't work, dunno why.
    
    public ExpressionNode trans(TranslationContext tc) {
        SourceCoordinate coord = new SourceCoordinate(1,1,1,1);
        Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
       
        ExpressionNode exp = getValue() == null ? Grace.SOMBridge.graceDone() : getValue().trans(tc);
        
    if (tc.methodBuilder.isBlockMethod()) {
        return tc.methodBuilder.getNonLocalReturn(exp, source);
      } else {
    	  System.out.println("KJX CANT RETURN FROM TOP-LEVEL");
    	  return super.trans(tc);
      }
    }
}


