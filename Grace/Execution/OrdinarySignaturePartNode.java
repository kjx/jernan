//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.OrdinarySignaturePartParseNode;
import Grace.MethodHelper;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import Grace.Execution.Node;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.SignaturePartNode;

/**
* A literal method signature part
*/
public class OrdinarySignaturePartNode  extends SignaturePartNode 
{
    private String _name;
    private String _baseName;
    /**
    * Name of the part
    */
    public String getName()  {
        return _name;
    }
    public String getBaseName()  {
        return _baseName;
    }

    /**
    * Generic parameters of this part
    */
    private List<Node> __GenericParameters;
    public List<Node> getGenericParameters() {
        return __GenericParameters;
    }

    public void setGenericParameters(List<Node> value) {
        __GenericParameters = value;
    }

    /**
    * Ordinary parameters of this part
    */
    private List<Node> __Parameters;
    public List<Node> getParameters() {
        return __Parameters;
    }

    public void setParameters(List<Node> value) {
        __Parameters = value;
    }

    public OrdinarySignaturePartNode(Token location, OrdinarySignaturePartParseNode source, List<Node> parameters, List<Node> genericParameters)  {
        super(location, source);
        _baseName = source.getName();
        _name = MethodHelper.ArityNamePart(source.getName(), parameters.size());
        setParameters(parameters);
        setGenericParameters(genericParameters);
    }

    public OrdinarySignaturePartNode(Token location, OrdinarySignaturePartParseNode source, List<Node> parameters, List<Node> genericParameters, boolean allowArityOverloading)  {
        super(location, source);
        _baseName = source.getName();
        _name = allowArityOverloading ? MethodHelper.ArityNamePart(source.getName(), parameters.size()) : source.getName();
        setParameters(parameters);
        setGenericParameters(genericParameters);
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Part: " + getName());
        for (Node p : getParameters())
            p.debugPrint(tw, prefix + "    ");
    }

}


