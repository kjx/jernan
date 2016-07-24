//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class NumberToken  extends Token 
{
    private int _base;
    public int getNumericBase() throws Exception {
        return _base;
    }

    public void setNumericBase(int value) throws Exception {
        _base = value;
    }

    private String _digits;
    public String getDigits() throws Exception {
        return _digits;
    }

    public void setDigits(String value) throws Exception {
        _digits = value;
    }

    public NumberToken(String module, int line, int column, int b, String digits) throws Exception {
        super(module, line, column);
        _base = b;
        this._digits = digits;
    }

    protected String describe() throws Exception {
        String ret = "Number:";
        if (_base == 10)
            ret += _digits;
        else if (_base == 16)
            ret += "0x" + _digits;
        else
            ret += _base + "x" + _digits;  
        return ret;
    }

}


