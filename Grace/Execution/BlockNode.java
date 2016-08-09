//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import som.compiler.AccessModifier;
import som.compiler.MethodBuilder;
import som.compiler.Lexer.SourceCoordinate;
import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.literals.BlockNode.BlockNodeWithContext;
import som.vmobjects.SInvokable;
import tools.highlight.Tags.DelimiterClosingTag;
import Grace.Parsing.ParseNode;

import static som.compiler.Symbol.Colon;
import static som.compiler.Symbol.EndBlock;
import static som.compiler.Symbol.Or;
import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import Grace.TranslationContext;
import Grace.Execution.BlockNode;
import Grace.Execution.Node;
import Grace.Execution.ParameterNode;

/**
* A block expression
*/
public class BlockNode  extends Node 
{
    private List<Node> parameters;
    private List<Node> body;
    private Node _forcedPattern;
    private boolean variadic;

    public BlockNode(Token token, ParseNode source, List<Node> parameters, List<Node> body, Node forcedPattern)  {
        super(token, source);
        this.parameters = parameters;
        this.body = body;
        _forcedPattern = forcedPattern;
        for (Node p : parameters)
        {
            ParameterNode param = p instanceof ParameterNode ? (ParameterNode)p : (ParameterNode)null;
            if (param != null)
                variadic |= param.getVariadic();
         
        }
    }

    /**
    * The parameters of this blockThis property gets the value of the field parameters
    */
    public List<Node> getParameters()  {
        return parameters;
    }

    /**
    * The body of this blockThis property gets the value of the field body
    */
    public List<Node> getBody()  {
        return body;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Block:");
        if (_forcedPattern != null)
        {
            tw.println(prefix + "  Pattern:");
            _forcedPattern.debugPrint(tw,prefix + "    ");
        }
         
        tw.println(prefix + "  Parameters:");
        for (Object __dummyForeachVar1 : parameters)
        {
            Node arg = (Node)__dummyForeachVar1;
            arg.debugPrint(tw,prefix + "    ");
        }
        tw.println(prefix + "  Body:");
        for (Object __dummyForeachVar2 : body)
        {
            Node n = (Node)__dummyForeachVar2;
            n.debugPrint(tw,prefix + "    ");
        }
    }

  
    
    public ExpressionNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

        
    	//from Parser primary case NewBlock:
        MethodBuilder builder = new MethodBuilder(tc.methodBuilder);

        //from Parser nestedBlock
        builder.addArgumentIfAbsent("$blockSelf", source);

        
        //from Parser blockArguments
        for (Node pn : parameters)
        {
            ParameterNode p = (ParameterNode) pn;
            builder.addArgumentIfAbsent(p.getName(), source);
            
        }
        
        //builder.debugPrint();
        
        // generate Block signature
        String blockSig = "$blockMethod@0@0KJXEVIL";
        int argSize = builder.getNumberOfArguments();
        for (int i = 1; i < argSize; i++) {
          blockSig += ":";
        }

        builder.setSignature(symbolFor(blockSig));

//KJX Code should now be moved into defDecl & varDecl nodes
//        for (Node n : getBody()) {
//        	if (n instanceof DefDeclarationNode) {
//        		builder.addLocalIfAbsent(((DefDeclarationNode)n).getName(), source);	
//        	}
//        	if (n instanceof VarDeclarationNode) {
//        		builder.addLocalIfAbsent(((VarDeclarationNode)n).getName(), source);	
//        	}
//        }
        
        //builder.debugPrint();
        
        // from blockBody(builder);
                
        TranslationContext blockBodyTC = new TranslationContext(builder, tc.mixinBuilder, true);
//        System.out.println("blockBlodyTC buildingMethod=" + blockBodyTC.buildingMethod);
        List<ExpressionNode> exps = getBody().stream().map(n -> n.trans(blockBodyTC)).collect(Collectors.toList());
        
        //KJX TODO TOO EASY!
        

        //back to Parser primary case NewBlock
        SInvokable blockMethod = builder.assemble(SNodeFactory.createSequence(exps, source),
            AccessModifier.BLOCK_METHOD, null, source);
        builder.addEmbeddedBlockMethod(blockMethod);

        if (builder.requiresContext()) {
          return new BlockNodeWithContext(blockMethod, source);
        } else {
          return new som.interpreter.nodes.literals.BlockNode(blockMethod, source);
        }

    }

}


