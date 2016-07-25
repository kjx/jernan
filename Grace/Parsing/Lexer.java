//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
// Fixed by James Noble. Or not. Liscence files MISSING
//

package Grace.Parsing;

//import CS2JNet.System.IntegerSupport;  //KJX BUGGY
//import CS2JNet.System.StringSupport;   //KJX BUGGY

import Grace.ErrorReporting;
import static Grace.ErrorReporting.hash;
import Grace.StringInfo; //KJX evil compatability shit.
import Grace.StringSupport; //KJX evil compatability shit.

import Grace.Parsing.AliasKeywordToken;
import Grace.Parsing.ArrowToken;
import Grace.Parsing.AsToken;
import Grace.Parsing.BindToken;
import Grace.Parsing.ClassKeywordToken;
import Grace.Parsing.CloseBracketToken;
import Grace.Parsing.ColonToken;
import Grace.Parsing.CommaToken;
import Grace.Parsing.CommentToken;
import Grace.Parsing.DefKeywordToken;
import Grace.Parsing.DialectKeywordToken;
import Grace.Parsing.DotToken;
import Grace.Parsing.EndToken;
import Grace.Parsing.ExcludeKeywordToken;
import Grace.Parsing.IdentifierToken;
import Grace.Parsing.ImportKeywordToken;
import Grace.Parsing.InheritsKeywordToken;
import Grace.Parsing.IsKeywordToken;
import Grace.Parsing.LBraceToken;
import Grace.Parsing.LGenericToken;
import Grace.Parsing.LParenToken;
import Grace.Parsing.MethodKeywordToken;
import Grace.Parsing.NewLineToken;
import Grace.Parsing.NumberToken;
import Grace.Parsing.ObjectKeywordToken;
import Grace.Parsing.OpenBracketToken;
import Grace.Parsing.OperatorToken;
import Grace.Parsing.OuterKeywordToken;
import Grace.Parsing.RBraceToken;
import Grace.Parsing.ReturnKeywordToken;
import Grace.Parsing.RGenericToken;
import Grace.Parsing.RParenToken;
import Grace.Parsing.SelfKeywordToken;
import Grace.Parsing.SemicolonToken;
import Grace.Parsing.SingleEqualsToken;
import Grace.Parsing.StringToken;
import Grace.Parsing.Token;
import Grace.Parsing.TraitKeywordToken;
import Grace.Parsing.TypeKeywordToken;
import Grace.Parsing.UnknownToken;
import Grace.Parsing.UsesKeywordToken;
import Grace.Parsing.VarKeywordToken;
import Grace.Parsing.WhereKeywordToken;

import java.util.HashMap;
import java.util.HashSet;
import java.text.Normalizer;
import java.util.stream.IntStream;
import java.util.PrimitiveIterator;

