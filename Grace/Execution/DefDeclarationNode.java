//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import som.interpreter.nodes.ExpressionNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.DefDeclarationParseNode;
import Grace.SOMBridge;
import Grace.TranslationContext;
import Grace.Execution.AnnotationsNode;
import Grace.Execution.DefDeclarationNode;
import Grace.Execution.ImplicitNode;
import Grace.Execution.Node;

import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

/**
* A def declaration
*/
public class DefDeclarationNode  extends Node 
{
    private Node type;
    private DefDeclarationParseNode origin;
    /**
    * The type given to this def declarationThis property gets the value of the field type
    */
    public Node getType()  {
        return type;
    }

    /**
    * Whether this def is annotated public
    */
    private boolean __Public;
    public boolean getPublic() {
        return __Public;
    }

    public void setPublic(boolean value) {
        __Public = value;
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

    public DefDeclarationNode(Token location, DefDeclarationParseNode source, Node val, Node type)  {
        super(location, source);
        this.type = type;
        setValue(val);
        this.origin = source;
        setAnnotations(new AnnotationsNode(source.getAnnotations() == null ? location : source.getAnnotations().getToken(), source.getAnnotations()));
    }

    /**
    * The initial value given in this def declaration
    */
    private Node __Value;
    public Node getValue() {
        return __Value;
    }

    public void setValue(Node value) {
        __Value = value;
    }

    /**
    * The name of this def declarationThis property accesses the name field of the originating
    * parse node
    */
    public String getName()  {
        return (origin.getName() instanceof IdentifierParseNode ? ((IdentifierParseNode)origin.getName()).getName() : "KJX_SHOULD_NOT_HAPPEN");
    }

    /**
    * 
    */

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "DefDeclaration:");
        tw.println(prefix + "  Name:");
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
         
        if (getPublic())
        {
            tw.println(prefix + "  Public: yes");
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

        //potentally can do better here (I think?) by using slot initialisers
        //with the actual value, and setting immutable to true?
        //ALTHOUGH really needs to be redone to catch unitialisation errors
        if (tc.buildingMethod) {
        	tc.methodBuilder.addLocalIfAbsent(getName(), source);
        } else {
    		ExpressionNode slotInitializer = new som.interpreter.nodes.literals.StringLiteralNode("Unitialised def " + getName(), source);
      	    SOMBridge.defSlot(tc.mixinBuilder, 
    	    		getName(),
    				false,
    				SOMBridge.getAccessModifier(! getPublic()),
    				slotInitializer);
        }
        
        
        //translating this node then does the (grace-level) initialisation
        return tc.methodBuilder.getSetterSend(symbolFor(getName()), getValue().trans(tc), source);
   }
    

}


