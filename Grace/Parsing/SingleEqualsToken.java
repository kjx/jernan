//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.Token;

public class SingleEqualsToken  extends Token 
{
    public SingleEqualsToken(String module, int line, int column) throws Exception {
        super(module, line, column);
    }

    protected String describe() throws Exception {
        return "SingleEquals";
    }

}