//KJX BUGGY
//hack to emulate C#
class UnicodeCategory {
    public final byte category;
    public UnicodeCategory(byte b) { 
	category = b;
    }
    public static UnicodeCategory UppercaseLetter = new UnicodeCategory(Character.UPPERCASE_LETTER);
    public static UnicodeCategory LowercaseLetter = new UnicodeCategory(Character.LOWERCASE_LETTER);
    public static UnicodeCategory TitlecaseLetter = new UnicodeCategory(Character.TITLECASE_LETTER);
    public static UnicodeCategory ModifierLetter = new UnicodeCategory(Character.MODIFIER_LETTER);
    public static UnicodeCategory OtherLetter = new UnicodeCategory(Character.OTHER_LETTER);
    public static UnicodeCategory NonSpacingMark = new UnicodeCategory(Character.NON_SPACING_MARK);
    public static UnicodeCategory SpacingCombiningMark = new UnicodeCategory(Character.COMBINING_SPACING_MARK);
    public static UnicodeCategory EnclosingMark = new UnicodeCategory(Character.ENCLOSING_MARK);
    public static UnicodeCategory DecimalDigitNumber = new UnicodeCategory(Character.DECIMAL_DIGIT_NUMBER);
    public static UnicodeCategory LetterNumber = new UnicodeCategory(Character.LETTER_NUMBER);
    public static UnicodeCategory OtherNumber = new UnicodeCategory(Character.OTHER_NUMBER);
    public static UnicodeCategory ConnectorPunctuation = new UnicodeCategory(Character.CONNECTOR_PUNCTUATION);
    public static UnicodeCategory DashPunctuation = new UnicodeCategory(Character.DASH_PUNCTUATION);
    public static UnicodeCategory OpenPunctuation = new UnicodeCategory(Character.START_PUNCTUATION);
    public static UnicodeCategory ClosePunctuation = new UnicodeCategory(Character.END_PUNCTUATION);
    public static UnicodeCategory InitialQuotePunctuation = new UnicodeCategory(Character.INITIAL_QUOTE_PUNCTUATION);
    public static UnicodeCategory FinalQuotePunctuation = new UnicodeCategory(Character.FINAL_QUOTE_PUNCTUATION);
    public static UnicodeCategory OtherPunctuation = new UnicodeCategory(Character.OTHER_PUNCTUATION);
    public static UnicodeCategory MathSymbol = new UnicodeCategory(Character.MATH_SYMBOL);
    public static UnicodeCategory CurrencySymbol = new UnicodeCategory(Character.CURRENCY_SYMBOL);
    public static UnicodeCategory ModifierSymbol = new UnicodeCategory(Character.MODIFIER_SYMBOL);
    public static UnicodeCategory OtherSymbol = new UnicodeCategory(Character.OTHER_SYMBOL);
    public static UnicodeCategory SpaceSeparator = new UnicodeCategory(Character.SPACE_SEPARATOR);
    public static UnicodeCategory LineSeparator = new UnicodeCategory(Character.LINE_SEPARATOR);
    public static UnicodeCategory ParagraphSeparator = new UnicodeCategory(Character.PARAGRAPH_SEPARATOR);
    public static UnicodeCategory Control = new UnicodeCategory(Character.CONTROL);
    public static UnicodeCategory Format = new UnicodeCategory(Character.FORMAT);
    public static UnicodeCategory Surrogate = new UnicodeCategory(Character.SURROGATE);
    public static UnicodeCategory PrivateUse = new UnicodeCategory(Character.PRIVATE_USE);
    public static UnicodeCategory OtherNotAssigned = new UnicodeCategory(Character.UNASSIGNED);
}

//hack to emulate michaels better version
class UnicodeLookup {

    public static UnicodeCategory GetUnicodeCategory(String code, int index) {
	int catcode = Character.getType(code.codePointAt(index));

	switch (catcode) 
	    {
	    case Character.UPPERCASE_LETTER: return UnicodeCategory.UppercaseLetter;
	    case Character.LOWERCASE_LETTER: return UnicodeCategory.LowercaseLetter;
	    case Character.TITLECASE_LETTER: return UnicodeCategory.TitlecaseLetter;
	    case Character.MODIFIER_LETTER: return UnicodeCategory.ModifierLetter;
	    case Character.OTHER_LETTER: return UnicodeCategory.OtherLetter;
	    case Character.NON_SPACING_MARK: return UnicodeCategory.NonSpacingMark;
	    case Character.COMBINING_SPACING_MARK: return UnicodeCategory.SpacingCombiningMark;
	    case Character.ENCLOSING_MARK: return UnicodeCategory.EnclosingMark;
	    case Character.DECIMAL_DIGIT_NUMBER: return UnicodeCategory.DecimalDigitNumber;
	    case Character.LETTER_NUMBER: return UnicodeCategory.LetterNumber;
	    case Character.OTHER_NUMBER: return UnicodeCategory.OtherNumber;
	    case Character.CONNECTOR_PUNCTUATION: return UnicodeCategory.ConnectorPunctuation;
	    case Character.DASH_PUNCTUATION: return UnicodeCategory.DashPunctuation;
	    case Character.START_PUNCTUATION: return UnicodeCategory.OpenPunctuation;
	    case Character.END_PUNCTUATION: return UnicodeCategory.ClosePunctuation;
	    case Character.INITIAL_QUOTE_PUNCTUATION: return UnicodeCategory.InitialQuotePunctuation;
	    case Character.FINAL_QUOTE_PUNCTUATION: return UnicodeCategory.FinalQuotePunctuation;
	    case Character.OTHER_PUNCTUATION: return UnicodeCategory.OtherPunctuation;
	    case Character.MATH_SYMBOL: return UnicodeCategory.MathSymbol;
	    case Character.CURRENCY_SYMBOL: return UnicodeCategory.CurrencySymbol;
	    case Character.MODIFIER_SYMBOL: return UnicodeCategory.ModifierSymbol;
	    case Character.OTHER_SYMBOL: return UnicodeCategory.OtherSymbol;
	    case Character.SPACE_SEPARATOR: return UnicodeCategory.SpaceSeparator;
	    case Character.LINE_SEPARATOR: return UnicodeCategory.LineSeparator;
	    case Character.PARAGRAPH_SEPARATOR: return UnicodeCategory.ParagraphSeparator;
	    case Character.CONTROL: return UnicodeCategory.Control;
	    case Character.FORMAT: return UnicodeCategory.Format;
	    case Character.SURROGATE: return UnicodeCategory.Surrogate;
	    case Character.PRIVATE_USE: return UnicodeCategory.PrivateUse;
	    case Character.UNASSIGNED: return UnicodeCategory.OtherNotAssigned;
	    }
	return UnicodeCategory.OtherNotAssigned;
    }
    


/****** KJX BUGGY --- useful list of unicode categories for cut & paste.
UppercaseLetter
LowercaseLetter
TitlecaseLetter
ModifierLetter
OtherLetter
NonSpacingMark
SpacingCombiningMark
EnclosingMark
DecimalDigitNumber
LetterNumber
OtherNumber
ConnectorPunctuation
DashPunctuation
OpenPunctuation
ClosePunctuation
InitialQuotePunctuation
FinalQuotePunctuation
OtherPunctuation
MathSymbol
CurrencySymbol
ModifierSymbol
OtherSymbol
SpaceSeparator
LineSeparator
ParagraphSeparator
Control
Format
Surrogate
PrivateUse
OtherNotAssigned
**********/

