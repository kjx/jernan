//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:37 a.m.
//

package Grace.Parsing;

import Grace.Parsing.CheckingParseNodeVisitor;
import Grace.Parsing.ExplicitReceiverRequestParseNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ImplicitReceiverRequestParseNode;
import Grace.Parsing.InheritsParseNode;
import Grace.Parsing.OperatorParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.PrefixOperatorParseNode;
import java.util.HashMap;

public class NonReceiverNameCheckingParseNodeVisitor  extends CheckingParseNodeVisitor 
{
    private HashSet<String> _names = new HashSet<String>();
    public NonReceiverNameCheckingParseNodeVisitor(HashSet<String> names) throws Exception {
        _names = names;
    }

    /**
    * 
    */
    public ParseNode visit(IdentifierParseNode ipn) throws Exception {
        // A bare identifier that matches an element of the
        // set of disallowed names will always raise an error,
        // but the other cases below avoid visiting such a
        // node if it is in a valid place.
        if (_names.Contains(ipn.getName()))
            ErrorReporting.ReportStaticError(ipn.getToken().getModule(), ipn.getLine(), "P1043", new HashMap<String,String>{ { "name", ipn.getName() } }, "Invalid use of parent name");
         
        return ipn;
    }

    /**
    * 
    */
    public ParseNode visit(ImplicitReceiverRequestParseNode irrpn) throws Exception {
        if (irrpn.getNameParts().size() != 1)
            return super.visit(irrpn);
         
        // A single-part name could be a banned identifier.
        IdentifierParseNode n = (IdentifierParseNode)irrpn.getNameParts().get(0);
        visit(n);
        for (List<ParseNode> args : irrpn.getArguments())
            for (ParseNode a : args)
                // The arguments require checking either way.
                a.Visit(this);
        for (List<ParseNode> args : irrpn.getGenericArguments())
            for (ParseNode a : args)
                a.Visit(this);
        return irrpn;
    }

    /**
    * 
    */
    public ParseNode visit(ExplicitReceiverRequestParseNode errpn) throws Exception {
        if (!(errpn.getReceiver() instanceof IdentifierParseNode))
        {
            return super.visit(errpn);
        }
         
        for (List<ParseNode> args : errpn.getArguments())
            for (ParseNode a : args)
                // If the receiver was an identifier, we only
                // want to look at arguments for possible
                // problems - the receiver is OK.
                a.Visit(this);
        for (List<ParseNode> args : errpn.getGenericArguments())
            for (ParseNode a : args)
                a.Visit(this);
        return errpn;
    }

    /**
    * 
    */
    public ParseNode visit(OperatorParseNode opn) throws Exception {
        if (!(opn.getLeft() instanceof IdentifierParseNode))
        {
            return super.visit(opn);
        }
         
        // If the left-hand side (the receiver) was
        // an identifier, only check the right.
        opn.getRight().Visit(this);
        return opn;
    }

    /**
    * 
    */
    public ParseNode visit(PrefixOperatorParseNode popn) throws Exception {
        // If the receiver is an identifier, it's ok,
        // and we just return successfully.
        if (popn.getReceiver() instanceof IdentifierParseNode)
            return popn;
         
        return popn.getReceiver().Visit(this);
    }

    /**
    * 
    */
    public ParseNode visit(InheritsParseNode ipn) throws Exception {
        return ipn.getFrom().Visit(this);
    }

}


// Only the request part should be scanned.