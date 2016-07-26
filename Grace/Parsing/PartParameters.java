//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import java.util.List;
import java.util.ArrayList;

import Grace.Parsing.ParseNode;

/**
* Combined ordinary and generic parameters of
* a method name part
*/
public class PartParameters   
{
    public PartParameters() {
    }

    private List<ParseNode> _generics;
    /**
    * Generic parameters
    */
    public List<ParseNode> getGenerics()  {
        return _generics;
    }

    public void setGenerics(List<ParseNode> value)  {
        _generics = value;
    }

    private List<ParseNode> _ordinary;
    /**
    * Ordinary parameters
    */
    public List<ParseNode> getOrdinary()  {
        return _ordinary;
    }

    public void setOrdinary(List<ParseNode> value)  {
        _ordinary = value;
    }

    /**
    * @param g Generic parameters
    *  @param o Ordinary parameters
    */
    public PartParameters(List<ParseNode> g, List<ParseNode> o)  {
        _generics = g;
        _ordinary = o;
    }

}


