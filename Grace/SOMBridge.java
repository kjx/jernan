package Grace;

import static som.compiler.Symbol.Equal;
import static som.compiler.Symbol.Identifier;
import static som.compiler.Symbol.Keyword;
import static som.compiler.Symbol.Or;
import static som.compiler.Symbol.Period;
import static som.compiler.Symbol.SlotMutableAssign;
import static som.interpreter.SNodeFactory.createMessageSend;
import static som.interpreter.SNodeFactory.createSequence;
import static som.vm.Symbols.symbolFor;
import som.compiler.Lexer;
import som.compiler.AccessModifier;
import static som.vm.Symbols.symbolFor;
import som.interpreter.nodes.literals.LiteralNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.oracle.truffle.api.source.SourceSection;

import Grace.TranslationContext;

import com.oracle.truffle.api.source.Source;

import java.io.PrintStream;
import Grace.Parsing.Parser;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.Token;
import Grace.Execution.Node;
import Grace.Execution.ExecutionTreeTranslator;
import Grace.Execution.MethodNode;
import Grace.Execution.ObjectConstructorNode;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.SignatureNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ObjectParseNode;
import Grace.Parsing.OrdinarySignaturePartParseNode;
import Grace.Parsing.ParseNode;
import som.VM;
import som.compiler.MethodBuilder;
import som.compiler.MixinBuilder;
import som.compiler.MixinDefinition;
import som.compiler.Lexer.SourceCoordinate;
import som.compiler.MixinBuilder.MixinDefinitionError;
import som.compiler.Parser.ParseError;
import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.interpreter.nodes.OuterObjectRead;
import som.interpreter.nodes.SequenceNode;
import som.interpreter.nodes.MessageSendNode.AbstractUninitializedMessageSendNode;
import som.interpreter.nodes.literals.StringLiteralNode;
import som.vmobjects.SInvokable;
import som.vmobjects.SSymbol;
import tools.highlight.Tags.DelimiterClosingTag;
import tools.highlight.Tags.KeywordTag;
import tools.highlight.Tags.StatementSeparatorTag;


public class SOMBridge {

