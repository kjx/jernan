//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.BracketToken;

public class CloseBracketToken  extends BracketToken 
{
    public CloseBracketToken(String module, int line, int column, String val)  {
        super(module, line, column, val);
        setClosing(true);
    }

    protected String describe()  {
        return "CloseBracket:" + getName();
    }

}


