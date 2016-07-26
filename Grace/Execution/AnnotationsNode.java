//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:25 a.m.
//

package Grace.Execution;
import Grace.Execution.AnnotationsNode;
import Grace.Execution.Node;
import Grace.Parsing.Token;
import Grace.Parsing.AnnotationsParseNode;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

/**
* A group of "is" annotations.
*/
public class AnnotationsNode  extends Node implements Iterable<Node>
{
    private List<Node> annotations = new ArrayList<Node>();
    public AnnotationsNode(Token location, AnnotationsParseNode source)  {
        super(location, source);
    }

    /**
    * Add an annotation.
    * 
    *  @param ann Annotation
    */
    public void addAnnotation(Node ann)  {
        annotations.add(ann);
    }

    /**
    * Add many annotations at once.
    * 
    *  @param anns Enumerable of annotations
    */
    public void addAnnotations(Collection<Node> anns)  {
        annotations.addAll(anns);
    }

    /**
    * Get an enumerator giving each annotation in turn.
    */
    public Iterator<Node> iterator()  {
        return annotations.iterator();
    }

    /**
    * Number of annotations in this group.
    */
    public int size()  {
        return annotations.size();
    }

    /**
    * 
    */
    public void debugPrint(PrintStream tw, String prefix)  {
        tw.println(prefix + "Annotations:");
        for (Node a : annotations) 
            a.debugPrint(tw, prefix + "    ");
    }

}


