//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.KeywordToken;

public class TypeKeywordToken  extends KeywordToken 
{
    public TypeKeywordToken(String module, int line, int column)  {
        super(module, line, column);
    }

    protected String describe()  {
        return "TypeKeyword";
    }

}