    // KJX Grace is putting me into bad habits
    public static HashMap<String, String> MirroredBrackets = new HashMap<String, String>();    
    public static HashSet<String> OpenBrackets = new HashSet<String>();
    public static HashSet<String> CloseBrackets = new HashSet<String>();

    private static void addBrackets(String left, String right) {
    	MirroredBrackets.put(left,right);
    	MirroredBrackets.put(right,left);
    	OpenBrackets.add(left);
    	CloseBrackets.add(right);
    }
    
    static {
    	addBrackets("(",")");
    	addBrackets("[","]");
    	addBrackets("{","}");
    	addBrackets("‹","›");
    	addBrackets("«","»");
    	addBrackets("⟦","⟧");	
    }

    // KJX Grace is putting me into bad habits
    //    public static HashMap<String, String> MirroredBrackets() { return _bracketMirror; }
    //    public static HashSet<String> OpenBrackets() { return _openBrackets; }
    //    public static HashSet<String> CloseBrackets() { return _closeBrackets; }
    
}





//KJX BUGGY ENDS

/**
* Tokeniser for Grace code
*/
public class Lexer
{
    private String code;
    private int index = 0;
    private int line = 1;
    private int column = 1;
    private int lineStart = -1;
    private boolean allowShebang = true;
    public String moduleName;
    public Token current;
    public Token previous;
    /**
    * @param module Module of this code
    *  @param code Code of this module as a string
    */
    public Lexer(String module, String code) throws Exception {
        this.code = code;
        this.moduleName = module;
        if (code.charAt(code.length() - 1) != '\n')
            this.code += "\n";
         
        nextToken();
    }

    /**
    * @param code Code of this module as a string
    */
    public Lexer(String code) throws Exception {
        this.code = code;
        if (code.charAt(code.length() - 1) != '\n')
            this.code += "\n";
         
        nextToken();
    }


    private void reportError(String code, HashMap<String,String> vars, String localDescription) throws Exception {
        ErrorReporting.ReportStaticError(moduleName, line, code, vars, localDescription);
    }

    private void reportError(String code, String localDescription) throws Exception {
        ErrorReporting.ReportStaticError(moduleName, line, code, localDescription);
    }

    public boolean done() throws Exception {
        return index == code.length();
    }

    /**
    * Behave as though the source code at this point
    * were immediately after the opening quote of a string
    * literal
    */
    public void treatAsString() throws Exception {
        column = index - lineStart;
        current = lexStringRemainder();
        return ;
    }

    /**
    * Look at what the next token would be, without
    * changing the state of the lexer
    */
    public Token peek() throws Exception {
        int startIndex = index;
        Token startCurrent = current;
        int startLine = line;
        Token startPrevious = previous;
        Token ret = nextToken();
        index = startIndex;
        line = startLine;
        current = startCurrent;
        previous = startPrevious;
        return ret;
    }

