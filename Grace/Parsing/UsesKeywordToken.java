//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.KeywordToken;

public class UsesKeywordToken  extends KeywordToken 
{
    public UsesKeywordToken(String module, int line, int column) throws Exception {
        super(module, line, column);
    }

    protected String describe() throws Exception {
        return "UsesKeyword";
    }

}


