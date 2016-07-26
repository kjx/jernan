//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;

import Grace.Execution.Node;
import Grace.Execution.NumberLiteralNode;

/**
* A numeric literal
*/
public class NumberLiteralNode  extends Node 
{
    private NumberParseNode origin = new NumberParseNode();
    Rational numbase = 10;
    Rational val = new Rational();
    public NumberLiteralNode(Token location, NumberParseNode source) throws Exception {
        super(location, source);
        origin = source;
        numbase = Rational.Create(origin.NumericBase);
        Rational integral = Rational.Zero;
        Rational fractional = Rational.Zero;
        Rational size = Rational.One;
        boolean frac = false;
        for (Object __dummyForeachVar0 : origin.Digits)
        {
            char c = (Character)__dummyForeachVar0;
            if (c == '.')
                frac = true;
            else if (!frac)
            {
                integral *= numbase;
                integral += digit(c);
            }
            else
            {
                size /= numbase;
                fractional += size * digit(c);
            }  
        }
        val = integral + fractional;
    }

    private static Dictionary<char, Rational> digits = new Dictionary<char, Rational>();
    private static Rational digit(char c) throws Exception {
        if (!digits.ContainsKey(c))
        {
            if (c >= '0' && c <= '9')
            {
                digits[c] = Rational.Create(c - '0');
            }
             
            if (c >= 'a' && c <= 'z')
            {
                digits[c] = Rational.Create(10 + c - 'a');
            }
             
            if (c >= 'A' && c <= 'Z')
            {
                digits[c] = Rational.Create(10 + c - 'A');
            }
             
        }
         
        return digits[c];
    }

    /**
    * The value of this literal as a RationalThis property gets the value of the field val
    */
    public Rational getValue() throws Exception {
        return val;
    }

    /**
    * 
    */
    public void debugPrint(System.IO.TextWriter tw, String prefix) throws Exception {
        String desc = "";
        if (origin.NumericBase == 10)
            desc += origin.Digits;
        else if (origin.NumericBase == 16)
            desc += "0x" + origin.Digits;
        else
            desc += origin.NumericBase + "x" + origin.Digits;  
        tw.WriteLine(prefix + "Number: " + desc + " (" + getValue() + ")");
    }

    /**
    * 
    */
    public GraceObject evaluate(EvaluationContext ctx) throws Exception {
        return GraceNumber.Create(getValue());
    }

    //return new GraceObjectProxy(Value);
    // Below exposes state as Grace methods.
    private static Dictionary<String, Method> sharedMethods = new Dictionary<String, Method>{ { "value", new DelegateMethodTyped0<NumberLiteralNode>(mValue) } };
    /**
    * 
    */
    protected void addMethods() throws Exception {
        AddMethods(sharedMethods);
    }

    private static GraceObject mValue(NumberLiteralNode self) throws Exception {
        return GraceNumber.Create(self.getValue());
    }

}


