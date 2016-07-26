//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.BracketToken;

public class OpenBracketToken  extends BracketToken 
{
    public OpenBracketToken(String module, int line, int column, String val)  {
        super(module, line, column, val);
        setOpening(true);
    }

    protected String describe()  {
        return "OpenBracket:" + getName();
    }

}


