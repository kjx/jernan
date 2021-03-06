//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.ErrorReporting;
import static Grace.ErrorReporting.hash;
import Grace.StringInfo; //KJX evil compatability shit.
import Grace.StringSupport;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import Grace.Parsing.AliasKeywordToken;
import Grace.Parsing.AnnotationsParseNode;
import Grace.Parsing.ArrowToken;
import Grace.Parsing.AsToken;
import Grace.Parsing.BindParseNode;
import Grace.Parsing.BindToken;
import Grace.Parsing.BlockParseNode;
import Grace.Parsing.ClassDeclarationParseNode;
import Grace.Parsing.ClassKeywordToken;
import Grace.Parsing.CloseBracketToken;
import Grace.Parsing.ColonToken;
import Grace.Parsing.CommaToken;
import Grace.Parsing.CommentParseNode;
import Grace.Parsing.CommentToken;
import Grace.Parsing.DefDeclarationParseNode;
import Grace.Parsing.DefKeywordToken;
import Grace.Parsing.DialectKeywordToken;
import Grace.Parsing.DialectParseNode;
import Grace.Parsing.DotToken;
import Grace.Parsing.EndToken;
import Grace.Parsing.ExcludeKeywordToken;
import Grace.Parsing.ExplicitBracketRequestParseNode;
import Grace.Parsing.ExplicitReceiverRequestParseNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.IdentifierToken;
import Grace.Parsing.ImplicitBracketRequestParseNode;
import Grace.Parsing.ImplicitReceiverRequestParseNode;
import Grace.Parsing.ImportKeywordToken;
import Grace.Parsing.ImportParseNode;
import Grace.Parsing.InheritsKeywordToken;
import Grace.Parsing.InheritsParseNode;
import Grace.Parsing.InterpolatedStringParseNode;
import Grace.Parsing.IsKeywordToken;
import Grace.Parsing.LBraceToken;
import Grace.Parsing.Lexer;
import Grace.Parsing.LGenericToken;
import Grace.Parsing.LParenToken;
import Grace.Parsing.MethodDeclarationParseNode;
import Grace.Parsing.MethodKeywordToken;
import Grace.Parsing.NewLineToken;
import Grace.Parsing.NumberParseNode;
import Grace.Parsing.NumberToken;
import Grace.Parsing.ObjectKeywordToken;
import Grace.Parsing.ObjectParseNode;
import Grace.Parsing.OpenBracketToken;
import Grace.Parsing.OperatorParseNode;
import Grace.Parsing.OperatorToken;
import Grace.Parsing.OrdinarySignaturePartParseNode;
import Grace.Parsing.OuterKeywordToken;
import Grace.Parsing.ParenthesisedParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.PrefixOperatorParseNode;
import Grace.Parsing.RBraceToken;
import Grace.Parsing.ReturnKeywordToken;
import Grace.Parsing.ReturnParseNode;
import Grace.Parsing.RGenericToken;
import Grace.Parsing.RParenToken;
import Grace.Parsing.SelfKeywordToken;
import Grace.Parsing.SemicolonToken;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.SignaturePartParseNode;
import Grace.Parsing.SingleEqualsToken;
import Grace.Parsing.SpaceToken;
import Grace.Parsing.StringLiteralParseNode;
import Grace.Parsing.StringToken;
import Grace.Parsing.Token;
import Grace.Parsing.TraitDeclarationParseNode;
import Grace.Parsing.TraitKeywordToken;
import Grace.Parsing.TypedParameterParseNode;
import Grace.Parsing.TypeKeywordToken;
import Grace.Parsing.TypeParseNode;
import Grace.Parsing.TypeStatementParseNode;
import Grace.Parsing.UnknownToken;
import Grace.Parsing.UsesKeywordToken;
import Grace.Parsing.UsesParseNode;
import Grace.Parsing.VarArgsParameterParseNode;
import Grace.Parsing.VarDeclarationParseNode;
import Grace.Parsing.VarKeywordToken;
import java.util.HashMap;
import java.util.List;

/**
* Parser for Grace code
*/
public class Parser   
{
    //KJX THIS IS TOTALLY EEEVIILLL
    private static <T> T First(List<T> list) {return list.get(0); }
    private static <T> T Last(List<T> list) {return list.get(list.size() - 1); }

    private Lexer lexer;
    private String code;
    private int indentColumn = 0;
    private String moduleName = "source code";
    private List<ParseNode> comments;
    private boolean doNotAcceptDelimitedBlock = false;
    /**
    * @param module Module name for debugging
    *  @param code Complete source code of this module
    */
    public Parser(String module, String code)  {
        this.moduleName = module;
        this.code = code;
    }

    /**
    * @param code Complete source code of this module
    */
    public Parser(String code)  {
        this.code = code;
    }

    /**
    * Parse the source code of this instance from the
    * beginning
    *  @return Module object created from the code
    */
    public ParseNode parse()  {
        ObjectParseNode module = new ObjectParseNode(new UnknownToken(moduleName,0,0));
        if (code.length() == 0)
            return module;
         
        List<ParseNode> body = module.getBody();
        lexer = new Lexer(moduleName,this.code);
        Token was = lexer.current;
        while (!lexer.done())
        {
            consumeBlankLines();
            indentColumn = lexer.current.column;
            ParseNode n = parseStatement(StatementLevel.ModuleLevel);
            body.add(n);
            if (lexer.current == was)
            {
                reportError("P1000",lexer.current,"Unknown construct");
                break;
            }
             
            while (lexer.current instanceof NewLineToken)
            lexer.nextToken();
            was = lexer.current;
        }
        return module;
    }

