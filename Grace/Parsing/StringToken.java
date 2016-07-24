//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class StringToken  extends Token 
{
    private String _value;
    public String getValue() throws Exception {
        return _value;
    }

    public void setValue(String value) throws Exception {
        _value = value;
    }

    private String _raw;
    public String getRaw() throws Exception {
        return _raw;
    }

    public void setRaw(String value) throws Exception {
        _raw = value;
    }

    private boolean _beginsInterpolation = false;
    public boolean getBeginsInterpolation() throws Exception {
        return _beginsInterpolation;
    }

    public void setBeginsInterpolation(boolean value) throws Exception {
        _beginsInterpolation = value;
    }

    public StringToken(String module, int line, int column, String val) throws Exception {
        super(module, line, column);
        _value = val;
    }

    public StringToken(String module, int line, int column, String val, boolean interp) throws Exception {
        super(module, line, column);
        _value = val;
        _beginsInterpolation = interp;
    }

    public StringToken(String module, int line, int column, String val, String raw) throws Exception {
        super(module, line, column);
        _value = val;
        this._raw = raw;
    }

    public StringToken(String module, int line, int column, String val, String raw, boolean interp) throws Exception {
        super(module, line, column);
        _value = val;
        this._raw = raw;
        _beginsInterpolation = interp;
    }

    protected String describe() throws Exception {
        return "String:" + _value;
    }

}


