//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.SOMBridge;
import Grace.Parsing.Token;
import som.VM;
import som.compiler.AccessModifier;
import som.compiler.MethodBuilder;
import som.compiler.MixinBuilder;
import som.compiler.MixinDefinition;
import som.compiler.Lexer.SourceCoordinate;
import som.compiler.MixinBuilder.MixinDefinitionError;
import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.vm.Symbols;
import som.vmobjects.SInvokable;
import som.vmobjects.SSymbol;
import tools.highlight.Tags.KeywordTag;
import Grace.Parsing.Lexer;
import Grace.Parsing.ParseNode;

import static Grace.Parsing.Lexer.isOperatorCharacter;
import static som.interpreter.SNodeFactory.createMessageSend;
import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import java.util.ArrayList;

import Grace.TranslationContext;
import Grace.Execution.AnnotationsNode;
import Grace.Execution.IdentifierNode;
import Grace.Execution.MethodNode;
import Grace.Execution.Node;
import Grace.Execution.ObjectConstructorNode;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.ParameterNode;
import Grace.Execution.SignatureNode;

/**
* A method declaration
*/
public class MethodNode  extends Node 
{
    private List<Node> body = new ArrayList<Node>();
    /**
    * Signature of this method
    */
    private SignatureNode __Signature;
    public SignatureNode getSignature() {
        return __Signature;
    }

    public void setSignature(SignatureNode value) {
        __Signature = value;
    }

    /**
    * Whether this method is confidential or not
    */
    private boolean __Confidential;
    public boolean getConfidential() {
        return __Confidential;
    }

    public void setConfidential(boolean value) {
        __Confidential = value;
    }

    /**
    * Whether this method is abstract or not
    */
    private boolean __Abstract;
    public boolean getAbstract() {
        return __Abstract;
    }

    public void setAbstract(boolean value) {
        __Abstract = value;
    }

    /**
    * Whether this method returns a fresh object or not
    */
    private boolean __Fresh;
    public boolean getFresh() {
        return __Fresh;
    }

    public void setFresh(boolean value) {
        __Fresh = value;
    }

    /**
    * The annotations on this method (and its signature).
    */
    private AnnotationsNode __Annotations;
    public AnnotationsNode getAnnotations() {
        return __Annotations;
    }

    public void setAnnotations(AnnotationsNode value) {
        __Annotations = value;
    }

    /**
    * Whether this method should be given the user-facing receiver
    * (true) or the concrete part-object on which it was found
    * (false)
    */
//KJX - 
//     private boolean __UseRealReceiver;
//     public boolean getUseRealReceiver() {
//         return __UseRealReceiver;
//     }
//
//     public void setUseRealReceiver(boolean value) {
//         __UseRealReceiver = value;
//     }

    public MethodNode(Token token, ParseNode source)  {
        super(token, source);
        if (source == null)
            setAnnotations(new AnnotationsNode(token,null));
         
    }

    /**
    * The name of this methodThis property gets the value of the field name
    */
    public String getName()  {
        return getSignature().getName();
    }

    /**
    * Add a node to the body of this method
    *  @param node Node to add
    */
    public void add(Node node)  {
        body.add(node);
    }

    /**
    * The body of this methodThis property gets the value of the field body
    */
    public List<Node> getBody()  {
        return body;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Method: " + getName());
        tw.println(prefix + "SOMnsName: " + getSignature().getSOMnsName());
        tw.println(prefix + "  Signature:");
        getSignature().debugPrint(tw,prefix + "    ");
        if (getConfidential())
        {
            tw.println(prefix + "  Is: Confidential");
        }
        else
        {
            tw.println(prefix + "  Is: Public");
        } 
        if (getFresh())
        {
            tw.println(prefix + "  Fresh: Yes");
        }
        else
        {
            tw.println(prefix + "  Fresh: No");
        } 
        tw.println(prefix + "  Body:");
        for (Object __dummyForeachVar0 : body)
        {
            Node n = (Node)__dummyForeachVar0;
            n.debugPrint(tw,prefix + "    ");
        }
    }
    
    public boolean isSimpleClass() {
    	return (getBody().size() == 1) 
    			&& (getBody().get(0) instanceof ObjectConstructorNode) 
    			&& ( ! Lexer.isOperatorCharacter(getName().charAt(0), null));
    }
    
  
    
