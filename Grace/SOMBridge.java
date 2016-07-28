package Grace;

import static som.compiler.Symbol.Equal;
import static som.compiler.Symbol.Identifier;
import static som.compiler.Symbol.Keyword;
import static som.compiler.Symbol.Or;
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

import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.source.Source;

import java.io.PrintStream;
import Grace.Parsing.Parser;
import Grace.Execution.Node;
import Grace.Execution.ExecutionTreeTranslator;
import Grace.Parsing.ObjectParseNode;
import som.VM;
import som.compiler.MethodBuilder;
import som.compiler.MixinBuilder;
import som.compiler.MixinDefinition;
import som.compiler.Lexer.SourceCoordinate;
import som.compiler.MixinBuilder.MixinDefinitionError;
import som.compiler.Parser.ParseError;
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


public class SOMBridge {

    public static MixinDefinition parseForSOM(String filename, File file) throws IOException { 
    	System.out.println("KJX in SOMBridge");
    	if 	("fake.grace".equals(filename)) {
    		return fakeSOM();
    	}
    	
    	System.out.println("KJX SOMBridge loading " + filename);
    	byte[] bytes = Files.readAllBytes(file.toPath());
    	Parser parser = new Parser(filename, new String(bytes,"UTF-8"));

    	ObjectParseNode graceModule = (ObjectParseNode)parser.parse();
    	Node ast = new ExecutionTreeTranslator().translate(graceModule);

    	ast.debugPrint(new PrintStream(System.out,true), "");

    	return ast.trans();
    }
    
    public static MixinDefinition fakeSOM() {
    	System.out.println("KJX starting fakeSOM");
        MixinBuilder mxnBuilder = new MixinBuilder(null, AccessModifier.PUBLIC, symbolFor("Hello"));
        MethodBuilder primaryFactory = mxnBuilder.getPrimaryFactoryMethodBuilder();
        SourceCoordinate coord = new SourceCoordinate(1,1,1,1);
        Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
        		
        //build the primary factory method		
        primaryFactory.addArgumentIfAbsent("self", source); 	
        MethodBuilder builder = primaryFactory; //make it easier to copy in code
        builder.addArgumentIfAbsent("platform", source);
        builder.setSignature(symbolFor("usingPlatform:"));
  	    mxnBuilder.setupInitializerBasedOnPrimaryFactory(source);

  	    //inheritance List And Or Body 
  	    MethodBuilder meth = mxnBuilder.getClassInstantiationMethodBuilder();

  	    SSymbol SCselector = symbolFor("Value");
   	    ExpressionNode superClassResolution = meth.getImplicitReceiverSend(SCselector, source);
  	  
   	    mxnBuilder.setSuperClassResolution(superClassResolution);

        mxnBuilder.setSuperclassFactorySend(
                mxnBuilder.createStandardSuperFactorySend(
                   source), true);

        //slotDeclarations(mxnBuilder)  //variable slots
                
        // initExprs(mxnBuilder);  //decode - can do more later to *initialise* (hopefully instances)
        
   	    mxnBuilder.setInitializerSource(source);
   	    //sideDeclaration(mxnBuilder);  	//decode   //pretty sure these are nested classes and category tags 
        //ARGH sideDeclaration -> category -> methodDeclaration
        //I guess because methods are in categories... (Smalltalk Stylee)???
        final SSymbol category = symbolFor("");  //no category for us
        AccessModifier accessModifier = AccessModifier.PUBLIC;
        /**CAREFUL**/ builder = new MethodBuilder(
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
        List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
        // expressions.add(expression(builder));
        //evaluation(builder) -> primary(builder) -> literal -> litearlString
        LiteralNode HelloWorldString = new StringLiteralNode("Hello KJX World", source);
        // messages(builder, HelloWorldString);
        //unaryMessage(receiver=HelloWorldString, evenutalSend=false);
        //createMessageSend(selector=, new ExpressionNode[] {receiver=HelloWorldString},
        //        eventualSend=false, getSource(coord)-source);
        SSymbol printLnSelector = symbolFor("println");
        ExpressionNode printlnHellowWorld =
          MessageSendNode.createMessageSend(printLnSelector, new ExpressionNode[] {HelloWorldString}, source); 
        expressions.add(printlnHellowWorld);
        // the end of the method has been found (EndTerm) - make it implicitly
        // return "self"
        ExpressionNode self = builder.getSelfRead(source);
        expressions.add(self);
        // createSequence(expressions, source);
        ExpressionNode body = new SequenceNode(expressions.toArray(new ExpressionNode[0]), source);
        SInvokable myMethod = builder.assemble(body, accessModifier, category, source);
        VM.reportNewMethod(myMethod);
          
   	   MixinDefinition ret;	
  	   try {
  	      mxnBuilder.addMethod(myMethod);  //really belongs with code above but throws

  		   
  		   System.out.println("KJX assembling builder fakeSOM");

  	      ret = mxnBuilder.assemble(source);
  	    } catch (MixinDefinitionError pe) {
 	      VM.errorExit(pe.toString());
  	     return null;
  	    }

  	    
  	    
        System.out.println("KJX returning fakeSOM");
	    return ret; 
       
    }
}
