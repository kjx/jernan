//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.TranslationContext;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import Grace.Execution.ExplicitReceiverRequestNode;
import Grace.Execution.ImplicitReceiverRequestNode;
import Grace.Execution.Node;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;

import static som.interpreter.SNodeFactory.createMessageSend;
import static som.vm.Symbols.symbolFor;
import som.interpreter.nodes.MessageSendNode;
import som.compiler.MethodBuilder;
import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.vmobjects.SSymbol;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.source.Source;

/**
* A method request with a syntactic receiver
*/
public class ExplicitReceiverRequestNode  extends RequestNode 
{
    private Node receiver;
    public ExplicitReceiverRequestNode(Token location, ParseNode source, Node receiver)  {
        super(location, source);
        this.receiver = receiver;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "ExplicitReceiverRequest: " + getName());
        tw.println(prefix + "SOMnsName:               " + getSOMnsName());
        tw.println(prefix + "  Receiver:");
        receiver.debugPrint(tw,prefix + "    ");
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

    
    public SourceSection source(String s) {
        Source sourceText = Source.fromText("fake\nfake\nfake\n", "ERRr " + s + " " + getName());
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
    	return source;
    }
    
    
    public ExpressionNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

    	SSymbol selector = symbolFor(getSOMnsName());
    	List<ExpressionNode> subs = new ArrayList<>(parts.size() + 1);
    	subs.add(receiver.trans(tc));
    	for (RequestPartNode part : parts) {
    		for (Node arg : part.getArguments()) {
    			subs.add(arg.trans(tc));
    		}
    	}
    	return MessageSendNode.createMessageSend(selector, 
    			subs.toArray(new ExpressionNode[0]), source);     	
    }
    
    public void transAsInheritsClause(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

        System.out.println("ExplicitRR.transAsInheritsClause(" + getSOMnsName() + ")");
  
        if (getSOMnsName().endsWith(":=")) {
    		throw new UnsupportedOperationException( 	
					"You want to INHERIT from an ASSIGNMENT????? - I REFUSE! to ERR.transAsInheristClause to SOMns at " + Location);
					 //I could support this but WHY BOTHER especially in a SPIKE
    	}

         //what we have to do here is
         //0. get the receiver
         //1. send receiver expicit call of getSOMnsClassName() -> which gets the SOMns class 
         //2. send that class the factory method 

        
    	//setup the tc.methodbuilder as tc.mixinBuilder.classInitialisationMethodBuilder 
    	MethodBuilder cimb = tc.mixinBuilder.getClassInstantiationMethodBuilder();
        TranslationContext initTC = new TranslationContext(cimb, tc.mixinBuilder, true);
        
    	//so: we have to make an EXPLICIT send of the munged name to the Receiver to get the fucking class
    	//and install that as setSuperClassResolution
        System.out.println("XXXX KJX trying to find superclassresolution");
        receiver.debugPrint(System.out, "XXXX KJX");
        SSymbol SCselector = symbolFor(getSOMnsClassName()); 
    	ExpressionNode superClassResolution = 
    			 MessageSendNode.createMessageSend(SCselector, 
    					 new ExpressionNode[] {receiver.trans(initTC)}, source);     
   	    initTC.mixinBuilder.setSuperClassResolution(superClassResolution);
   	    System.out.println(" resolving superclass to " + SCselector);
   	    
   	    
   	    //pervert this current send into a factory call on the superclass initialiser (or something)
   	    MethodBuilder imb = tc.mixinBuilder.getInitializerMethodBuilder();
   	    TranslationContext factoryTC = new TranslationContext(imb, tc.mixinBuilder, true);
  	    
    	SSymbol initializerName = symbolFor(getSOMnsName());
    	System.out.println(" attempting to build factory send  " + initializerName);

    	//stuff cming in from Parser inheritance Clause
    	ExpressionNode factoryReceiver = factoryTC.methodBuilder.getSuperReadNode(source);  
    	
    	List<ExpressionNode> args = new ArrayList<>(parts.size());
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
  
    	List<ExpressionNode> iexprs = new ArrayList<>();
    	iexprs.add(createMessageSend(symbolFor("println"), 
	   	    		new ExpressionNode[] { new som.interpreter.nodes.literals.StringLiteralNode("ERR super " + getName() + " start",source("ERR")) },
	   	    		false, source("ERR")  ));
	   	
    	iexprs.add(SNodeFactory.createMessageSend(
    	        initializerName, new ExpressionNode[] {factoryReceiver}, false, source));
    	
//    	//causes endless loops...
//  		iexprs.add(factoryTC.methodBuilder.getGraceImplicitReceiverSendWithReceiver(
//   				initializerName, factoryReceiver, args, source)  );

    	iexprs.add(createMessageSend(symbolFor("println"), 
   	    		new ExpressionNode[] { new som.interpreter.nodes.literals.StringLiteralNode("ERR super " + getName() + " done",source("ERR")) },
   	    		false, source("ERR")  ));
	
    	iexprs.add(factoryTC.methodBuilder.getSelfRead(source("ERR self")));
    	
    	ExpressionNode	superclassFactorySend  = SNodeFactory.createSequence(iexprs, source("ERR SCFS"));
   	    		
   	    		
   	        
   	    

    	//install that as the superclassFactorySend
        factoryTC.mixinBuilder.setSuperclassFactorySend(superclassFactorySend, false);
    }
    
    
}

