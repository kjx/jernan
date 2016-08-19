//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.TranslationContext;
import Grace.Parsing.Token;
import som.compiler.MixinBuilder;
import som.compiler.MethodBuilder;

import som.compiler.Variable.Local;
import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.interpreter.nodes.MessageSendNode.AbstractUninitializedMessageSendNode;
import som.vmobjects.SSymbol;
import Grace.Parsing.ParseNode;

import static som.interpreter.SNodeFactory.createImplicitReceiverSend;
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
    	List<ExpressionNode> args = new ArrayList<>(parts.size());
    	for (RequestPartNode part : parts) {
    		for (Node arg : part.getArguments()) {
    			args.add(arg.trans(tc));
    		}
    	}
    	//methodbuilder.getImplicitSend does more optimisation I think
    	
    	return tc.methodBuilder.getGraceImplicitReceiverSend(selector, args, source);
    }
    
    
    public void transAsInheritsClause(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

        System.out.println("ImplicitRR.transAsInheritsClause(" + getSOMnsName() + ")");
    	if (getSOMnsName().endsWith(":=")) {
    		throw new UnsupportedOperationException( 	
					"You want to INHERIT from an ASSIGNMENT????? - I REFUSE! to IRR.transAsInheristClause to SOMns at " + Location);
					 //I could support this but WHY BOTHER especially in a SPIKE
    	}

        //what we have to do here is
        //1. make implicit call of getSOMnsClassName() -> which gets the SOMns class 
        //2. send that class the factory method 


    	
    	//setup the tc.methodbuilder as tc.mixinBuilder.classInitialisationMethodBuilder 
    	MethodBuilder cimb = tc.mixinBuilder.getClassInstantiationMethodBuilder();
        TranslationContext initTC = new TranslationContext(cimb, tc.mixinBuilder, true);

    	//so: we have to make an implicit send of the munged name to get the fucking class
    	//and install that as setSuperClassResolution
        SSymbol SCselector = symbolFor(getSOMnsClassName()); 
    	ExpressionNode superClassResolution = cimb.getImplicitReceiverSend(SCselector, source);
   	    initTC.mixinBuilder.setSuperClassResolution(superClassResolution);
   	    
   	    System.out.println(" resolving superclass to " + SCselector);
   	    
   	    
   	    //pervert this current send into a factory call on the superclass initialiser (or something)
   	    MethodBuilder imb = tc.mixinBuilder.getInitializerMethodBuilder();
   	    TranslationContext factoryTC = new TranslationContext(imb, tc.mixinBuilder, true);
  	    
    	SSymbol initializerName = symbolFor(getSOMnsName());
    	System.out.println(" attempting to build factory send  " + initializerName);

    	//stuff cming in from Parser inheritance Clause

    	List<ExpressionNode> args = new ArrayList<>(parts.size() + 1);
    	ExpressionNode receiver = factoryTC.methodBuilder.getSuperReadNode(source);
    	//would be better just to add the reciever in at the start here, but...
    	for (RequestPartNode part : parts) {
    		for (Node arg : part.getArguments()) {
    			args.add(arg.trans(factoryTC));
    		}
    	}

    	initializerName = factoryTC.mixinBuilder.getInitializerName(initializerName);
    	
    	// Originally I wrote: this code seemed to work but now I think it shouldn't
    	// Now I think: that's because I was an idiot.
    	//these factory sends are *super* sends from the *Current* class to invokve the *superclass factory*
    	//which is why they are *implicit*
   	    ExpressionNode	superclassFactorySend  = 
   	    		factoryTC.methodBuilder.getGraceImplicitReceiverSendWithReceiver(
   	    				initializerName, receiver, args, source);    	
    	
    	//install that as the superclassFactorySend
        factoryTC.mixinBuilder.setSuperclassFactorySend(superclassFactorySend, false);
    }
    
}