    private String formatCodepoint(int cp) throws Exception {
	//KJX could be BUGGY
        return "U+" + String.format("%4X",cp);
    }

    private UnicodeCategory validateChar() throws Exception {
        char c = code.charAt(index);
        if (c >= 0xD800 && c <= 0xDBFF)
        {
            // Leading surrogate
            char c2 = code.charAt(index + 1);
            if (c2 >= 0xDC00 && c2 <= 0xDFFF)
            {
            }
            else
            {
                // Trailing surrogate - ignore for now
                reportError("L0007","Illegal lone surrogate");
            } 
        }
        else if (c >= 0xDC00 && c <= 0xDFFF)
        {
            // Trailing surrogate
            reportError("L0007","Illegal lone surrogate");
        }
          
        UnicodeCategory cat = UnicodeLookup.GetUnicodeCategory(code, index);
        if (c == '\t')
            reportError("L0001","Tab characters are not permitted.");
         
        if (cat == UnicodeCategory.ParagraphSeparator || cat == UnicodeCategory.SpaceSeparator)
        {
            if (c != ' ' && c != '\u2028')
                reportError("L0002",new HashMap<String,String>(),"Illegal whitespace."); 
 
             
        }
        else if ((cat == UnicodeCategory.Control || cat == UnicodeCategory.Format || cat == UnicodeCategory.Surrogate) && c != '\n' && c != '\r')
        {
            reportError("L0003",new HashMap<String,String>(),"Illegal control character. ");
        }
          
        return cat;
    }

    private void advanceIndex() throws Exception {
        if (code.charAt(index) >= 0xD800 && code.charAt(index) <= 0xDBFF)
        {
            // Leading surrogate - skip the trailing one
            index++;
        }
         
        index++;

	//KJX wonders (if things break in unicode-land)
	//that something like Character.charCount might help
	//along with codePointAt...
    }


    /**
    * Get the next token from the stream and
    * advance the lexer
    */
    public Token nextToken() throws Exception {
        previous = current;
        if (index >= code.length())
        {
            current = new EndToken(moduleName,line,column);
            return current;
        }
         
        char c = code.charAt(index);
        column = index - lineStart;
        Token ret = null;
        UnicodeCategory cat = validateChar();
        String cStr = StringInfo.GetNextTextElement(code, index);
        if (isIdentifierStartCharacter(c,cat))
            ret = lexIdentifier();
         
        if (isOperatorCharacter(c,cat))
            ret = lexOperator();
         
        if (isNumberStartCharacter(c))
            ret = lexNumber();
         
        if (c == ' ')
        {
            skipSpaces();
            return nextToken();
        }
         
        if (c == '#' && allowShebang && column == 1)
        {
            while (code.charAt(index) != '\n' && code.charAt(index) != '\u2028')
            {
                // Eat the rest of the line, ignoring its
                // contents entirely.
                index++;
            }
            line++;
            lineStart = index;
            advanceIndex();
            return nextToken();
        }
        else if (column == 1)
            allowShebang = false;
          
        if (c == '"')
            ret = lexString();
         
        if (c == '(')
            ret = lexLParen();
         
        if (c == ')')
            ret = lexRParen();
         
        if (c == '{')
            ret = lexLBrace();
         
        if (c == '}')
            ret = lexRBrace();
         
        if (ret == null && UnicodeLookup.OpenBrackets.contains(cStr))
            ret = lexOpenBracket();
         
        if (ret == null && UnicodeLookup.CloseBrackets.contains(cStr))
            ret = lexCloseBracket();
         
        //if (UnicodeLookup.CloseBrackets.contains(cStr))
        //    ret = lexCloseBracket();
        if (c == ',')
            ret = lexComma();
         
        if (c == ';')
            ret = lexSemicolon();
         
        if (c == '\n' || c == '\u2028' || c == '\r')
        {
            ret = new NewLineToken(moduleName,line,column);
            lineStart = index;
            line++;
            advanceIndex();
            if (c == '\r' && index < code.length() && code.charAt(index) == '\n')
            {
                advanceIndex();
                lineStart++;
            }
             
        }
         
        if (ret == null)
        {
            reportError("L0000",new HashMap<String,String>(),"Character '" + c + "' may not appear here");
            ret = new UnknownToken(moduleName,line,index - 1);
        }
         
        current = ret;
        return ret;
    }

