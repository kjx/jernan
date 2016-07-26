//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class LGenericToken  extends Token 
{
    public LGenericToken(String module, int line, int column)  {
        super(module, line, column);
    }

    protected String describe()  {
        return "LGeneric";
    }

}


