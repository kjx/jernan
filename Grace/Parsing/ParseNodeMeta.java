//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

//KJX currently chose NOT to fix. Not uses by jernan so far.

package Grace.Parsing;


import Grace.Parsing.AliasParseNode;
import Grace.Parsing.AnnotationsParseNode;
import Grace.Parsing.BindParseNode;
import Grace.Parsing.BlockParseNode;
import Grace.Parsing.ClassDeclarationParseNode;
import Grace.Parsing.CommentParseNode;
import Grace.Parsing.DefDeclarationParseNode;
import Grace.Parsing.DialectParseNode;
import Grace.Parsing.ExcludeParseNode;
import Grace.Parsing.ExplicitBracketRequestParseNode;
import Grace.Parsing.ExplicitReceiverRequestParseNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ImplicitBracketRequestParseNode;
import Grace.Parsing.ImplicitReceiverRequestParseNode;
import Grace.Parsing.ImportParseNode;
import Grace.Parsing.InheritsParseNode;
import Grace.Parsing.InterpolatedStringParseNode;
import Grace.Parsing.MethodDeclarationParseNode;
import Grace.Parsing.NumberParseNode;
import Grace.Parsing.ObjectParseNode;
import Grace.Parsing.OperatorParseNode;
import Grace.Parsing.ParenthesisedParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.Parser;
import Grace.Parsing.PrefixOperatorParseNode;
import Grace.Parsing.ReturnParseNode;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.SignaturePartParseNode;
import Grace.Parsing.StringLiteralParseNode;
import Grace.Parsing.TraitDeclarationParseNode;
import Grace.Parsing.TypedParameterParseNode;
import Grace.Parsing.TypeParseNode;
import Grace.Parsing.TypeStatementParseNode;
import Grace.Parsing.UsesParseNode;
import Grace.Parsing.VarArgsParameterParseNode;
import Grace.Parsing.VarDeclarationParseNode;
import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;

/**
* Encapsulates access to Grace patterns matching parse nodes
* exposed through proxies.
*/
public class ParseNodeMeta   
{
    private static HashMap<String,GraceObject> parseNodes;
    /**
    * Get dictionary of parse node names to patterns.
    */
    public static HashMap<String,GraceObject> getPatternDict() throws Exception {
        if (parseNodes == null)
            parseNodes = new HashMap<String,GraceObject>{ { "Object", new NativeTypePattern<ObjectParseNode>() }, { "MethodDeclaration", new NativeTypePattern<MethodDeclarationParseNode>() }, { "ClassDeclaration", new NativeTypePattern<ClassDeclarationParseNode>() }, { "TraitDeclaration", new NativeTypePattern<TraitDeclarationParseNode>() }, { "TypeStatement", new NativeTypePattern<TypeStatementParseNode>() }, { "Type", new NativeTypePattern<TypeParseNode>() }, { "Signature", new NativeTypePattern<SignatureParseNode>() }, { "SignaturePart", new NativeTypePattern<SignaturePartParseNode>() }, { "Block", new NativeTypePattern<BlockParseNode>() }, { "VarArgsParameter", new NativeTypePattern<VarArgsParameterParseNode>() }, { "TypedParameter", new NativeTypePattern<TypedParameterParseNode>() }, { "VarDeclaration", new NativeTypePattern<VarDeclarationParseNode>() }, { "DefDeclaration", new NativeTypePattern<DefDeclarationParseNode>() }, { "Annotations", new NativeTypePattern<AnnotationsParseNode>() }, { "Operator", new NativeTypePattern<OperatorParseNode>() }, { "PrefixOperator", new NativeTypePattern<PrefixOperatorParseNode>() }, { "Bind", new NativeTypePattern<BindParseNode>() }, { "Number", new NativeTypePattern<NumberParseNode>() }, { "Identifier", new NativeTypePattern<IdentifierParseNode>() }, { "StringLiteral", new NativeTypePattern<StringLiteralParseNode>() }, { "InterpolatedString", new NativeTypePattern<InterpolatedStringParseNode>() }, { "ImplicitBracketRequest", new NativeTypePattern<ImplicitBracketRequestParseNode>() }, { "ExplicitBracketRequest", new NativeTypePattern<ExplicitBracketRequestParseNode>() }, { "ImplicitReceiverRequest", new NativeTypePattern<ImplicitReceiverRequestParseNode>() }, { "ExplicitReceiverRequest", new NativeTypePattern<ExplicitReceiverRequestParseNode>() }, { "Inherits", new NativeTypePattern<InheritsParseNode>() }, { "Uses", new NativeTypePattern<UsesParseNode>() }, { "Alias", new NativeTypePattern<AliasParseNode>() }, { "Exclude", new NativeTypePattern<ExcludeParseNode>() }, { "Import", new NativeTypePattern<ImportParseNode>() }, { "Dialect", new NativeTypePattern<DialectParseNode>() }, { "Return", new NativeTypePattern<ReturnParseNode>() }, { "Parenthesised", new NativeTypePattern<ParenthesisedParseNode>() }, { "Comment", new NativeTypePattern<CommentParseNode>() } };
         
        return parseNodes;
    }

