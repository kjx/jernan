//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import som.VM;
import som.compiler.AccessModifier;
import som.compiler.MethodBuilder;
import som.compiler.MixinBuilder;
import som.compiler.MixinBuilder.MixinDefinitionError;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.vmobjects.SInvokable;
import som.vmobjects.SSymbol;
import Grace.Parsing.ImportParseNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.StringLiteralParseNode;

import static som.vm.Symbols.symbolFor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import Grace.SOMBridge;
import Grace.TranslationContext;
import Grace.Execution.ImplicitNode;
import Grace.Execution.ImportNode;
import Grace.Execution.Node;

/**
* An import statement
*/
public class ImportNode  extends Node 
{
    private Node type;
    private ImportParseNode origin;
    /**
    * Type annotation of the import statementThis property gets the value of the type field
    */
    public Node getType() {
        return type;
    }

    public ImportNode(Token location, ImportParseNode source, Node type)  {
        super(location, source);
        this.type = type;
        this.origin = source;
    }

    /**
    * Module pathThis property gets the string value of the
    * path field of the originating parse node
    */
    public String getPath()  {
	return ((StringLiteralParseNode) origin.getPath()).getValue();
    }

    /**
    * Bound nameThis property gets the string value of the
    * name field of the originating parse node
    */
    public String getName()  {
	return ((IdentifierParseNode)origin.getName()).getName();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Import:");
        tw.println(prefix + "  Path:");
        tw.println(prefix + "    " + getPath());
        tw.println(prefix + "  As:");
        tw.println(prefix + "    " + getName());
        if (type != null)
        {
            tw.println(prefix + "  Type:");
            type.debugPrint(tw,prefix + "    ");
        }
         
    }
    

    
    public ExpressionNode trans(TranslationContext tc) {
    	Source sourceText = Source.fromText("fake\nfake\nfake\n", "importNode");
        SourceSection source = sourceText.createSection("fake\nfake\nfake\n",1,1,1);

      //should really check it is only at the top level - this will allow imports inside (nested) classes
      //but perhaps we don't care?
        
        if (tc.buildingMethod) {
        	System.out.println("cain't import inside a method"); //but it should still work if we did! 
        	VM.errorExit("KKRUNCH");
        } 
   		
        //tc.mixinbuilder *should* be the grace module class  (i.e. instenace of ersatz)
        //tc.mixinbuild.getOuterBuilder(): should get the builder for the enclosing graceModule thingy 
        
        MixinBuilder whereToStickTheImport;
        
        
        Boolean makeImportInSurroundingObject = true;
        
        if (makeImportInSurroundingObject) {
        	whereToStickTheImport = tc.mixinBuilder.getOuterBuilder();
        } else {
        	whereToStickTheImport = tc.mixinBuilder;
        }
        	
        if (whereToStickTheImport == null) {
           	System.out.println("whereToStickTheImport is null"); //huh 
           	whereToStickTheImport = tc.mixinBuilder;
        	// VM.errorExit("KKRUNCH");
        }
        
            System.out.println("IMP translating import " + getPath() + " as " + getName());
            System.out.println("IMP Putting the import " + whereToStickTheImport);
        	
        	SSymbol selector = symbolFor("loadGraceModule:");
        	List<ExpressionNode> args = new ArrayList<>(1);        	
        	args.add( new som.interpreter.nodes.literals.StringLiteralNode(getPath(), source) );
        	
        	ExpressionNode slotInitializer =  whereToStickTheImport.getInitializerMethodBuilder().getGraceImplicitReceiverSend(selector, args, source);
        	//ExpressionNode slotInitializer =  whereToStickTheImport.getInitializerMethodBuilder().getSelfRead(source);
        	   	

        	Boolean makeImportASlot = false;
        	
            if (makeImportASlot)  {
            	//  if this is in the inner thing, then an inherit statement can't find it
            	//  if this is in the outer thing, then the grace module isn't a fucking NS value  so we crash
            	
        	SOMBridge.defSlot(whereToStickTheImport, 
    	    		getName(),
    				true,
    				AccessModifier.PUBLIC,
    				slotInitializer);
        	} else { 
        	//  we make a METHOD not a SLOT and stick THAT in the  scope
        	//  we rely on the vm caching under loadGraceModule 
        	//        	
        	//        	
            final SSymbol category = symbolFor("");  //no category for us
            AccessModifier accessModifier = AccessModifier.PUBLIC;
            MethodBuilder builder = new MethodBuilder(
            		whereToStickTheImport, whereToStickTheImport.getScopeForCurrentParserPosition());
            // messagePattern(builder); //decode
            builder.addArgumentIfAbsent("self", source);
            builder.setSignature(symbolFor(getName()));
      
        	ExpressionNode methodBody = slotInitializer;
        	
        	SInvokable myMethod = builder.assemble(methodBody, accessModifier, category, source);
            VM.reportNewMethod(myMethod);
              
      	   try {
      	      whereToStickTheImport.addMethod(myMethod);  //really belongs with code above but throws
      	   } catch (MixinDefinitionError pe) {
     	      VM.errorExit(pe.toString());
      	     return null;
      	    }
        	}
        	
        //return self. for some reason I don't understand - kjx 
        //ahh. probalby should return "done"
        return tc.methodBuilder.getSelfRead(source);
   }
    
    	
}