    private Token lexIdentifier() throws Exception {
        int start = index;
        advanceIndex();
        UnicodeCategory cat = validateChar();
        while (isIdentifierCharacter(code.charAt(index),cat))
        {
            advanceIndex();
            cat = validateChar();
        }
        String ident = code.substring(start, (start) + (index - start));
        if (StringSupport.equals("object", ident))
            return new ObjectKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("var", ident))
            return new VarKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("def", ident))
            return new DefKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("method", ident))
            return new MethodKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("class", ident))
            return new ClassKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("trait", ident))
            return new TraitKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("inherit", ident))
            return new InheritsKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("use", ident))
            return new UsesKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("import", ident))
            return new ImportKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("as", ident))
            return new AsToken(moduleName,line,column);
         
        if (StringSupport.equals("dialect", ident))
            return new DialectKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("return", ident))
            return new ReturnKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("type", ident))
            return new TypeKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("is", ident))
            return new IsKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("where", ident))
            return new WhereKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("outer", ident))
            return new OuterKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("self", ident))
            return new SelfKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("alias", ident))
            return new AliasKeywordToken(moduleName,line,column);
         
        if (StringSupport.equals("exclude", ident))
            return new ExcludeKeywordToken(moduleName,line,column);
         
        return new IdentifierToken(moduleName, line, column, 
				   Normalizer.normalize(ident, Normalizer.Form.NFC));
    }

    /**
    * Check whether a given string is an identifier.
    * 
    *  @param s String to check
    */
    public static boolean isIdentifier(String s) throws Exception {
	String first = StringInfo.GetNextTextElement(s,0);
        UnicodeCategory cat = UnicodeLookup.GetUnicodeCategory(s, 0);
        if (!isIdentifierStartCharacter(first.charAt(0), cat))
            return false;
        
	//more KJX BUGGINESS
        PrimitiveIterator.OfInt en = s.codePoints().iterator();
        while (en.hasNext())
        {
	    String el = StringInfo.CodePointToString(en.next());
            cat = UnicodeLookup.GetUnicodeCategory(el, 0);
            if (!isIdentifierCharacter(el.charAt(0), cat))
                return false;
        }
        return true;
    }

    private static boolean isIdentifierStartCharacter(char c, UnicodeCategory cat) throws Exception {
        return (cat == UnicodeCategory.LowercaseLetter || cat == UnicodeCategory.UppercaseLetter || cat == UnicodeCategory.TitlecaseLetter || cat == UnicodeCategory.ModifierLetter || cat == UnicodeCategory.OtherLetter || c == '_' || (!isOperatorCharacter(c,cat) && (cat == UnicodeCategory.OtherSymbol)));
    }

    private static boolean isIdentifierCharacter(char c, UnicodeCategory cat) throws Exception {
        return (cat == UnicodeCategory.LowercaseLetter || cat == UnicodeCategory.UppercaseLetter || cat == UnicodeCategory.TitlecaseLetter || cat == UnicodeCategory.ModifierLetter || cat == UnicodeCategory.OtherLetter || cat == UnicodeCategory.DecimalDigitNumber || cat == UnicodeCategory.LetterNumber || cat == UnicodeCategory.OtherNumber || cat == UnicodeCategory.NonSpacingMark || cat == UnicodeCategory.SpacingCombiningMark || cat == UnicodeCategory.EnclosingMark || (!isOperatorCharacter(c,cat) && (cat == UnicodeCategory.OtherSymbol)) || c == '\'' || c == '_');
    }

    private boolean isNumberStartCharacter(char c) throws Exception {
        return (c >= '0' && c <= '9');
    }

    private boolean isDigitInBase(char c, int numBase) throws Exception {
        if (c >= '0' && c <= '9')
            return (c - '0') < numBase;
         
        if (c >= 'A' && c <= 'Z')
            return (c - 'A' + 10) < numBase;
         
        if (c >= 'a' && c <= 'z')
            return (c - 'a' + 10) < numBase;
         
        return false;
    }

    private Token lexOperator() throws Exception {
        boolean spaceBefore = false, spaceAfter = false;
        int start = index;
        if (start > 0 && (code.charAt(start - 1) == ' '))
            spaceBefore = true;
         
        advanceIndex();
        UnicodeCategory cat = validateChar();
        while (isOperatorCharacter(code.charAt(index),cat) || cat == UnicodeCategory.NonSpacingMark || cat == UnicodeCategory.SpacingCombiningMark || cat == UnicodeCategory.EnclosingMark)
        {
            advanceIndex();
            cat = validateChar();
        }
        String op = code.substring(start, (start) + (index - start));
        if (op.startsWith("//"))
        {
            index = start;
            return lexComment();
        }
         
        if (".".equals(op))
        {
            return new DotToken(moduleName,line,column);
        }
         
        if ("=".equals(op))
        {
            return new SingleEqualsToken(moduleName,line,column);
        }
         
        if ("->".equals(op))
        {
            return new ArrowToken(moduleName,line,column);
        }
         
        if (":".equals(op))
        {
            return new ColonToken(moduleName,line,column);
        }
         
        if (":=".equals(op))
        {
            return new BindToken(moduleName,line,column);
        }
         
        if (index < code.length() && (code.charAt(index) == ' ' || code.charAt(index) == '\r' || code.charAt(index) == '\n' || code.charAt(index) == '\u2028'))
            spaceAfter = true;
         
        if (op.startsWith(">") && !spaceBefore)
        {
            // This is a closing generic followed by some other
            // operator, rather than a single operator, so we
            // need to "un-lex" some codepoints.
            index -= (op.length() - 1);
            return new RGenericToken(moduleName,line,column);
        }
         
        OperatorToken ret = new OperatorToken(moduleName, line, column, 
	     Normalizer.normalize(op, Normalizer.Form.NFC));

        ret.setSpacing(spaceBefore,spaceAfter);
        return ret;
    }

    private Token lexComment() throws Exception {
        advanceIndex();
        advanceIndex();
        int start = index;
        while (code.charAt(index) != '\n' && code.charAt(index) != '\u2028')
        {
            validateChar();
            // If this is a leading surrogate, skip over
            // the trailing surrogate too.
            if (code.charAt(index) >= '\ud800' && code.charAt(index) <= '\udfff')
                advanceIndex();
             
            advanceIndex();
        }
        String body = code.substring(start, (start) + (index - start));
        return new CommentToken(moduleName,line,column,body);
    }

    private static boolean isOperatorCharacter(char c, UnicodeCategory cat) throws Exception {
        return c != '"' && c != ',' && c != ';' && (c == '+' || c == '-' || c == '*' || c == '/' || c == '=' || c == '!' || c == '.' || c == '>' || c == '<' || c == '@' || c == '$' || c == '?' || c == '&' || c == '|' || c == '^' || c == '%' || c == '#' || c == '~' || c == ':' || c == '\\' || c == '¬' || c == '±' || c == '×' || c == '÷' || c == '¡' || c == '¢' || c == '£' || c == '¤' || c == '¥' || c == '§' || c == '¿' || c == '‽' || c == '⁂' || (c >= 0x2200 && c <= 0x22ff) || (c >= 0x2a00 && c <= 0x2aff) || (c >= 0x27c0 && c <= 0x27ef) || (c >= 0x2980 && c <= 0x29ff) || (c >= 0x2b00 && c <= 0x2bff) || (c >= 0x2190 && c <= 0x21ff) || (c >= 0x27f0 && c <= 0x27ff) || (c >= 0x2900 && c <= 0x297f) || (c >= 0x2300 && c <= 0x23ff) || (c >= 0x20a0 && c <= 0x20cf) || (c >= 0x25a0 && c <= 0x25ff));
    }

    // Standard ASCII keyboard operators
    // Additional individual operator codepoints
    // From Latin-1
    // From General Punctuation
    // Block: Mathematical Operators
    // Block: Supplemental Mathematical Operators
    // Block: Miscellaneous Mathematical Symbols-A
    // Block: Miscellaneous Mathematical Symbols-B
    // Block: Miscellaneous Symbols and Arrows
    // Block: Arrows
    // Block: Supplemental Arrows-A
    // Block: Supplemental Arrows-B
    // Block: Supplemental Technical
    // Block: Currency Symbols
    // Block: Geometric Shapes
    private void skipSpaces() throws Exception {
        advanceIndex();
        while (' ' == code.charAt(index))
        {
            advanceIndex();
        }
        return ;
    }

    // new SpaceToken(moduleName, line, column, index - start);
    private Token lexNumber() throws Exception {
        char base1 = code.charAt(index);
        char base2 = '\0';
        char base3 = '\0';
        if (index + 1 < code.length())
            base2 = code.charAt(index + 1);
         
        if (index + 2 < code.length())
            base3 = code.charAt(index + 2);
         
        int numbase = 10;
        if (base2 == 'x')
        {
            numbase = base1 - '0';
            if (numbase == 0)
                numbase = 16;
             
            index += 2;
        }
        else if (base2 >= '0' && base2 <= '9' && base3 == 'x')
        {
            numbase = (base1 - '0') * 10 + base2 - '0';
            index += 3;
        }
          
        if (numbase == 1 || numbase > 36)
            reportError("L0012", hash("base", "" + numbase), "Invalid base ${base}");
         
        int start = index;
        while (isDigitInBase(code.charAt(index),numbase))
        advanceIndex();
        if (code.charAt(index) == '.')
        {
            // Fractional number?
            if (isDigitInBase(code.charAt(index + 1),numbase))
            {
                // Yes!
                advanceIndex();
                while (isDigitInBase(code.charAt(index),numbase))
                advanceIndex();
            }
             
        }
         
        String digits = code.substring(start, (start) + (index - start));
        if (index < code.length() && isDigitInBase(code.charAt(index),36))
            reportError("L0004",new HashMap<String,String>(),"Not a valid digit in base " + numbase + ": " + code.charAt(index) + ".");
         
        if (digits.length() == 0)
            reportError("L0005","No valid digits in number.");
         
        return new NumberToken(moduleName,line,column,numbase,digits);
    }

    private Token lexString() throws Exception {
        advanceIndex();
        return lexStringRemainder();
    }

    private void appendHexEscape(StringBuilder b, String hex) throws Exception {
        for (int i = 0;i < hex.length();i++)
        {
            char c = hex.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')))
            {
                String error = "" + c;
                // We don't want to cut a surrogate pair in half,
                // but also don't want to consume combining marks
                // here as StringInfo.GetNextTextElement would.
                if (Character.isHighSurrogate(c)) //KJXBUGGY
                    error += hex.charAt(i + 1);
                reportError("L0013",hash("length", "" + hex.length(), "u", hex.length() == 4 ? "u" : "U",  "error", "" + error), "Invalid unicode escape");
	    }
             
        }
        System.out.println("KJX BUGGY001");
        int cp = Integer.parseInt(hex, 16); //KJXBUGGY
        b.appendCodePoint(cp); //KJXBUGGY
    }

    private Token lexStringRemainder() throws Exception {
        int start = index;
        StringBuilder b = new StringBuilder();
        boolean escaped = false;
        while (index < code.length() && ('"' != code.charAt(index) || escaped))
        {
            validateChar();
            if (!escaped && code.charAt(index) == '\\')
                escaped = true;
            else if (code.charAt(index) == '{' && !escaped)
            {
                return new StringToken(moduleName,line,column,b.toString(),code.substring(start, (start) + (index - start)),true);
            }
            else if ((code.charAt(index) == '\n' || code.charAt(index) == '\r') && index < code.length() - 1)
            {
                reportError("L0011","String literal contains line break.");
            }
            else if (escaped)
            {
                char c = code.charAt(index);
                if (c == 'n')
                    b.append('\u2028');
                else if (c == 't')
                    b.append('\t');
                else if (c == 'l')
                    b.append('\u2028');
                else if (c == '{')
                    b.append('{');
                else if (c == '}')
                    b.append('}');
                else if (c == '\\')
                    b.append('\\');
                else if (c == '"')
                    b.append('"');
                else if (c == 'u')
                {
                    // Four-character BMP escape
                    advanceIndex();
                    if (index + 4 > code.length())
                    {
                        reportError("L0013",hash("length", "4",  "u", "u" ,  "error", "end of file"), "Invalid unicode escape");
                    }
                     
                    String hex = code.substring(index, (index) + (4));
                    appendHexEscape(b,hex);
                    advanceIndex();
                    advanceIndex();
                    advanceIndex();
                }
                else if (c == 'U')
                {
                    // Six-character Unicode escape
                    advanceIndex();
                    if (index + 6 > code.length())
                    {
                        reportError("L0013", hash("length", "6", "u", "U", "error", "end of file") ,"Invalid unicode escape");
                    }
                     
                    String hex = code.substring(index, (index) + (6));
                    appendHexEscape(b,hex);
                    advanceIndex();
                    advanceIndex();
                    advanceIndex();
                    advanceIndex();
                    advanceIndex();
                }
                else
                    reportError("L0008",new HashMap<String,String>(),"Unknown escape sequence");         
                escaped = false;
            }
            else
            {
                b.append(code.charAt(index));
                // If this is a leading surrogate, copy and skip over
                // the trailing surrogate too (already validated).
                if (code.charAt(index) >= '\ud800' && code.charAt(index) <= '\udfff')
                    b.append(code.charAt(++index));
                 
                escaped = false;
            }    
            advanceIndex();
        }
        if (index == code.length())
            reportError("L0006","Unterminated string literal.");
         
        advanceIndex();
        return new StringToken(moduleName,line,column,b.toString(),code.substring(start, (start) + (index - start - 1)));
    }

    private Token lexOpenBracket() throws Exception {
        int start = index;
        advanceIndex();
        UnicodeCategory cat = validateChar();
        char c = code.charAt(index);
        String cStr = StringInfo.GetNextTextElement(code, index);
        while (UnicodeLookup.OpenBrackets.contains(cStr) || isOperatorCharacter(c,cat))
        {
            advanceIndex();
            cat = validateChar();
            c = code.charAt(index);
            cStr = StringInfo.GetNextTextElement(code, index);
            if (StringSupport.equals(code.substring(start, (start) + (index - start)), "[["))
                return new LGenericToken(moduleName,line,column);
             
        }
        String bracket = code.substring(start, (start) + (index - start));
        int[] indices = StringInfo.ParseCombiningCharacters(bracket);
        int lastIndex = indices[indices.length - 1];
        String l = StringInfo.GetNextTextElement(bracket, lastIndex);
        if (!UnicodeLookup.OpenBrackets.contains(l))
            reportError("L0009",hash( "char", l), "Invalid character at end of bracket sequence");
         
        if (StringSupport.equals(l, "(") || StringSupport.equals(l, "{"))
            reportError("L0010", hash("char", l), "Invalid character at end of bracket sequence");
         
        return new OpenBracketToken(moduleName,line,column,bracket);
    }

    private Token lexCloseBracket() throws Exception {
        int start = index;
        advanceIndex();
        UnicodeCategory cat = validateChar();
        char c = code.charAt(index);
        String cStr = StringInfo.GetNextTextElement(code, index);
        while (UnicodeLookup.CloseBrackets.contains(cStr) || isOperatorCharacter(c,cat))
        {
            advanceIndex();
            cat = validateChar();
            c = code.charAt(index);
            cStr = StringInfo.GetNextTextElement(code, index);
            if (StringSupport.equals(code.substring(start, (start) + (index - start)), "]]"))
                return new RGenericToken(moduleName,line,column);
             
        }
        String bracket = code.substring(start, (start) + (index - start));
        int[] indices = StringInfo.ParseCombiningCharacters(bracket);
        int lastIndex = indices[indices.length - 1];
        String l = StringInfo.GetNextTextElement(bracket, lastIndex);
        // For ease, any ) characters at the end of a closing bracket
        // are removed from the token.
        int sub = 0;
        int blen = bracket.length();
        int graphemeOffset = indices.length - 1;
        while (StringSupport.equals(l, ")") || StringSupport.equals(l, "."))
        {
            sub++;
            graphemeOffset--;
            lastIndex = indices[graphemeOffset];
            l = StringInfo.GetNextTextElement(bracket, lastIndex);
        }
        if (sub > 0)
        {
            // Reset index to before the )s so that they will be
            // lexed as the following tokens.
            index -= sub;
            bracket = bracket.substring(0, (0) + (bracket.length() - sub));
        }
         
        if (!UnicodeLookup.CloseBrackets.contains(l))
            reportError("L0009",hash( "char", l) ,"Invalid character at end of bracket sequence");
         
        return new CloseBracketToken(moduleName,line,column,bracket);
    }

    private Token lexLParen() throws Exception {
        advanceIndex();
        return new LParenToken(moduleName,line,column);
    }

    private Token lexRParen() throws Exception {
        advanceIndex();
        return new RParenToken(moduleName,line,column);
    }

    private Token lexLBrace() throws Exception {
        advanceIndex();
        return new LBraceToken(moduleName,line,column);
    }

    private Token lexRBrace() throws Exception {
        advanceIndex();
        return new RBraceToken(moduleName,line,column);
    }

    private Token lexSemicolon() throws Exception {
        advanceIndex();
        return new SemicolonToken(moduleName,line,column);
    }

    private Token lexComma() throws Exception {
        advanceIndex();
        return new CommaToken(moduleName,line,column);
    }

}


