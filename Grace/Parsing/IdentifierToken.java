//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class IdentifierToken  extends Token 
{
    private String _name;
    public String getName() throws Exception {
        return _name;
    }

    public void setName(String value) throws Exception {
        _name = value;
    }

    public IdentifierToken(String module, int line, int column, String val) throws Exception {
        super(module, line, column);
        _name = val;
    }

    protected String describe() throws Exception {
        return "Identifier:" + _name;
    }

}