    private static GraceObject prettyPrinter = new GraceObject();
    private static GraceObject getPrettyPrinter(EvaluationContext ctx) throws Exception {
        String dir = Path.GetDirectoryName(Interpreter.class.Assembly.Location);
        String path = (new File(dir, "pretty_printer.grace")).toString();
        GraceObject ret = new GraceObject();
        LocalScope surrounding = new LocalScope();
        surrounding.AddLocalDef("parseNodes", new DictionaryDataObject(getPatternDict()));
        ctx.Extend(surrounding);
        BufferedReader reader = File.OpenText(path);
        try
        {
            {
                Parser parser = new Parser("pretty_printer",TextReaderSupport.readToEnd(reader));
                ObjectParseNode pt = parser.parse() instanceof ObjectParseNode ? (ObjectParseNode)parser.parse() : (ObjectParseNode)null;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ eMod = (new ExecutionTreeTranslator()).Translate(pt);
                ret = eMod.Evaluate(ctx);
            }
        }
        finally
        {
            if (reader != null)
                Disposable.mkDisposable(reader).dispose();
             
        }
        ctx.Unextend(surrounding);
        return ret;
    }

    /**
    * Format a parse node into a string of Grace source code
    * that generates that parse tree.
    * 
    *  @param ctx 
    * Interpreter in which to execute the pretty-printer.
    * 
    *  @param p 
    * Parse node to pretty-print.
    */
    public static String prettyPrint(EvaluationContext ctx, ParseNode p) throws Exception {
        if (prettyPrinter == null)
            prettyPrinter = getPrettyPrinter(ctx);
         
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ req = MethodRequest.Single("formatParseNode", new GraceObjectProxy(p));
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ r = prettyPrinter.Request(ctx, req);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ gs = r instanceof GraceString ? (GraceString)r : (GraceString)null;
        if (gs == null)
            return "";
         
        return gs.Value.Replace("\u2028", System.getProperty("line.separator"));
    }

    /**
    * Format the body of an object parse node into a string of
    * Grace source code that generates that parse tree.
    * 
    *  @param ctx 
    * Interpreter in which to execute the pretty-printer.
    * 
    *  @param p 
    * Object to pretty-print.
    * 
    *  @param useSemicolons 
    * True to insert semicolons after every statement.
    */
    public static String prettyPrintModule(EvaluationContext ctx, ObjectParseNode p, boolean useSemicolons) throws Exception {
        if (prettyPrinter == null)
            prettyPrinter = getPrettyPrinter(ctx);
         
        MethodRequest req = new MethodRequest();
        if (useSemicolons)
        {
            req = MethodRequest.Single("prettyPrintObjectBodyWithSemicolons", new GraceObjectProxy(p.getBody()));
        }
        else
        {
            req = MethodRequest.Single("prettyPrintObjectBody", new GraceObjectProxy(p.getBody()));
            req[0].Arguments.Add(GraceString.Create(""));
        } 
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ r = prettyPrinter.Request(ctx, req);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ gs = r instanceof GraceString ? (GraceString)r : (GraceString)null;
        if (gs == null)
            return "";
         
        return gs.Value.Replace("\u2028", System.getProperty("line.separator"));
    }

}


