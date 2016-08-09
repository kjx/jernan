//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;
import Grace.StringInfo;

public class BracketToken  extends Token 
{
    private String _name;
    private String _other;
    public String getName()  {
        return _name;
    }

    public void setName(String value)  {
        _name = value;
    }

    private boolean __Opening;
    public boolean getOpening() {
        return __Opening;
    }

    public void setOpening(boolean value) {
        __Opening = value;
    }

    private boolean __Closing;
    public boolean getClosing() {
        return __Closing;
    }

    public void setClosing(boolean value) {
        __Closing = value;
    }

    public String getOther()  {
        if (_other == null)
        {
            _other = computeOther();
        }
         
        return _other;
    }

    public BracketToken(String module, int line, int column, String val)  {
        super(module, line, column);
        _name = val;
    }

    private String computeOther()  {
	System.out.println("in computeOther " + _name);
        StringBuilder sb = new StringBuilder();
        int[] graphemeIndices = StringInfo.ParseCombiningCharacters(_name);
        for (int i = graphemeIndices.length - 1;i >= 0;i--)
        {
            String c = StringInfo.GetNextTextElement(_name, i);
            if (UnicodeLookup.MirroredBrackets.containsKey(c))
                sb.append(UnicodeLookup.MirroredBrackets.get(c));
            else
                sb.append(c); 
        }
	System.out.println("in computeOther, returning " +sb.toString());
        return sb.toString();
    }

    protected String describe()  {
        return "Bracket:" + _name;
    }

}


