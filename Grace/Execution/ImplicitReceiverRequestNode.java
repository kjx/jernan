//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.TranslationContext;
import Grace.Parsing.Token;
import som.compiler.MixinBuilder;
import som.compiler.Variable.Local;
import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.vmobjects.SSymbol;
import Grace.Parsing.ParseNode;

import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import Grace.StringSupport;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

/**
* A method request with no syntactic receiver
*/
public class ImplicitReceiverRequestNode  extends RequestNode 
{
    public ImplicitReceiverRequestNode(Token location, ParseNode source)  {
        super(location, source);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "ImplicitReceiverRequest: " + getName());
        tw.println(prefix + "SOMnsName:               " + getSOMnsName());

        if (parts.size() == 1)
        {
            if (parts.get(0).getArguments().size() == 0 && parts.get(0).getGenericArguments().size() == 0)
                return ;
             
        }
         
        tw.println(prefix + "  Parts:");
        int i = 1;
        for (Object __dummyForeachVar2 : parts)
        {
            RequestPartNode p = (RequestPartNode)__dummyForeachVar2;
            String partName = p.getName();
            tw.println(prefix + "    Part " + i + ": ");
            tw.println(prefix + "      Name: " + p.getName());
            if (p.getGenericArguments().size() != 0)
            {
                tw.println(prefix + "      Generic arguments:");
                for (Object __dummyForeachVar0 : p.getGenericArguments())
                {
                    Node arg = (Node)__dummyForeachVar0;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            if (p.getArguments().size() != 0)
            {
                tw.println(prefix + "      Arguments:");
                for (Object __dummyForeachVar1 : p.getArguments())
                {
                    Node arg = (Node)__dummyForeachVar1;
                    arg.debugPrint(tw,prefix + "        ");
                }
            }
             
            i++;
        }
    }
    

    public ExpressionNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

        //System.out.println("ImplicitRR.trans(" + getSOMnsName() + ")");
    	if (getSOMnsName().endsWith(":=")) {
 	   //Kernan parse tree for assignments is FUCKED! FUCKED! FUCKED!
    		assert parts.size() == 2;
    		assert parts.get(1).getArguments().size() == 1;
    		return tc.methodBuilder.getGraceSetterSend(getSOMnsName(), parts.get(1).getArguments().get(0).trans(tc), source);
    	}	    	  

        //System.out.println("ImplicitRR.trans() not assignment");
    	SSymbol selector = symbolFor(getSOMnsName());
    	List<ExpressionNode> subs = new ArrayList<>(parts.size());
    	for (RequestPartNode part : parts) {
    		for (Node arg : part.getArguments()) {
    			subs.add(arg.trans(tc));
    		}
    	}
    	return tc.methodBuilder.getImplicitReceiverSend(selector, source);
    }


}


