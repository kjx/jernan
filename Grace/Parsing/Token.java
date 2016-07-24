//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.ErrorReporting;
import Grace.Parsing.ArrowToken;
import Grace.Parsing.RParenToken;
import Grace.Parsing.Token;

/**
* A token of Grace source
*/
public abstract class Token   
{
    /**
    * Module this token was found in
    */
    public String module;
    /**
    * Line this token was found at
    */
    public int line;
    /**
    * Column this token was found at
    */
    public int column;
    /**
    * @param module Module this token was found in
    *  @param line Line this token was found at
    *  @param column Column this token was found at
    */
    public Token(String module, int line, int column) throws Exception {
        this.module = module;
        this.line = line;
        this.column = column;
    }

    /**
    * 
    */
    public String toString() {
        try
        {
            return "<Token:" + module + ":" + line + ":" + column + "::" + describe() + ">";
        }
        catch (RuntimeException __dummyCatchVar0)
        {
            throw __dummyCatchVar0;
        }
        catch (Exception __dummyCatchVar0)
        {
            throw new RuntimeException(__dummyCatchVar0);
        }
    
    }

    /**
    * Subclass-specific description of the value of this token.
    */
    abstract protected String describe() throws Exception ;

    /**
    * Module this token was found in.
    */
    public String getModule() throws Exception {
        return module;
    }

    /**
    * Give a string description of a token class, suitable for
    * use in an error message.
    * Class to describe
    */
    public static <T extends Token>String describeSubclass() throws Exception {
	return "KJX-FUCKED-KJX-Token-FUCKED";
	//KJX MUST FIX
//        if (ArrowToken.class.equals(T.class))
//             return "->";
         
//         if (RParenToken.class.equals(T.class))
//             return ")";
         
//         return T.class.getName();
    }

}


