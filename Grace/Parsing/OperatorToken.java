//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class OperatorToken  extends Token 
{
    private String _name;
    public String getName()  {
        return _name;
    }

    public void setName(String value)  {
        _name = value;
    }

    private boolean _spaceBefore;
    public boolean getSpaceBefore()  {
        return _spaceBefore;
    }

    public void setSpaceBefore(boolean value)  {
        _spaceBefore = value;
    }

    private boolean _spaceAfter;
    public boolean getSpaceAfter()  {
        return _spaceAfter;
    }

    public void setSpaceAfter(boolean value)  {
        _spaceAfter = value;
    }

    public OperatorToken(String module, int line, int column, String val)  {
        super(module, line, column);
        _name = val;
    }

    /**
    * Set whether spaces were found before and after
    * this operator symbol
    */
    public void setSpacing(boolean before, boolean after)  {
        _spaceBefore = before;
        _spaceAfter = after;
    }

    protected String describe()  {
        return "Operator:" + _name;
    }

}


