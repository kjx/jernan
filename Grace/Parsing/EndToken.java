//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class EndToken  extends Token 
{
    public EndToken(String module, int line, int column)  {
        super(module, line, column);
    }

    protected String describe()  {
        return "End";
    }

    public String toString() {
        try
        {
            return "end of file";
        }
        catch (RuntimeException __dummyCatchVar1)
        {
            throw __dummyCatchVar1;
        }
        catch (Exception __dummyCatchVar1)
        {
            throw new RuntimeException(__dummyCatchVar1);
        }
    
    }

}


