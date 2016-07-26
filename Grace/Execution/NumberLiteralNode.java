//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.NumberParseNode;
import java.io.PrintStream;


import Grace.Execution.Node;
import Grace.Execution.NumberLiteralNode;

/**
* A numeric literal
*/
public class NumberLiteralNode  extends Node 
{
    private NumberParseNode origin;
//KJX HACKED to remove value, just store digits
//KJX removes dependency on Kernan's Rational class
//     Rational numbase = 10;
//     Rational val;
    public NumberLiteralNode(Token location, NumberParseNode source) throws Exception {
        super(location, source);
        origin = source;
//         numbase = Rational.Create(origin.getNumericBase());
//         Rational integral = Rational.Zero;
//         Rational fractional = Rational.Zero;
//         Rational size = Rational.One;
//         boolean frac = false;
//         for (Object __dummyForeachVar0 : origin.getDigits())
//         {
//             char c = (Character)__dummyForeachVar0;
//             if (c == '.')
//                 frac = true;
//             else if (!frac)
//             {
//                 integral *= numbase;
//                 integral += digit(c);
//             }
//             else
//             {
//                 size /= numbase;
//                 fractional += size * digit(c);
//             }  
//         }
//         val = integral + fractional;
    }

//     private static Map<char, Rational> digits = new HashMap<char, Rational>();
//     private static Rational digit(char c)  {
//         if (!digits.ContainsKey(c))
//         {
//             if (c >= '0' && c <= '9')
//             {
//                 digits[c] = Rational.Create(c - '0');
//             }
             
//             if (c >= 'a' && c <= 'z')
//             {
//                 digits[c] = Rational.Create(10 + c - 'a');
//             }
             
//             if (c >= 'A' && c <= 'Z')
//             {
//                 digits[c] = Rational.Create(10 + c - 'A');
//             }
             
//         }
         
//         return digits[c];
//     }

//     /**
//     * The value of this literal as a RationalThis property gets the value of the field val
//     */
//     public Rational getValue()  {
//         return val;
//     }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        String desc = "";
        if (origin.getNumericBase() == 10)
            desc += origin.getDigits();
        else if (origin.getNumericBase() == 16)
            desc += "0x" + origin.getDigits();
        else
            desc += origin.getNumericBase() + "x" + origin.getDigits();  
        tw.println(prefix + "Number: " + desc + " (NO VALUE)");
    }
}