    public ExpressionNode trans(final TranslationContext tc) {
       System.out.println("KJX translating method node - " + getName());
       if (getFresh()) {
    	   if (isSimpleClass()) {
    		   return transAsClass(tc);
    	   } else {
    	       System.out.println("KJX - CANNOT TRANSLATE" + getName() + " is fresh but not a simple class");
    	       super.trans(tc); //Give up
    	   }	
       }
       System.out.println("KJX translating method node - " + getName() + " as method");

       Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
       SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
     
//build a new method and add it into the TC mixing builder
       
       final SSymbol category = symbolFor("");  //no category for us
       AccessModifier accessModifier = SOMBridge.getAccessModifier(getConfidential());;
       MethodBuilder builder = new MethodBuilder(
               tc.mixinBuilder, tc.mixinBuilder.getScopeForCurrentParserPosition());
   
       buildParametersForMethod(builder);
       
       builder.setSignature(symbolFor(getSignature().getSOMnsName()));
       Grace.TranslationContext methodTC = new TranslationContext(builder,tc.mixinBuilder);
   	   List<ExpressionNode> expressions = getBody().stream().map(n -> n.trans(methodTC)).collect(Collectors.toList());       
       ExpressionNode body = SNodeFactory.createSequence(expressions, source);
       SInvokable myMethod = builder.assemble(body, accessModifier, category, source); 
         
 	   try {
 		  tc.mixinBuilder.addMethod(myMethod);  //really belongs with code above but throws
 	   } catch (MixinDefinitionError pe) {
 		   System.out.println("Crashed trying to translate as plain method " + getName());
 	      VM.errorExit(pe.toString());
  	     return null;
 	   }
       
 	   //we've now added the method into the mixin ("class") being built.
 	   //executing a method decln returns done
       return Grace.SOMBridge.graceDone();
    }	
    
