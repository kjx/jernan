//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class CommentToken  extends Token 
{
    private String _value;
    public String getValue()  {
        return _value;
    }

    public void setValue(String value)  {
        _value = value;
    }

    public CommentToken(String module, int line, int column, String val)  {
        super(module, line, column);
        _value = val;
    }

    protected String describe()  {
        return "Comment:" + _value;
    }

}


