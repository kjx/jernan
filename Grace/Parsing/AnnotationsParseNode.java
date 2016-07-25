//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import Grace.Parsing.StringSupport;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ParseNodeVisitor;
import Grace.Parsing.Token;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
/**
* Parse node for a list of annotations
*/
public class AnnotationsParseNode  extends ParseNode 
{
    List<ParseNode> _annotations = new ArrayList<ParseNode>();
    /**
    * The annotations in this collection
    */
    public List<ParseNode> getAnnotations() throws Exception {
        return _annotations;
    }

    public void setAnnotations(List<ParseNode> value) throws Exception {
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