    public static MixinDefinition parseForSOM(String filename, File file) throws IOException {
    	System.out.println("KJX in SOMBridge");
//    	if 	("fake.grace".equals(filename)) {
//    		return fakeSOM(helloWorldNode());
//    	}
  	
    	System.out.println("KJX SOMBridge loading " + filename);
    	byte[] bytes = Files.readAllBytes(file.toPath());
    	Parser parser = new Parser(filename, new String(bytes,"UTF-8"));

    	ObjectParseNode graceModule = (ObjectParseNode)parser.parse();
    	Node ast = new ExecutionTreeTranslator().translate(graceModule);

    	ast.debugPrint(new PrintStream(System.out,true), "");

    	System.out.print("KJX SOMBridge returning");
    	return fakeSOM(ast,"graceModule");
 
 }
    
//    public static List<ExpressionNode> helloWorldNode() {
//        SourceCoordinate coord = new SourceCoordinate(1,1,1,1);
//        Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
//        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
//
//        
//    	       // expressions.add(expression(builder));
//            //evaluation(builder) -> primary(builder) -> literal -> litearlString
//            LiteralNode HelloWorldString = new StringLiteralNode("Hello KJX World", source);
//            // messages(builder, HelloWorldString);
//            //unaryMessage(receiver=HelloWorldString, evenutalSend=false);
//            //createMessageSend(selector=, new ExpressionNode[] {receiver=HelloWorldString},
//            //        eventualSend=false, getSource(coord)-source);
//            SSymbol printLnSelector = symbolFor("println");
//            ExpressionNode printlnHellowWorld =
//              MessageSendNode.createMessageSend(printLnSelector, new ExpressionNode[] {HelloWorldString}, source); 
//            ArrayList<ExpressionNode> ret = new ArrayList<ExpressionNode>();
//            ret.add(printlnHellowWorld);
//            return ret;
//    }
//    
    public static MixinDefinition fakeSOM(Node ast, String moduleName)  {
    	
    	System.out.println("KJX starting fakeSOM for " + moduleName);
    	
    	//make a new, top-level class called "fakeSOM" to be the Newspeak Module
        MixinBuilder mxnBuilder = new MixinBuilder(null, AccessModifier.PUBLIC, symbolFor("fakeSOM"));
      
            SourceCoordinate coord = new SourceCoordinate(1,1,1,1);
	        Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
	        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
        		
	        //build the primary factory method for the fakeSOM class
		    MethodBuilder primaryFactory = mxnBuilder.getPrimaryFactoryMethodBuilder();	       
		    primaryFactory.addArgumentIfAbsent("self", source); 	
		    primaryFactory.addArgumentIfAbsent("platform", source);
		    primaryFactory.setSignature(symbolFor("usingPlatform:"));
	  	    mxnBuilder.setupInitializerBasedOnPrimaryFactory(source);
	  	    
  	    MethodBuilder meth = mxnBuilder.getClassInstantiationMethodBuilder();

  	    SSymbol SCselector = symbolFor("Value"); 
   	    ExpressionNode superClassResolution = meth.getImplicitReceiverSend(SCselector, source);
   	    mxnBuilder.setSuperClassResolution(superClassResolution);
        mxnBuilder.setSuperclassFactorySend(
                mxnBuilder.createStandardSuperFactorySend(
                   source), true);
                
        
        MethodBuilder slotIniterBuilder = mxnBuilder.getInitializerMethodBuilder();
        ExpressionNode initer = slotIniterBuilder.getImplicitReceiverSend(symbolFor("platform"),source);
        
        defSlot(mxnBuilder, 
				"platform",
				true,
				AccessModifier.PUBLIC,
	    	    initer);

        
        // initExprs(mxnBuilder);  //decode - can do more later to *initialise* (hopefully instances)
        
   	    mxnBuilder.setInitializerSource(source);
   	    //sideDeclaration(mxnBuilder);  	//decode   //pretty sure these are nested classes and category tags 
        //ARGH sideDeclaration -> category -> methodDeclaration
        //I guess because methods are in categories... (Smalltalk Stylee)???
        final SSymbol category = symbolFor("");  //no category for us
        AccessModifier accessModifier = AccessModifier.PUBLIC;
        MethodBuilder builder = new MethodBuilder(
                mxnBuilder, mxnBuilder.getScopeForCurrentParserPosition());
        // messagePattern(builder); //decode
        builder.addArgumentIfAbsent("self", source);
        // keywordPattern(builder); //decode, for "main:"
        builder.addArgumentIfAbsent("args", source);  //argument() decoded
        builder.setSignature(symbolFor("main:"));
        // ExpressionNode body = methodBlock(builder); //decode
        // ExpressionNode methodBody = blockContents(builder);
        // locals(builder); we have no locals
        // ExpressionNode  blockBody(builder);
 
        ExpressionNode self = builder.getSelfRead(source);
    	ExpressionNode runGraceModule = MessageSendNode.createMessageSend(
				symbolFor(moduleName),  
				(new ExpressionNode[] { self } ), source);
        
    	//KJX the three lines below is old code that just pasted the grace module AST into the body of hte main method.
    	//we are now being more "subtle" 
    	
      	//ObjectConstructorNode graceOC = (ObjectConstructorNode) ast;
      	//Grace.TranslationContext tc = new TranslationContext(builder,mxnBuilder, true);
    	//List<ExpressionNode> expressions = graceOC.getBody().stream().map(n -> n.trans(tc)).collect(Collectors.toList());
  
                
        // the end of the method has been found (EndTerm) - make it implicitly
        // return "self" -- EXCEPT GRACE SHOULDN"T DO THIS! - always return last expression!!
        //ExpressionNode self = builder.getSelfRead(source);
        // expressions.add(self);
        // createSequence(expressions, source);
        // ExpressionNode body = SNodeFactory.createSequence(expressions, source);
        SInvokable myMethod = builder.assemble(runGraceModule, accessModifier, category, source);
        VM.reportNewMethod(myMethod);
          
  	   try {
  	      mxnBuilder.addMethod(myMethod);  //really belongs with code above but throws
  	   } catch (MixinDefinitionError pe) {
 	      VM.errorExit(pe.toString());
  	     return null;
  	    }
  	   
   	    System.out.println("KJX building ersaztsmethod for " + moduleName);
  	    Token tok = new Grace.Parsing.IdentifierToken(moduleName, 1, 1, moduleName);
  	    MethodNode ersaztsmethod = new MethodNode(tok, null);
  	    ersaztsmethod.add(ast);
  	    ersaztsmethod.setFresh(true);
  	    OrdinarySignaturePartParseNode osppn = new OrdinarySignaturePartParseNode(
  	    								new IdentifierParseNode(tok));
  	    SignatureParseNode spn = new SignatureParseNode(tok);
  	    spn.addPart(osppn);
  	    SignatureNode sig = new SignatureNode(tok, spn);
  	    OrdinarySignaturePartNode ospn = new OrdinarySignaturePartNode(tok, osppn, new ArrayList<Node>(), new ArrayList<Node>());
  	    sig.addPart(ospn);
  	    ersaztsmethod.setSignature(sig);
  	    
  	    System.out.println("KJX translating ersaztsmethod " + moduleName);
  	    TranslationContext ersaztsTC = new TranslationContext(slotIniterBuilder,mxnBuilder,true);
  	    ExpressionNode moduleSOMversion = ersaztsmethod.trans(ersaztsTC);

  	    System.out.println("KJX assembling builder fakeSOM");

   	   	MixinDefinition ret = mxnBuilder.assemble(source);
   	
  	    
        System.out.println("KJX returning fakeSOM");
	    return ret; 
       
    }
    
    //init experssion must be build by a methodBuilder which is got by
    //mxnBuilder.getInitializerMethodBuilder 
    public static void defSlot(final MixinBuilder mxnBuilder, 
    							final String slotName,
    							final boolean immutable,
    							final AccessModifier acccessModifier,
    				    	    final ExpressionNode init)
    	       {

    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
	
        	assert !(immutable && (init == null));
        	
        	try {
    	    mxnBuilder.addSlot(symbolFor(slotName), acccessModifier, immutable, init,
    	        source);
    	       } catch (MixinDefinitionError pe) {
    	    	   System.out.println("Defslot Broken");
    	  	      VM.errorExit(pe.toString());
    	   	     return;
    	   	    }
    	       }
    
    public static ExpressionNode graceDone() {
    		Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
    		SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
    		return new som.interpreter.nodes.literals.StringLiteralNode("***KJX***DONE***",source);}
    
    public static ExpressionNode makeReturn(final MethodBuilder builder,
    										final ExpressionNode result) {

    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
	
        	//KJX force everything to be a non-local return
    	    //if (builder.isBlockMethod()) {
    	      return builder.getNonLocalReturn(result, source);
    	    //} else {
    	    //  return exp;
    	    //}	
    }
    
    public static AccessModifier getAccessModifier(boolean isConfidential) {
    	return isConfidential ? AccessModifier.PROTECTED : AccessModifier.PUBLIC;
    }
    
}
