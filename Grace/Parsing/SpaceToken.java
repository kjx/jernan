//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class SpaceToken  extends Token 
{
    private int _size;
    public int getSize()  {
        return _size;
    }

    public void setSize(int value)  {
        _size = value;
    }

    public SpaceToken(String module, int line, int column, int size)  {
        super(module, line, column);
        this._size = size;
    }

    protected String describe()  {
        return "Space:" + _size;
    }

}


