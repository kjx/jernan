//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
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

import Grace.TranslationContext;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

/**
* A method request on the inbuilt prelude
*/
public class PreludeRequestNode  extends RequestNode 
{
    public PreludeRequestNode(Token location, ParseNode source)  {
        super(location, source);
    	System.out.println("AS FAR AS KJX CAN SEE, PreludeRequestNode is never instantiated");

    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "PreludeRequest: " + getName());
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
    	
    	//For now, we put things in the Grace Prelude 
    	//into the NS Object class as methosd beginning with the name "gracePrelude"
    	
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

    	SSymbol selector = symbolFor("gracePrelude" + getSOMnsName());  //EVIL by KJX
    	List<ExpressionNode> subs = new ArrayList<>(parts.size() + 1);
    	
    	ExpressionNode self = tc.methodBuilder.getSelfRead(source);
    	subs.add(self);                       							//EVIL by KJX

    	for (RequestPartNode part : parts) {
    		for (Node arg : part.getArguments()) {
    			subs.add(arg.trans(tc));
    		}
    	}
    	
    	System.out.println("HERE WE GO!!: " + selector.getString());
    	return MessageSendNode.createMessageSend(selector, 
    			subs.toArray(new ExpressionNode[0]), source);     	
    }
}


