//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class StringToken  extends Token 
{
    private String _value;
    public String getValue()  {
        return _value;
    }

    public void setValue(String value)  {
        _value = value;
    }

    private String _raw;
    public String getRaw()  {
        return _raw;
    }

    public void setRaw(String value)  {
        _raw = value;
    }

    private boolean _beginsInterpolation = false;
    public boolean getBeginsInterpolation()  {
        return _beginsInterpolation;
    }

    public void setBeginsInterpolation(boolean value)  {
        _beginsInterpolation = value;
    }

    public StringToken(String module, int line, int column, String val)  {
        super(module, line, column);
        _value = val;
    }

    public StringToken(String module, int line, int column, String val, boolean interp)  {
        super(module, line, column);
        _value = val;
        _beginsInterpolation = interp;
    }

    public StringToken(String module, int line, int column, String val, String raw)  {
        super(module, line, column);
        _value = val;
        this._raw = raw;
    }

    public StringToken(String module, int line, int column, String val, String raw, boolean interp)  {
        super(module, line, column);
        _value = val;
        this._raw = raw;
        _beginsInterpolation = interp;
    }

    protected String describe()  {
        return "String:" + _value;
    }

}


