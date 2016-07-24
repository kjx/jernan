//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class OperatorToken  extends Token 
{
    private String _name;
    public String getName() throws Exception {
        return _name;
    }

    public void setName(String value) throws Exception {
        _name = value;
    }

    private boolean _spaceBefore;
    public boolean getSpaceBefore() throws Exception {
        return _spaceBefore;
    }

    public void setSpaceBefore(boolean value) throws Exception {
        _spaceBefore = value;
    }

    private boolean _spaceAfter;
    public boolean getSpaceAfter() throws Exception {
        return _spaceAfter;
    }

    public void setSpaceAfter(boolean value) throws Exception {
        _spaceAfter = value;
    }

    public OperatorToken(String module, int line, int column, String val) throws Exception {
        super(module, line, column);
        _name = val;
    }

    /**
    * Set whether spaces were found before and after
    * this operator symbol
    */
    public void setSpacing(boolean before, boolean after) throws Exception {
        _spaceBefore = before;
        _spaceAfter = after;
    }

    protected String describe() throws Exception {
        return "Operator:" + _name;
    }

}


