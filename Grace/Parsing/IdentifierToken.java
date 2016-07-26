//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class IdentifierToken  extends Token 
{
    private String _name;
    public String getName()  {
        return _name;
    }

    public void setName(String value)  {
        _name = value;
    }

    public IdentifierToken(String module, int line, int column, String val)  {
        super(module, line, column);
        _name = val;
    }

    protected String describe()  {
        return "Identifier:" + _name;
    }

}


