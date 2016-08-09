//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.VarDeclarationParseNode;
import Grace.Parsing.IdentifierParseNode;

import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import som.interpreter.SNodeFactory;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.interpreter.nodes.literals.NilLiteralNode;
import som.vmobjects.SSymbol;
import Grace.SOMBridge;
import Grace.TranslationContext;
import Grace.Execution.AnnotationsNode;
import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;
import Grace.Execution.VarDeclarationNode;

/**
* A var declaration
*/
public class VarDeclarationNode  extends Node 
{
    private Node type;
    private VarDeclarationParseNode origin;
    /**
    * The type given to this var declarationThis property gets the value of the field type
    */
    public Node getType()  {
        return type;
    }

    /**
    * Whether this var is annotated readable
    */
    private boolean __Readable;
    public boolean getReadable() {
        return __Readable;
    }

    public void setReadable(boolean value) {
        __Readable = value;
    }

    /**
    * Whether this var is annotated writable
    */
    private boolean __Writable;
    public boolean getWritable() {
        return __Writable;
    }

    public void setWritable(boolean value) {
        __Writable = value;
    }

    /**
    * The "is" annotations on this declaration.
    */
    private AnnotationsNode __Annotations;
    public AnnotationsNode getAnnotations() {
        return __Annotations;
    }

    public void setAnnotations(AnnotationsNode value) {
        __Annotations = value;
    }

    public VarDeclarationNode(Token location, VarDeclarationParseNode source, Node val, Node type)  {
        super(location, source);
        this.type = type;
        setValue(val);
        this.origin = source;
        setAnnotations(new AnnotationsNode(source.getAnnotations() == null ? location : source.getAnnotations().getToken(), source.getAnnotations()));
    }

    /**
    * The initial value given in this var declaration
    */
    private Node __Value;
    public Node getValue() {
        return __Value;
    }

    public void setValue(Node value) {
        __Value = value;
    }

    /**
    * The name of this var declarationThis property accesses the name field of the originating
    * parse node
    */
    public String getName()  {
        return (origin.getName() instanceof IdentifierParseNode ? ((IdentifierParseNode)origin.getName()).getName() : "KJX_SHOULD_NOT_HAPPEN");
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "VarDeclaration:");
        tw.println(prefix + "  As:");
        tw.println(prefix + "    " + getName());
        if (type != null)
        {
            tw.println(prefix + "  Type:");
            type.debugPrint(tw,prefix + "    ");
        }
         
        if (getAnnotations().size() > 0)
        {
            tw.println(prefix + "  Annotations:");
            getAnnotations().debugPrint(tw,prefix + "    ");
        }
         
        if (getValue() != null)
        {
            tw.println(prefix + "  Value:");
            getValue().debugPrint(tw,prefix + "    ");
        }   
    }

   public ExpressionNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "fake source in SOMBridge.java");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);
//        System.out.println("KJX EVIL DEAD");
//        return new NilLiteralNode(source);

        System.out.println("translating vardecl " + getName() + "buldingMethod=" + tc.buildingMethod);

        if (tc.buildingMethod) {
//            System.out.println("adding as local " + getName());
  
        	tc.methodBuilder.addLocalIfAbsent(getName(), source);
        } else {
//            System.out.println("adding as class slot " + getName());

    		ExpressionNode slotInitializer = new som.interpreter.nodes.literals.StringLiteralNode("Unitialised var " + getName(), source);
      	    SOMBridge.defSlot(tc.mixinBuilder, 
    	    		getName(),
    				false,
    				SOMBridge.getAccessModifier(! getReadable()),
    				slotInitializer);
        }
 
        //actual variables (SOM slots) should have already been created uninitialised
        //translating this node just does the assignment
        if (getValue() != null)
        {
        	return tc.methodBuilder.getSetterSend(symbolFor(getName()), getValue().trans(tc), source);
        }   
        
        return tc.methodBuilder.getSetterSend(symbolFor(getName()), 
        		new som.interpreter.nodes.literals.StringLiteralNode("Unitialised var " + getName(), source), 
        		source);
   }
    
}


