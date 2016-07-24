//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.StringSupport;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;

/**
* Parse node for a list of annotations
*/
public class AnnotationsParseNode  extends ParseNode 
{
    CSList<ParseNode> _annotations = new CSList<ParseNode>();
    /**
    * The annotations in this collection
    */
    public CSList<ParseNode> getAnnotations() throws Exception {
        return _annotations;
    }

    public void setAnnotations(CSList<ParseNode> value) throws Exception {
        _annotations = value;
    }

    public AnnotationsParseNode(Token tok) throws Exception {
        super(tok);
    }

    /**
    * Add an annotation to this collection
    *  @param ann Annotation to add
    */
    public void addAnnotation(ParseNode ann) throws Exception {
        _annotations.add(ann);
    }

    /**
    * Check for a named annotation
    *  @param name Annotation to search for
    */
    public boolean hasAnnotation(String name) throws Exception {
        for (ParseNode p : _annotations)
        {
            IdentifierParseNode aid = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            if (aid != null)
            {
                if (StringSupport.equals(aid.getName(), name))
                    return true;
                 
            }
             
        }
        return false;
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix) throws Exception {
        tw.println(prefix + "Annotations:");
        for (ParseNode ann : _annotations)
            ann.debugPrint(tw,prefix + "    ");
        writeComment(tw,prefix);
    }

    /**
    * 
    */
    public <T>T visit(ParseNodeVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

}


