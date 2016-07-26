//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.IdentifierToken;

public class AsToken  extends IdentifierToken 
{
    public AsToken(String module, int line, int column)  {
        super(module, line, column, "as");
    }

    protected String describe()  {
        return "As (contextual keyword 'as')";
    }

}