    private void reportError(String code, HashMap<String,String> vars, String localDescription)  {
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, code, vars, localDescription);
    }

    private void reportError(String code, Token t1, HashMap<String,String> vars, String localDescription)  {
        ErrorReporting.ReportStaticError(moduleName, t1.line, code, vars, localDescription);
    }

    private void reportError(String code, Token t1, String localDescription)  {
        HashMap<String, String> vars = new HashMap<String,String>();
        if (StringSupport.equals(code, "P1018") && t1 instanceof EndToken)
        {
            code = "P1001";
            vars.put("expected", "expression");
        }
         
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, code, vars, localDescription);
    }

    private void reportError(String code, String localDescription)  {
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, code, localDescription);
    }

    /**
    * True until a particular kind of token
    * is found, for use in a loopIf EOF is reached, reports an error.
    *  @param start First token that led to this
    * sequence, for use in error reportingToken class to search for
    */
    private <T extends Token>boolean awaiting(Token start, Class<T> token)  {
        if (token.isInstance(lexer.current))
            return false;
         
        if (lexer.current instanceof EndToken)
            ErrorReporting.ReportStaticError(
  	     moduleName, start.line, "P1001",
	     hash("expected", token.getName(),  
		  "found", lexer.current.toString() ), 
	     "Unexpected end of file");
         
        return true;
    }

    /**
    * Report an error if the current token is not
    * a particular kindToken class to expect
    */
    private <T extends Token>void expect(Class<T> token)  {
        if (token.isInstance(lexer.current))
            return ;
         
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, lexer.current instanceof EndToken ? "P1001" : "P1002", hash(), "Expected something else, got " + lexer.current);
    }

    /**
    * Report an error if the current token is not
    * a particular kindToken class to expect
    *  @param expectation Description of what was expected
    * instead
    */
    private <T extends Token>void expect(Class<T> token, String expectation)  {
        if (token.isInstance(lexer.current))
            return ;
         
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, lexer.current instanceof EndToken ? "P1001" : "P1002", hash(), "Expected something else, got " + lexer.current);
    }

    /**
    * Report a specific error if the current token is not
    * a particular kindToken class to expect
    *  @param code Error code to report
    */
    private <T extends Token>void expectWithError(Class<T> token, String code)  {
        if (token.isInstance(lexer.current))
            return ;
         
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, code, hash(), "Expected something else, got " + lexer.current);
    }

    /**
    * Report a specific error if the current token is not
    * a particular kind, with a string expectationToken class to expect
    *  @param code Error code to report
    *  @param expectation Description of what was expected
    * instead
    */
    private <T extends Token>void expectWithError(Class<T> token, String code, String expectation)  {
        if (token.isInstance(lexer.current))
            return ;
         
        if (lexer.current instanceof EndToken)
            code = "P1001";
         
        ErrorReporting.ReportStaticError(moduleName, lexer.current.line, code, hash(), "Expected something else, got " + lexer.current);
    }

    /**
    * Obtain the next meaningful token from the lexer,
    * accounting for indentation rules and comments
    */
    private Token nextToken()  {
        lexer.nextToken();
        if (lexer.current instanceof CommentToken)
        {
            takeComments();
        }
         
        if (lexer.current instanceof NewLineToken)
        {
            // Check for continuation lines
            Token t = lexer.peek();
            if (t.column > indentColumn)
                lexer.nextToken();
             
        }
         
        if (lexer.current instanceof CommentToken)
        {
            takeComments();
        }
         
        return lexer.current;
    }

    private void consumeBlankLines()  {
        while (lexer.current instanceof NewLineToken)
        {
            lexer.nextToken();
        }
    }

    private void skipSpaces()  {
        while (lexer.current instanceof SpaceToken || lexer.current instanceof NewLineToken)
        lexer.nextToken();
    }

    /**
    * Take a comment, if present, attach it to a node,
    * and return that node
    *  @param to Node to attach comment toType of node
    */
    private <T extends ParseNode>T attachComment(T to)  {
        if (lexer.current instanceof CommentToken)
            comments.add(parseComment());
         
        return to;
    }

    /**
    * Attach many comments to a node
    *  @param node Node to attach comments to
    *  @param comments Comment nodes to attach
    */
    private void attachComments(ParseNode node, List<ParseNode> comments)  {
        if (comments.size() == 0)
        {
            return ;
        }
         
        int startAt = 0;
        if (node.getComment() == null)
        {
            ParseNode dest = First(comments);
            node.setComment(dest);
            startAt = 1;
        }
         
        ParseNode append = node.getComment();
        while (append.getComment() != null)
        append = append.getComment();
        for (int i = startAt;i < comments.size();i++)
        {
            ParseNode cur = comments.get(i);
            append.setComment(cur);
            while (append.getComment() != null)
            append = append.getComment();
        }
    }

    private List<ParseNode> prepareComments()  {
        List<ParseNode> orig = comments;
        comments = new ArrayList<ParseNode>();
        return orig;
    }

    private void restoreComments(List<ParseNode> orig)  {
        comments = orig;
    }

    private enum StatementLevel
    {
        ModuleLevel,
        TraitLevel,
        ObjectLevel,
        MethodLevel
    }
    private String describeKeyword(Token t)  {
        String ret = "inline code";
        if (t instanceof VarKeywordToken)
            ret = "var declaration";
        else if (t instanceof DefKeywordToken)
            ret = "def declaration";
        else if (t instanceof MethodKeywordToken)
            ret = "method declaration";
        else if (t instanceof ClassKeywordToken)
            ret = "class declaration";
        else if (t instanceof TraitKeywordToken)
            ret = "trait declaration";
        else if (t instanceof InheritsKeywordToken)
            ret = "inherits statement";
        else if (t instanceof UsesKeywordToken)
            ret = "uses statement";
        else if (t instanceof ImportKeywordToken)
            ret = "import statement";
        else if (t instanceof DialectKeywordToken)
            ret = "dialect declaration";
        else if (t instanceof ReturnKeywordToken)
            ret = "return statement";
                  
        return ret;
    }

    private ParseNode parseStatement(StatementLevel level)  {
        List<ParseNode> origComments = comments;
        comments = new ArrayList<ParseNode>();
        takeLineComments();
        Token start = lexer.current;
        ParseNode ret;
        if (lexer.current instanceof RBraceToken && comments.size() == 0)
        {
            reportError("P1029","Unpaired closing brace found");
        }
         
        boolean notTrait = level != StatementLevel.TraitLevel;
        boolean allowMethods = level != StatementLevel.MethodLevel;
        boolean allowImports = level == StatementLevel.ModuleLevel;
        boolean allowInherits = level == StatementLevel.ModuleLevel || level == StatementLevel.ObjectLevel;
        boolean allowUses = level != StatementLevel.MethodLevel;
        boolean allowReturns = level == StatementLevel.MethodLevel;
        if ((lexer.current instanceof NewLineToken || lexer.current instanceof EndToken || lexer.current instanceof RBraceToken) && comments.size() != 0)
        {
            // Took line comments, followed by a blank
            ret = collapseComments(comments);
            comments = new ArrayList<ParseNode>();
        }
        else if (lexer.current instanceof CommentToken)
            ret = parseComment();
        else if (notTrait && lexer.current instanceof VarKeywordToken)
            ret = parseVarDeclaration();
        else if (notTrait && lexer.current instanceof DefKeywordToken)
            ret = parseDefDeclaration();
        else if (allowMethods && lexer.current instanceof MethodKeywordToken)
            ret = parseMethodDeclaration();
        else if (allowMethods && lexer.current instanceof ClassKeywordToken)
            ret = parseClassDeclaration();
        else if (allowMethods && lexer.current instanceof TraitKeywordToken)
            ret = parseTraitDeclaration();
        else if (allowInherits && lexer.current instanceof InheritsKeywordToken)
            ret = parseInherits();
        else if (allowUses && lexer.current instanceof UsesKeywordToken)
            ret = parseUses();
        else if (allowImports && lexer.current instanceof ImportKeywordToken)
            ret = parseImport();
        else if (allowImports && lexer.current instanceof DialectKeywordToken)
            ret = parseDialect();
        else if (allowReturns && lexer.current instanceof ReturnKeywordToken)
            ret = parseReturn();
        else if (lexer.current instanceof TypeKeywordToken)
            ret = parseTypeStatement();
        else if (notTrait)
        {
            ret = parseExpression();
            if (lexer.current instanceof BindToken)
            {
                nextToken();
                ParseNode expr = parseExpression();
                ret = new BindParseNode(start,ret,expr);
            }
             
        }
        else
        {
            String context = "context";
            switch(level)
            {
                case TraitLevel: 
                    context = "trait";
                    break;
                case ObjectLevel: 
                    context = "object or class";
                    break;
                case MethodLevel: 
                    context = "method body";
                    break;
            
            }
            reportError("P1046",hash(),"May not have  inside ");
            return null;
        }              
        takeSemicolon();
        if (!(lexer.current instanceof NewLineToken || lexer.current instanceof CommentToken || lexer.current instanceof EndToken || lexer.current instanceof RBraceToken))
        {
            if (start.line == lexer.current.line || lexer.current.line == lexer.previous.line)
                reportError("P1004",lexer.current,"Unexpected token after statement.");
            else
                reportError("P1030",lexer.current,"Unexpected continuation token after statement."); 
        }
         
        while (lexer.current instanceof NewLineToken)
        lexer.nextToken();
        attachComments(ret,comments);
        comments = origComments;
        return ret;
    }

    private ParseNode parseVarDeclaration()  {
        Token start = lexer.current;
        nextToken();
        expect(IdentifierToken.class);
        ParseNode name = parseIdentifier();
        ParseNode type = null;
        if (lexer.current instanceof ColonToken)
        {
            type = parseTypeAnnotation();
        }
         
        AnnotationsParseNode annotations = parseAnnotations();
        ParseNode val = null;
        OperatorToken op = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        if (lexer.current instanceof BindToken)
        {
            nextToken();
            val = parseExpression();
        }
        else if (lexer.current instanceof SingleEqualsToken)
        {
            reportError("P1005","var declarations use ':='.");
        }
        else if (op != null && op.getName().startsWith(":="))
        {
            reportError("P1038",
			hash("rest", op.getName().substring(2)),
			":= requires space before prefix operator");
        }
           
        return new VarDeclarationParseNode(start,name,val,type,annotations);
    }

    private ParseNode parseDefDeclaration()  {
        Token start = lexer.current;
        nextToken();
        expect(IdentifierToken.class);
        ParseNode name = parseIdentifier();
        ParseNode type = null;
        if (lexer.current instanceof ColonToken)
        {
            type = parseTypeAnnotation();
        }
         
        AnnotationsParseNode annotations = parseAnnotations();
        if (lexer.current instanceof BindToken)
        {
            reportError("P1006","def declarations use '='.");
        }
         
        expect(SingleEqualsToken.class);
        nextToken();
        ParseNode val = parseExpression();
        return new DefDeclarationParseNode(start,name,val,type,annotations);
    }

    private AnnotationsParseNode parseAnnotations()  {
        if (!(lexer.current instanceof IsKeywordToken))
            return null;
         
        AnnotationsParseNode ret = new AnnotationsParseNode(lexer.current);
        nextToken();
        boolean expecting = true;
        while (lexer.current instanceof IdentifierToken)
        {
            doNotAcceptDelimitedBlock = true;
            ret.addAnnotation(parseExpression());
            doNotAcceptDelimitedBlock = false;
            expecting = false;
            if (lexer.current instanceof CommaToken)
            {
                nextToken();
                expecting = true;
            }
            else
                break; 
        }
        if (expecting)
            reportError("P1037",lexer.current,"Expected annotation.");
         
        return ret;
    }

    private SignaturePartParseNode parseOperatorSignaturePart(Token start, OperatorToken op)  {
        IdentifierParseNode partName = new IdentifierParseNode(op);
        nextToken();
        OrdinarySignaturePartParseNode ret = new OrdinarySignaturePartParseNode(partName);
        List<ParseNode> theseParameters = ret.getParameters();
        if (lexer.current instanceof LParenToken)
        {
            Token lp = lexer.current;
            nextToken();
            parseParameterList(lp,RParenToken.class,theseParameters);
            expect(RParenToken.class);
            nextToken();
        }
         
        if (theseParameters.size() != 1)
        {
            reportError("P1047",op, 
			hash( "op", op.getName() ),
			"Operator needs a parameter.");
        }
         
        ret.setFinal(true);
        return ret;
    }

    private SignaturePartParseNode parsePrefixOperatorSignaturePart(Token start, IdentifierToken prefix)  {
        nextToken();
        expect(OperatorToken.class);
        OperatorToken op = (OperatorToken)lexerCurrent();
        nextToken();
        IdentifierParseNode partName = new IdentifierParseNode(prefix,"prefix" + op.getName());
        OrdinarySignaturePartParseNode ret = new OrdinarySignaturePartParseNode(partName);
        ret.setFinal(true);
        return ret;
    }

    private SignaturePartParseNode parseCircumfixSignaturePart(Token start, IdentifierToken circumfix)  {
        nextToken();
        expect(OpenBracketToken.class);
        OpenBracketToken ob = (OpenBracketToken)lexer.current;
        nextToken();
        IdentifierParseNode partName = new IdentifierParseNode(circumfix,"circumfix" + ob.getName() + ob.getOther());
        OrdinarySignaturePartParseNode ret = new OrdinarySignaturePartParseNode(partName);
        parseParameterList(ob,OpenBracketToken.class,ret.getParameters());
        expectWithError(CloseBracketToken.class,"P1033",ob.getName() + " ... " + ob.getOther());
        CloseBracketToken cb = (CloseBracketToken)lexer.current;
        if (!StringSupport.equals(cb.getName(), ob.getOther()))
        {
            ErrorReporting.ReportStaticError(moduleName, cb.line, "P1033", 
		hash( "expected", ob.getName() + " ... " + ob.getOther(), 
		      "found", cb.getName() ), 
		     "Expected bracket name ${expected}, " + "got ${found}.");
        }
         
        nextToken();
        ret.setFinal(true);
        return ret;
    }

    private void rejectVariadicParameters(List<ParseNode> list)  {
	return;

        for (ParseNode p : list)
        {
            if (p instanceof VarArgsParameterParseNode)
                reportError("P1012",p.getToken(),
			    hash("operator", "*"), 
			    "Unexpected operator in parameter list.");
             
        }
    }

    private SignaturePartParseNode parseFirstSignaturePart(Token start)  {
        OperatorToken op = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        IdentifierToken id = lexer.current instanceof IdentifierToken ? (IdentifierToken)lexer.current : (IdentifierToken)null;
        if (op != null)
            return parseOperatorSignaturePart(start,op);
         
        if (id != null)
        {
            if (StringSupport.equals(id.getName(), "prefix"))
                return parsePrefixOperatorSignaturePart(start,id);
             
            if (StringSupport.equals(id.getName(), "circumfix"))
                return parseCircumfixSignaturePart(start,id);
             
            nextToken();
            OrdinarySignaturePartParseNode ret = new OrdinarySignaturePartParseNode(new IdentifierParseNode(id));
            if (lexer.current instanceof LGenericToken)
            {
                Token l = lexer.current;
                nextToken();
                parseParameterList(l,RGenericToken.class,ret.getGenericParameters());
                expect(RGenericToken.class);
                nextToken();
            }
             
            if (lexer.current instanceof LParenToken)
            {
                Token l = lexer.current;
                nextToken();
                parseParameterList(l,RParenToken.class,ret.getParameters());
                rejectVariadicParameters(ret.getParameters());
                expect(RParenToken.class);
                nextToken();
            }
            else
            {
                ret.setFinal(true);
            } 
            return ret;
        }
         
        expectWithError(IdentifierToken.class, "P1032","method name");
        return null;
    }

    private SignaturePartParseNode parseOrdinarySignaturePart(Token start)  {
        IdentifierToken id = (IdentifierToken)lexer.current;
        OrdinarySignaturePartParseNode ret = new OrdinarySignaturePartParseNode(new IdentifierParseNode(id));
        nextToken();
        if (lexer.current instanceof LGenericToken)
        {
            Token l = lexer.current;
            nextToken();
            parseParameterList(l,RGenericToken.class,ret.getGenericParameters());
            expect(RGenericToken.class);
            nextToken();
        }
         
        expect(LParenToken.class, "parameter list");
        Token lp = lexer.current;
        nextToken();
        parseParameterList(lp,RParenToken.class,ret.getParameters());
        rejectVariadicParameters(ret.getParameters());
        expect(RParenToken.class);
        nextToken();
        return ret;
    }

    private SignaturePartParseNode parseSignaturePart(Token start)  {
        if (lexer.current instanceof IdentifierToken)
            return parseOrdinarySignaturePart(start);
         
        return null;
    }

    private SignaturePartParseNode parseBindSignaturePart(Token start, boolean parametersAreOptional)  {
        IdentifierParseNode id = new IdentifierParseNode((BindToken)lexer.current);
        nextToken();
        OrdinarySignaturePartParseNode ret = new OrdinarySignaturePartParseNode(id);
        if (!parametersAreOptional || lexer.current instanceof LParenToken)
        {
            expect(LParenToken.class, "parameter list");
            Token lp = lexer.current;
            nextToken();
            parseParameterList(lp,RParenToken.class,ret.getParameters());
            expect(RParenToken.class);
            nextToken();
        }
         
        ret.setFinal(true);
        return ret;
    }

    private SignatureParseNode parseSignature(Token start, boolean parametersAreOptional)  {
        SignatureParseNode ret = new SignatureParseNode(start);
        Token first = lexer.current;
        SignaturePartParseNode part = parseFirstSignaturePart(start);
        ret.addPart(part);
        if (lexer.current instanceof BindToken)
        {
            if (first instanceof IdentifierToken || first instanceof OpenBracketToken)
            {
                part = parseBindSignaturePart(start,parametersAreOptional);
                ret.addPart(part);
            }
            else
                expect(LParenToken.class, "return type"); 
        }
         
        while (!part.getFinal())
        {
            part = parseSignaturePart(start);
            if (part == null)
                break;
             
            ret.addPart(part);
        }
        if (lexer.current instanceof ArrowToken)
        {
            nextToken();
            doNotAcceptDelimitedBlock = true;
            ret.setReturnType(parseExpression());
            doNotAcceptDelimitedBlock = false;
        }
         
        ret.setAnnotations(parseAnnotations());
        return ret;
    }

    private ParseNode parseMethodDeclaration()  {
        Token start = lexer.current;
        nextToken();
        MethodDeclarationParseNode ret = new MethodDeclarationParseNode(start);
        ret.setSignature(parseSignature(start,false));
        expect(LBraceToken.class);
        List<ParseNode> origComments = prepareComments();
        parseBraceDelimitedBlock(ret.getBody(),StatementLevel.MethodLevel);
        attachComments(ret,comments);
        restoreComments(origComments);
        return ret;
    }

    private ParseNode parseClassDeclaration()  {
        Token start = lexer.current;
        nextToken();
        if (!(lexer.current instanceof IdentifierToken || lexer.current instanceof OperatorToken))
            expect(IdentifierToken.class);
         
        ClassDeclarationParseNode ret = new ClassDeclarationParseNode(start);
        ret.setSignature(parseSignature(start,false));
        expect(LBraceToken.class);
        List<ParseNode> origComments = prepareComments();
        parseBraceDelimitedBlock(ret.getBody(),StatementLevel.ObjectLevel);
        attachComments(ret,comments);
        restoreComments(origComments);
        return ret;
    }

    private ParseNode parseTraitDeclaration()  {
        Token start = lexer.current;
        nextToken();
        expect(IdentifierToken.class);
        TraitDeclarationParseNode ret = new TraitDeclarationParseNode(start);
        ret.setSignature(parseSignature(start,false));
        expect(LBraceToken.class);
        List<ParseNode> origComments = prepareComments();
        parseBraceDelimitedBlock(ret.getBody(),StatementLevel.TraitLevel);
        attachComments(ret,comments);
        restoreComments(origComments);
        return ret;
    }

    private ParseNode parseTypeStatement()  {
        Token start = lexer.current;
        Token peeked = lexer.peek();
        if (peeked instanceof LBraceToken)
            return parseType();
         
        nextToken();
        expectWithError(IdentifierToken.class, "P1034");
        ParseNode name = parseIdentifier();
        List<ParseNode> genericParameters = new ArrayList<ParseNode>();
        if (lexer.current instanceof LGenericToken)
        {
            nextToken();
            while (lexer.current instanceof IdentifierToken)
            {
                genericParameters.add(parseIdentifier());
                if (lexer.current instanceof CommaToken)
                    nextToken();
                 
            }
            if (!(lexer.current instanceof RGenericToken))
                reportError("P1007","Unterminated generic type parameter list.");
             
            nextToken();
        }
        else if (lexer.current instanceof OperatorToken || lexer.current instanceof OpenBracketToken)
        {
            reportError("P1009","Unexpected operator in type name, expected '[['.");
        }
          
        expect(SingleEqualsToken.class);
        nextToken();
        Token ts = lexer.current;
        ParseNode type = null;
        if (lexer.current instanceof TypeKeywordToken || lexer.current instanceof LBraceToken)
        {
            if (lexer.current instanceof TypeKeywordToken)
                nextToken();
             
            List<ParseNode> origComments = prepareComments();
            List<ParseNode> body = parseTypeBody();
            type = new TypeParseNode(ts,body);
            attachComments(type,comments);
            restoreComments(origComments);
        }
        else
        {
            type = parseExpression();
        } 
        type = expressionRest(type);
        return new TypeStatementParseNode(start,name,type,genericParameters);
    }

    private ParseNode parseType()  {
        Token start = lexer.current;
        expect(TypeKeywordToken.class);
        nextToken();
        expect(LBraceToken.class);
        List<ParseNode> origComments = prepareComments();
        List<ParseNode> body = parseTypeBody();
        ParseNode ret = new TypeParseNode(start,body);
        attachComments(ret,comments);
        restoreComments(origComments);
        return ret;
    }

    private List<ParseNode> parseTypeBody()  {
        expect(LBraceToken.class);
        int indentBefore = indentColumn;
        Token start = lexer.current;
        nextToken();
        takeLineComments();
        consumeBlankLines();
        if (lexer.current instanceof RBraceToken)
        {
            nextToken();
            return new ArrayList<ParseNode>();
        }
         
        indentColumn = lexer.current.column;
        if (indentColumn <= indentBefore)
            reportError("P1010", hash(),"Indentation must increase inside {}.");
         
        List<ParseNode> ret = new ArrayList<ParseNode>();
        SignatureParseNode lastSig = null;
        while (awaiting(start,RBraceToken.class))
        {
            List<ParseNode> origComments = prepareComments();
            takeLineComments();
            if (lexer.current instanceof RBraceToken && comments.size() > 0)
            {
                // These can't just be dropped inline into
                // a type, so stick them on the last element
                // of the list.
                attachComments(lastSig,comments);
                restoreComments(origComments);
                break;
            }
             
            SignatureParseNode sig = parseSignature(lexer.current,false);
            takeSemicolon();
            ret.add(sig);
            attachComments(sig,comments);
            restoreComments(origComments);
            consumeBlankLines();
            if (sig.getToken().line == lexer.current.line && lexer.current.line != start.line)
                reportError("P1004",lexer.current,"Unexpected token after statement.");
             
            lastSig = sig;
        }
        lexer.nextToken();
        indentColumn = indentBefore;
        return ret;
    }

    private <Terminator extends Token>void parseParameterList(Token start, Class<Terminator> terminator, List<ParseNode> parameters)  {
        while (awaiting(start,terminator))
        {
            ParseNode param = null;
            if (lexer.current instanceof IdentifierToken)
            {
                Token after = lexer.peek();
                if (after instanceof ColonToken)
                {
                    ParseNode id = parseTerm();
                    ParseNode type = parseTypeAnnotation();
                    param = new TypedParameterParseNode(id,type);
                }
                else if (after instanceof CommaToken || terminator.isInstance(after))
                {
                    param = parseTerm();
                }
                else if (lexer.current instanceof IdentifierToken)
                {
                    param = parseIdentifier();
                }
                else
                {
                    reportError("P1013",after,"In parameter list, expected " + " ',' or end of list.");
                }   
            }
            else if (lexer.current instanceof OperatorToken)
            {
                // This must be varargs
                OperatorToken op = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
                if (!StringSupport.equals("*", op.getName()))
                    reportError("P1012",hash(),"Unexpected operator in parameter list.");
                 
                nextToken();
                expectWithError(IdentifierToken.class, "P1031");
                ParseNode id = parseIdentifier();
                param = id;
                if (lexer.current instanceof ColonToken)
                {
                    ParseNode type = parseTypeAnnotation();
                    param = new TypedParameterParseNode(param,type);
                }
                 
                param = new VarArgsParameterParseNode(param);
                expectWithError(terminator, "P1039");
            }
              
            if (param != null)
                parameters.add(param);
            else
                expectWithError(IdentifierToken.class, "P1031"); 
            if (lexer.current instanceof CommaToken)
                nextToken();
            else if (!(terminator.isInstance(lexer.current)))
            {
                reportError("P1013",
			    hash("token", lexer.current.toString(),
				 "end", Token.describeSubclass()), 
			    "In parameter list, expected " + 
			    " ',' or end of list.");
                break;
            }
              
        }
    }

    private ParseNode parseTypeAnnotation()  {
        expect(ColonToken.class);
        nextToken();
        return parseExpression();
    }

    private ParseNode parseInherits()  {
        Token start = lexer.current;
        nextToken();
        ParseNode val = parseExpression();
        InheritsParseNode ret = new InheritsParseNode(start,val);
        while (lexer.current instanceof AliasKeywordToken || lexer.current instanceof ExcludeKeywordToken)
        {
            Token tok = lexer.current;
            nextToken();
            if (tok instanceof AliasKeywordToken)
            {
                SignatureParseNode newName = parseSignature(lexer.current,true);
                expect(SingleEqualsToken.class);
                nextToken();
                SignatureParseNode oldName = parseSignature(lexer.current,true);
                ret.addAlias(tok,newName,oldName);
            }
            else
            {
                SignatureParseNode name = parseSignature(lexer.current,true);
                ret.addExclude(tok,name);
            } 
        }
        return ret;
    }

    private ParseNode parseUses()  {
        Token start = lexer.current;
        nextToken();
        ParseNode val = parseExpression();
        UsesParseNode ret = new UsesParseNode(start,val);
        while (lexer.current instanceof AliasKeywordToken || lexer.current instanceof ExcludeKeywordToken)
        {
            Token tok = lexer.current;
            nextToken();
            if (tok instanceof AliasKeywordToken)
            {
                SignatureParseNode newName = parseSignature(lexer.current,true);
                expect(SingleEqualsToken.class);
                nextToken();
                SignatureParseNode oldName = parseSignature(lexer.current,true);
                ret.addAlias(tok,newName,oldName);
            }
            else
            {
                SignatureParseNode name = parseSignature(lexer.current,true);
                ret.addExclude(tok,name);
            } 
        }
        return ret;
    }

    private ParseNode parseImport()  {
        Token start = lexer.current;
        nextToken();
        expect(StringToken.class);
        if ((lexer.current instanceof StringToken ? (StringToken)lexer.current : (StringToken)null).getBeginsInterpolation())
            reportError("P1014","Import path uses string interpolation.");
         
        ParseNode path = parseString();
        expect(AsToken.class);
        nextToken();
        expect(IdentifierToken.class);
        ParseNode name = parseIdentifier();
        ParseNode type = null;
        if (lexer.current instanceof ColonToken)
        {
            type = parseTypeAnnotation();
        }
         
        return new ImportParseNode(start,path,name,type);
    }

    private ParseNode parseDialect()  {
        Token start = lexer.current;
        nextToken();
        expect(StringToken.class);
        if ((lexer.current instanceof StringToken ? (StringToken)lexer.current : (StringToken)null).getBeginsInterpolation())
            reportError("P1015","Dialect path uses string interpolation.");
         
        ParseNode path = parseString();
        return new DialectParseNode(start,path);
    }

    private ParseNode parseReturn()  {
        Token start = lexer.current;
        nextToken();
        if (lexer.current instanceof NewLineToken || lexer.current instanceof CommentToken)
        {
            return new ReturnParseNode(start,null);
        }
         
        // Void return
        ParseNode val = parseExpression();
        return new ReturnParseNode(start,val);
    }

    private ParseNode parsePostcircumfixRequest(ParseNode rec)  {
        OpenBracketToken startToken = lexer.current instanceof OpenBracketToken ? (OpenBracketToken)lexer.current : (OpenBracketToken)null;
        List<ParseNode> arguments = new ArrayList<ParseNode>();
        parseBracketConstruct(arguments);
        return new ExplicitBracketRequestParseNode(startToken,startToken.getName() + startToken.getOther(),rec,arguments);
    }

    private ParseNode expressionRestNoOp(ParseNode ex)  {
        ParseNode lhs = ex;
        while (lexer.current instanceof DotToken)
        {
            lhs = parseDotRequest(lhs);
        }
        return lhs;
    }

    private ParseNode maybeParseOperator(ParseNode lhs)  {
        if (lexer.current instanceof OperatorToken)
        {
            lhs = parseOperator(lhs);
        }
         
        return lhs;
    }

    private ParseNode expressionRest(ParseNode lhs)  {
        lhs = expressionRestNoOp(lhs);
        return maybeParseOperator(lhs);
    }

    private void parseBracketConstruct(List<ParseNode> arguments)  {
        OpenBracketToken startToken = lexer.current instanceof OpenBracketToken ? (OpenBracketToken)lexer.current : (OpenBracketToken)null;
        if (startToken != null)
        {
            nextToken();
            while (awaiting(startToken,CloseBracketToken.class))
            {
                ParseNode expr = parseExpression();
                arguments.add(expr);
                consumeBlankLines();
                if (lexer.current instanceof CommaToken)
                {
                    nextToken();
                    if (lexer.current instanceof CloseBracketToken)
                        reportError("P1018",lexer.current,"Expected argument after comma.");
                     
                }
                else
                {
                    expect(CloseBracketToken.class, "CloseBracketToken '" + startToken.getOther() + "'");
                } 
            }
            CloseBracketToken cb = (CloseBracketToken)lexer.current;
            if (!StringSupport.equals(cb.getName(), startToken.getOther()))
            {
                reportError("P1028",
			    hash( "start", startToken.getName(),
				  "expected", startToken.getOther(),
				  "found", cb.getName() ),
			    "Mismatched bracket construct");
            }
             
            nextToken();
        }
         
    }

    private ParseNode parseImplicitBracket()  {
        OpenBracketToken startToken = lexer.current instanceof OpenBracketToken ? (OpenBracketToken)lexer.current : (OpenBracketToken)null;
        List<ParseNode> arguments = new ArrayList<ParseNode>();
        parseBracketConstruct(arguments);
        return new ImplicitBracketRequestParseNode(startToken,startToken.getName() + startToken.getOther(),arguments);
    }

    private ParseNode parseParenthesisedExpression()  {
        if (lexer.current instanceof LParenToken)
        {
            Token startToken = lexer.current;
            nextToken();
            if (lexer.current instanceof RParenToken)
                reportError("P1036",lexer.current,"Empty parentheses.");
             
            ParseNode expr = parseExpression();
            consumeBlankLines();
            if (lexer.current instanceof RParenToken)
            {
                nextToken();
            }
            else
            {
                reportError("P1017","Parenthesised expression does not have closing parenthesis");
            } 
            return new ParenthesisedParseNode(startToken,expr);
        }
         
        return null;
    }

    private ParseNode parseExpressionNoOp()  {
        ParseNode lhs;
        if (lexer.current instanceof LParenToken)
        {
            lhs = parseParenthesisedExpression();
        }
        else if (lexer.current instanceof OpenBracketToken)
        {
            lhs = parseImplicitBracket();
        }
        else if (lexer.current instanceof OuterKeywordToken)
        {
            lhs = new IdentifierParseNode((OuterKeywordToken)lexer.current);
            nextToken();
            if (!(lexer.current instanceof DotToken || lexer.current instanceof OperatorToken))
                expectWithError(DotToken.class, "P1042");
             
        }
        else
        {
            lhs = parseTerm();
        }   
        if (lhs instanceof IdentifierParseNode)
        {
            if (lexer.current instanceof LParenToken)
            {
                lhs = parseImplicitReceiverRequest(lhs);
            }
            else if (hasDelimitedTerm())
            {
                lhs = parseImplicitReceiverRequest(lhs);
            }
            else if (lexer.current instanceof LGenericToken)
            {
                lhs = parseImplicitReceiverRequest(lhs);
            }
               
        }
         
        lhs = expressionRestNoOp(lhs);
        return lhs;
    }

    private ParseNode parseExpression()  {
        ParseNode lhs = parseExpressionNoOp();
        lhs = maybeParseOperator(lhs);
        return lhs;
    }

    private boolean hasDelimitedTerm()  {
        if (lexer.current instanceof NumberToken)
            return true;
         
        if (lexer.current instanceof StringToken)
            return true;
         
        if (lexer.current instanceof LBraceToken && !doNotAcceptDelimitedBlock)
            return true;
         
        if (lexer.current instanceof OpenBracketToken)
            return true;
         
        return false;
    }

    private boolean hasTermStart()  {
        if (lexer.current instanceof IdentifierToken)
            return true;
         
        if (lexer.current instanceof NumberToken)
            return true;
         
        if (lexer.current instanceof StringToken)
            return true;
         
        if (lexer.current instanceof LBraceToken)
            return true;
         
        if (lexer.current instanceof OpenBracketToken)
            return true;
         
        if (lexer.current instanceof TypeKeywordToken)
            return true;
         
        return false;
    }

    private ParseNode parseTerm()  {
        ParseNode ret = null;
        if (lexer.current instanceof IdentifierToken)
        {
            ret = parseIdentifier();
        }
        else if (lexer.current instanceof NumberToken)
        {
            ret = parseNumber();
        }
        else if (lexer.current instanceof StringToken)
        {
            ret = parseString();
        }
        else if (lexer.current instanceof LBraceToken)
        {
            ret = parseBlock();
        }
        else if (lexer.current instanceof ObjectKeywordToken)
        {
            ret = parseObject();
        }
        else if (lexer.current instanceof TypeKeywordToken)
        {
            ret = parseType();
        }
        else if (lexer.current instanceof OperatorToken)
        {
            ret = parsePrefixOperator();
        }
        else if (lexer.current instanceof OpenBracketToken)
        {
            ret = parseImplicitBracket();
        }
        else if (lexer.current instanceof SelfKeywordToken)
        {
            ret = new IdentifierParseNode((SelfKeywordToken)lexer.current);
            nextToken();
        }
                 
        if (ret == null)
        {
            reportError("P1018",lexer.current,"Expected term.");
        }
         
        return ret;
    }

    private ParseNode parsePrefixOperator()  {
        OperatorToken op = lexerCurrent() instanceof OperatorToken ? (OperatorToken)lexerCurrent() : (OperatorToken)null;
        nextToken();
        ParseNode expr;
        if (lexer.current instanceof LParenToken)
        {
            expr = parseParenthesisedExpression();
        }
        else if (lexer.current instanceof OuterKeywordToken)
        {
            expr = new IdentifierParseNode((OuterKeywordToken)lexer.current);
            nextToken();
        }
        else
        {
            expr = parseExpressionNoOp();
        }  
        return new PrefixOperatorParseNode(op,expr);
    }

    private ParseNode parseString()  {
        StringToken tok = lexer.current instanceof StringToken ? (StringToken)lexer.current : (StringToken)null;
        if (tok.getBeginsInterpolation())
        {
            InterpolatedStringParseNode ret = new InterpolatedStringParseNode(tok);
            StringToken lastTok = tok;
            lexer.nextToken();
            while (lastTok.getBeginsInterpolation())
            {
                ret.getParts().add(new StringLiteralParseNode(lastTok));
                lexer.nextToken();
                if (lexer.current instanceof RBraceToken)
                    reportError("P1035",lexer.current,"Empty interpolation.");
                 
                ParseNode expr = parseExpression();
                ret.getParts().add(expr);
                if (lexer.current instanceof RBraceToken)
                {
                    lexer.treatAsString();
                    lastTok = lexer.current instanceof StringToken ? (StringToken)lexer.current : (StringToken)null;
                }
                else
                {
                    reportError("P1019","Interpolation not terminated by }");
                    throw new RuntimeException(); //KJX evil
                } 
                lexer.nextToken();
            }
            ret.getParts().add(new StringLiteralParseNode(lastTok));
            return ret;
        }
        else
        {
            nextToken();
            return new StringLiteralParseNode(tok);
        } 
    }

    private ParseNode parseNumber()  {
        ParseNode ret = new NumberParseNode(lexer.current);
        nextToken();
        return ret;
    }

    private IdentifierParseNode parseIdentifier()  {
        IdentifierParseNode ret = new IdentifierParseNode(lexer.current);
        nextToken();
        return ret;
    }

    private ParseNode parseOperator(ParseNode lhs)  {
        return parseOperatorStream(lhs);
    }

    private ParseNode oldParseOperator(ParseNode lhs)  {
        OperatorToken tok = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        if ((!tok.getSpaceBefore() || !tok.getSpaceAfter()))
            reportError("P1020",hash(),"Infix operators must be surrounded by spaces.");
         
        nextToken();
        ParseNode rhs = parseExpressionNoOp();
        ParseNode ret = new OperatorParseNode(tok,tok.getName(),lhs,rhs);
        tok = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        while (tok != null)
        {
            if ((!tok.getSpaceBefore() || !tok.getSpaceAfter()))
                reportError("P1020",hash(),"Infix operators must be surrounded by spaces.");
             
            nextToken();
            ParseNode comment = null;
            if (lexer.current instanceof CommentToken)
            {
                comment = parseComment();
            }
             
            rhs = parseExpressionNoOp();
            ret = new OperatorParseNode(tok,tok.getName(),ret,rhs);
            ret.setComment(comment);
            tok = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        }
        return ret;
    }

    private static int precedence(String op)  {
        if (StringSupport.equals(op, "*"))
            return 10;
         
        if (StringSupport.equals(op, "/"))
            return 10;
         
        return 0;
    }

    private ParseNode parseOperatorStream(ParseNode lhs)  {
        Stack<OperatorToken> opstack = new Stack<OperatorToken>();
        Stack<ParseNode> valstack = new Stack<ParseNode>();
        valstack.push(lhs);
        OperatorToken tok = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        String firstOp = null;
        boolean allArith = true;
        while (tok != null)
        {
            if ((!tok.getSpaceBefore() || !tok.getSpaceAfter()))
            {
                if (tok.getName().startsWith(":="))
                    reportError("P1038",
				hash("rest", tok.getName().substring(2) ),
				":= needs space before prefix operator");
                 
                reportError("P1020", hash(), 
			    "Infix operators must be surrounded by spaces."); 
            }
             
            nextToken();
            if (lexer.current instanceof CommentToken)
            {
                parseComment();
            }
             
            String __dummyScrutVar1 = tok.getName();
            if (__dummyScrutVar1.equals("*") || __dummyScrutVar1.equals("-") || __dummyScrutVar1.equals("/") || __dummyScrutVar1.equals("+"))
            {
            }
            else
            {
                allArith = false;
            } 
            if (firstOp != null && !allArith && !StringSupport.equals(firstOp, tok.getName()))
            {
                reportError("P1026",hash(),"Mixed operators without parentheses");
            }
            else if (firstOp == null)
            {
                firstOp = tok.getName();
            }
              
            int myprec = precedence(tok.getName());
            while (opstack.size() > 0 && myprec <= precedence(opstack.peek().getName()))
            {
                OperatorToken o2 = opstack.pop();
                ParseNode tmp2 = valstack.pop();
                ParseNode tmp1 = valstack.pop();
                valstack.push(new OperatorParseNode(o2, o2.getName(), tmp1, tmp2));
            }
            opstack.push(tok);
            ParseNode rhs = parseExpressionNoOp();
            valstack.push(rhs);
            tok = lexer.current instanceof OperatorToken ? (OperatorToken)lexer.current : (OperatorToken)null;
        }
        while (opstack.size() > 0)
        {
            OperatorToken o = opstack.pop();
            ParseNode tmp2 = valstack.pop();
            ParseNode tmp1 = valstack.pop();
            valstack.push(new OperatorParseNode(o, o.getName(), tmp1, tmp2));
        }
        return valstack.pop();
    }

    // XXX works around JSIL bug #911
    private Token __lc;
    private Token getlc() {
        return __lc;
    }

    private void setlc(Token value) {
        __lc = value;
    }

    private Token lexerCurrent()  {
        setlc(lexer.current);
        return getlc();
    }

    private void parseBraceDelimitedBlock(List<ParseNode> body, StatementLevel level)  {
        int indentBefore = indentColumn;
        Token start = lexer.current;
        // Skip the {
        lexer.nextToken();
        if (lexer.current instanceof CommentToken)
        {
            comments.add(parseComment());
        }
         
        consumeBlankLines();
        if (lexer.current instanceof RBraceToken)
        {
            nextToken();
            return ;
        }
         
        consumeBlankLines();
        takeLineComments();
        consumeBlankLines();
        if (lexer.current instanceof RBraceToken)
        {
            nextToken();
            return ;
        }
         
        indentColumn = lexer.current.column;
        if (indentColumn <= indentBefore)
            reportError("P1011",hash(),"Indentation must increase inside {}.");
         
        Token lastToken = lexerCurrent();
        while (awaiting(start,RBraceToken.class))
        {
            if (lexer.current.column != indentColumn)
            {
                reportError("P1016",hash(),"Indentation mismatch; is " + (lexer.current.column - 1) + ", should be " + (indentColumn - 1) + ".");
            }
             
            body.add(parseStatement(level));
            if (lexer.current == lastToken)
            {
                reportError("P1000",lexer.current,"Nothing consumed in {} body.");
                break;
            }
             
        }
        nextToken();
        indentColumn = indentBefore;
    }

    private ParseNode parseObject()  {
        ObjectParseNode ret = new ObjectParseNode(lexer.current);
        lexer.nextToken();
        if (!(lexer.current instanceof LBraceToken))
        {
            reportError("P1021","object must have '{' after.");
        }
         
        List<ParseNode> origComments = prepareComments();
        parseBraceDelimitedBlock(ret.getBody(),StatementLevel.ObjectLevel);
        attachComments(ret,comments);
        restoreComments(origComments);
        return ret;
    }

    private void takeSemicolon()  {
        if (lexer.current instanceof SemicolonToken)
        {
            lexer.nextToken();
            if (!(lexer.current instanceof NewLineToken || lexer.current instanceof CommentToken || lexer.current instanceof EndToken || lexer.current instanceof RBraceToken))
                reportError("P1003","Other code cannot follow a semicolon on the same line.");
             
        }
         
    }

    private ParseNode parseBlock()  {
        int indentStart = indentColumn;
        BlockParseNode ret = new BlockParseNode(lexer.current);
        Token start = lexer.current;
        lexer.nextToken();
        consumeBlankLines();
        Token firstBodyToken = lexer.current;
        indentColumn = firstBodyToken.column;
        // TODO fix to handle indentation properly
        // does not at all now. must recalculate after params list too
        if (lexer.current instanceof IdentifierToken || lexer.current instanceof NumberToken || lexer.current instanceof StringToken || lexer.current instanceof LParenToken)
        {
            // It *might* be a parameter.
            ParseNode expr = parseExpression();
            if (lexer.current instanceof BindToken)
            {
                // Definitely not a parameter
                nextToken();
                ParseNode val = parseExpression();
                ret.getBody().add(new BindParseNode(start,expr,val));
                if (lexer.current instanceof CommaToken || lexer.current instanceof ArrowToken)
                    reportError("P1022",lexer.current,"Block parameter list contained invalid symbol.");
                 
                if (lexer.current.line == firstBodyToken.line && !(lexer.current instanceof NewLineToken || lexer.current instanceof RBraceToken))
                {
                    System.out.println("got token " + lexer.current);
                    reportError("P1004",lexer.current,"Unexpected token after statement.");
                }
                 
                takeSemicolon();
            }
            else if (lexer.current instanceof SemicolonToken)
            {
                // Definitely not a parameter
                takeSemicolon();
                ret.getBody().add(expr);
            }
            else if (lexer.current instanceof ColonToken)
            {
                indentColumn = indentStart;
                // Definitely a parameter of some sort, has a type.
                ParseNode type = parseTypeAnnotation();
                ret.getParameters().add(new TypedParameterParseNode(expr,type));
            }
            else if (lexer.current instanceof CommaToken)
            {
                indentColumn = indentStart;
                // Can only be a parameter.
                ret.getParameters().add(expr);
            }
            else if (lexer.current instanceof ArrowToken)
            {
                // End of parameter list
                ret.getParameters().add(expr);
            }
            else
            {
                ret.getBody().add(expr);
                takeSemicolon();
                if (lexer.current.line == firstBodyToken.line && !(lexer.current instanceof NewLineToken || lexer.current instanceof RBraceToken))
                {
                    System.out.println("got token " + lexer.current);
                    reportError("P1004",lexer.current,"Unexpected token after statement.");
                }
                 
            }     
            if (lexer.current instanceof CommaToken)
            {
                nextToken();
                parseParameterList(start, ArrowToken.class, ret.getParameters() );
            }
             
        }
         
        rejectVariadicParameters(ret.getParameters());
        if (lexer.current instanceof ArrowToken)
        {
            lexer.nextToken();
            consumeBlankLines();
            firstBodyToken = lexer.current;
        }
        else
        {
            consumeBlankLines();
        } 
        Token lastToken = lexerCurrent();
        indentColumn = firstBodyToken.column;
        while (awaiting(start,RBraceToken.class))
        {
            if (lexer.current.column != indentColumn)
            {
                reportError("P1016",
			    hash( "required indentation", "" + (indentColumn - 1),
				  "given indentation", "" + (lexer.current.column - 1) ), 
			    "Indentation mismatch; is " + (lexer.current.column - 1) + 
			    ", should be " + (indentColumn - 1) + ".");
            }
             
            ret.getBody().add(parseStatement(StatementLevel.MethodLevel));
            if (lexer.current == lastToken)
            {
                reportError("P1000",lexer.current,"Nothing consumed in block body.");
                break;
            }
             
        }
        indentColumn = indentStart;
        nextToken();
        return ret;
    }

    private boolean parseArgumentList(List<ParseNode> arguments)  {
        Boolean ret = false;
        if (lexer.current instanceof LParenToken)
        {
            Token start = lexer.current;
            nextToken();
            while (awaiting(start,RParenToken.class))
            {
                ParseNode expr = parseExpression();
                arguments.add(expr);
                consumeBlankLines();
                if (lexer.current instanceof CommaToken)
                {
                    nextToken();
                    if (lexer.current instanceof RParenToken)
                        reportError("P1018",lexer.current,"Expected argument after comma.");
                     
                }
                else if (!(lexer.current instanceof RParenToken))
                {
                    reportError("P1023",lexer.current,"In argument list of request, expected " + " ',' or ')'.");
                    break;
                }
                  
            }
            nextToken();
        }
        else if (hasDelimitedTerm())
        {
            arguments.add(parseTerm());
        }
          
        return ret;
    }

    private void parseGenericArgumentList(List<ParseNode> arguments)  {
        if (lexer.current instanceof LGenericToken)
        {
            Token start = lexer.current;
            nextToken();
            while (awaiting(start,RGenericToken.class))
            {
                ParseNode expr = parseExpression();
                arguments.add(expr);
                consumeBlankLines();
                if (lexer.current instanceof CommaToken)
                    nextToken();
                else if (!(lexer.current instanceof RGenericToken))
                {
                    reportError("P1024",lexer.current,"In generic argument list of request, expected " + " ',' or '>'.");
                    break;
                }
                  
            }
            nextToken();
        }
         
    }

    private ParseNode parseImplicitReceiverRequest(ParseNode lhs)  {
        ImplicitReceiverRequestParseNode ret = new ImplicitReceiverRequestParseNode(lhs);
        parseGenericArgumentList(ret.getGenericArguments().get(0));
        parseArgumentList(ret.getArguments().get(0));
        while (lexer.current instanceof IdentifierToken)
        {
            // This is a multi-part method name
            IdentifierParseNode partName = parseIdentifier();
            ret.addPart(partName);
            Boolean hadParen = lexer.current instanceof LParenToken;
            parseArgumentList(Last(ret.getArguments()));
            if (Last(ret.getArguments()).size() == 0 && !hadParen)
            {
                reportError("P1040", 
			    hash( "part", partName.getName() ),
			    "No argument list in request.");
            }
             
        }
        return ret;
    }

    private ParseNode parseDotRequest(ParseNode lhs)  {
        ExplicitReceiverRequestParseNode ret = new ExplicitReceiverRequestParseNode(lhs);
        nextToken();
        boolean named = false;
        while (lexer.current instanceof IdentifierToken)
        {
            // Add this part of the method name
            ret.addPart(parseIdentifier());
            parseGenericArgumentList(Last(ret.getGenericArguments()));
            Boolean hadParen = lexer.current instanceof LParenToken;
            parseArgumentList(Last(ret.getArguments()));
            if (Last(ret.getArguments()).size() == 0 && !hadParen)
                return ret;
             
            named = true;
        }
        if (!named)
        {
            reportError("P1025",lexer.current,"Expected identifier after '.'.");
        }
         
        return ret;
    }

    private ParseNode parseComment()  {
        ParseNode ret = new CommentParseNode(lexer.current);
        nextToken();
        return ret;
    }

    private ParseNode collapseComments(List<ParseNode> comments)  {
        ParseNode first = comments.get(0);
        ParseNode last = first;
        for (int i = 1;i < comments.size();i++)
        {
            last.setComment(comments.get(i));
            last = comments.get(i);
        }
        return first;
    }

    private void takeLineComments()  {
        if (!(lexer.current instanceof CommentToken))
            return ;
         
        ParseNode ret = new CommentParseNode(lexer.current);
        comments.add(ret);
        lexer.nextToken();
        if (lexer.current instanceof NewLineToken)
        {
            lexer.nextToken();
            if (lexer.current instanceof CommentToken)
                takeLineComments();
             
        }
         
    }

    private void takeComments()  {
        if (!(lexer.current instanceof CommentToken))
            return ;
         
        ParseNode ret = new CommentParseNode(lexer.current);
        comments.add(ret);
        lexer.nextToken();
        if (lexer.current instanceof NewLineToken)
        {
            // Check for continuation lines
            Token t = lexer.peek();
            if (t.column > indentColumn)
                lexer.nextToken();
             
        }
         
        if (lexer.current instanceof CommentToken)
            takeComments();
         
    }

}