    public ExpressionNode transAsClass(TranslationContext tc) {
    	assert isSimpleClass();  //method body is just one ObjectConstructorNode
    	ObjectConstructorNode internalOC = (ObjectConstructorNode)getBody().get(0);
    	List<Node> ocBody = internalOC.getBody();
    	
        System.out.println("KJX translating method node - " + getName() + " as class");

        SourceCoordinate coord = new SourceCoordinate(1,1,1,1);
        Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
        
        //what we have to do
        //1.make a SOMns nestedclass called _class_foo_bar_baz is the NAME of the NS class - foo()bar()baz() is its primary factory.
        //then
        //2.make a SOMns method which invokes the class and runs its constructor
        //foo()bar()baz() -> calls _class_foo_bar_baz (to get the class) .foo()bar()baz()
        
        
        //STUFF FROM THE BEGINNING STARTS HERE
        //STUFF FROM THE BEGINNING STARTS HERE
        //STUFF FROM THE BEGINNING STARTS HERE
        //STUFF FROM THE BEGINNING STARTS HERE
        MixinBuilder mxnBuilder = new MixinBuilder(tc.mixinBuilder, 	
        			SOMBridge.getAccessModifier(getConfidential()), 
        			symbolFor(getSignature().getSOMnsClassName()));
        
        //build the primary factory method for the class
	    MethodBuilder primaryFactory = mxnBuilder.getPrimaryFactoryMethodBuilder();	       
	    buildParametersForMethod(primaryFactory);
	    primaryFactory.setSignature(symbolFor(getSignature().getSOMnsName()));
  	    mxnBuilder.setupInitializerBasedOnPrimaryFactory(source);

  	    // set up inheritance from Object
  	    // KJX need to add in inhertance later
  	    MethodBuilder def = mxnBuilder.getClassInstantiationMethodBuilder();
  	    ExpressionNode selfRead = def.getSelfRead(source);
  	    ExpressionNode superClass = createMessageSend(Symbols.OBJECT,
  	        new ExpressionNode[] {selfRead}, false, source);
  	    mxnBuilder.setSuperClassResolution(superClass);
  	    mxnBuilder.setSuperclassFactorySend(
  	        mxnBuilder.createStandardSuperFactorySend(source), true);

  	    //copied from code in BlockNode and then hacked
  	    //I'm not sure we need to do this, perhaps nodes could just 
  	    //generate themselves into the mxnBuilder via tc,
  	    //and perhaps we need a seperate transInitialiser(tc) method to make initialisers?
  	    //or perhaps eith make transIntoObject and transIntoMethod (or stick something into the tc - a slotBuilder?)


  	    //treats defs and vars the same - I really should generate a "hidden" var slot for a def
  	    //only with an accessor method that reads the hidden var slot; the hidden var slots' writer is used to initialise...
  	    //and if a vars readiability and writeability are not the same, shoudl generate a hidden slot and accessors too
  	    
        System.out.println("KJX making slots for - " + getName() + " as class");

  	    //now make the slots
  	    MethodBuilder initializer = mxnBuilder.getInitializerMethodBuilder();  	   
   	    mxnBuilder.setInitializerSource(source);
  	    for (Node nn : ocBody) {
        	if (nn instanceof DefDeclarationNode) {
        		DefDeclarationNode n = (DefDeclarationNode)nn;
        		ExpressionNode slotInitializer = new som.interpreter.nodes.literals.StringLiteralNode("Unitialised def " + n.getName(), source);
        				
        	    SOMBridge.defSlot(mxnBuilder, 
        	    		n.getName(),
        				false,
        				SOMBridge.getAccessModifier(! n.getPublic()),
        				slotInitializer);
        	}
        	if (nn instanceof VarDeclarationNode) {
        		VarDeclarationNode n = (VarDeclarationNode)nn;
        		ExpressionNode slotInitializer = new som.interpreter.nodes.literals.StringLiteralNode("Unitialised var " + n.getName(), source);

        		SOMBridge.defSlot(mxnBuilder, 
        	    		n.getName(),
        				false,
        				SOMBridge.getAccessModifier(! n.getReadable()),
        				slotInitializer);              	    
        	}
  	    }

  	    //Process the body of the object constructor, running code, adding var inits, returning done for methods...
        Grace.TranslationContext classTC = new TranslationContext(initializer,mxnBuilder);
        for (Node n : ocBody) {
        	mxnBuilder.addInitializerExpression(n.trans(classTC));
        }
        
  	    //finally install the class as a nested class in the enclosing mixinBuilder.
        try {
      	    tc.mixinBuilder.addNestedMixin(mxnBuilder.assemble(source));  //really belongs with code above but throws
  	    } catch (MixinDefinitionError pe) {	
  	    	System.out.println("Crashed trying to install the SOMns class " + getSignature().getSOMnsClassName());
  	    	VM.errorExit(pe.toString());
  	     return null;
  	    }
      
        System.out.println("KJX making class bridge method for - " + getName() + " as class");

  	    //now add the "bridge method" that invokes the class
        final SSymbol category = symbolFor("");  //no category for us
        AccessModifier accessModifier = SOMBridge.getAccessModifier(getConfidential());
        MethodBuilder builder = new MethodBuilder(
                mxnBuilder, mxnBuilder.getScopeForCurrentParserPosition());
	    buildParametersForMethod(builder);
	    builder.setSignature(symbolFor(getSignature().getSOMnsName()));

	    //builder.debugPrint();
	    
	    ///AARGH I'm getting really confused -- KJX AARGH
	    	
	    // body of "bridge method" - generate self call to "getSOMnsClassName()" to get the SOM Class object"
	    ExpressionNode self = builder.getSelfRead(source);
	 	ExpressionNode theSOMnsClass = SNodeFactory.createMessageSend(symbolFor(getSignature().getSOMnsClassName()), new ExpressionNode[] {self},
	            false, source);

	 	//now make a send that sends all the arguments to the new message
	 	List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
	 	expressions.add(theSOMnsClass); 
        for (SignaturePartNode pp : getSignature().getParts()) {  				//should factor this out... 
	    	OrdinarySignaturePartNode p = (OrdinarySignaturePartNode)pp;
	    	for (Node nn : p.getParameters()) {
	    		ParameterNode n = (ParameterNode)nn;
	    		System.out.println("getting " + n.getName());
	    		 expressions.add(builder.getReadNode(n.getName(), source)); 	
	    	}
	    }

        ExpressionNode body = 
        		MessageSendNode.createMessageSend(
        				symbolFor(getSignature().getSOMnsName()),  //which is also the name of the primary Factory method
        				expressions.toArray(new ExpressionNode[0]), source);
 
        SInvokable bridgeMethod = builder.assemble(body, accessModifier, category, source);          
        try {
        	tc.mixinBuilder.addMethod(bridgeMethod);  //really belongs with code above but throws
  	    } catch (MixinDefinitionError pe) {	
  	    	System.out.println("Crashed trying to install the bridge method " + getName());
  	    	VM.errorExit(pe.toString());
  	     return null;
  	    }
//        //STUFF FROM THE END END HERES
//        //STUFF FROM THE END END HERES
//        //STUFF FROM THE END END HERES
//        //STUFF FROM THE END END HERES

  	    
        return Grace.SOMBridge.graceDone();
        
     }


    void buildParametersForMethod(MethodBuilder methodBuilder) {
        Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
        
	    methodBuilder.addArgumentIfAbsent("self", source); 
	    
	    for (SignaturePartNode pp : getSignature().getParts()) {
	    	OrdinarySignaturePartNode p = (OrdinarySignaturePartNode)pp;
	    	for (Node nn : p.getParameters()) {
	    		ParameterNode n = (ParameterNode)nn;
	    		 methodBuilder.addArgumentIfAbsent(n.getName(), source); 	
	    	}
	    }	   
    }
    
}


