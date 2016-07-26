//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.SignaturePartParseNode;
import java.io.PrintStream;


import Grace.Execution.Node;

/**
* A component of a method signature.
*/
public abstract class SignaturePartNode  extends Node 
{
    /**
    * Name of the part
    */
    public abstract String getName()  ;

    public SignaturePartNode(Token location, SignaturePartParseNode source)  {
        super(location, source);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "SignaturePart: " + getName());
    }
}


