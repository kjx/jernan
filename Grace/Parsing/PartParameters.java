//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
import Grace.Parsing.ParseNode;

/**
* Combined ordinary and generic parameters of
* a method name part
*/
public class PartParameters   
{
    public PartParameters() {
    }

    private CSList<ParseNode> _generics;
    /**
    * Generic parameters
    */
    public CSList<ParseNode> getGenerics() throws Exception {
        return _generics;
    }

    public void setGenerics(CSList<ParseNode> value) throws Exception {
        _generics = value;
    }

    private CSList<ParseNode> _ordinary;
    /**
    * Ordinary parameters
    */
    public CSList<ParseNode> getOrdinary() throws Exception {
        return _ordinary;
    }

    public void setOrdinary(CSList<ParseNode> value) throws Exception {
        _ordinary = value;
    }

    /**
    * @param g Generic parameters
    *  @param o Ordinary parameters
    */
    public PartParameters(CSList<ParseNode> g, CSList<ParseNode> o) throws Exception {
        _generics = g;
        _ordinary = o;
    }

}


