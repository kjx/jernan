//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 2:46:36 a.m.
//

package Grace.Parsing;

import Grace.Parsing.AliasParseNode;
import Grace.Parsing.BindParseNode;
import Grace.Parsing.BlockParseNode;
import Grace.Parsing.ClassDeclarationParseNode;
import Grace.Parsing.CommentParseNode;
import Grace.Parsing.DefDeclarationParseNode;
import Grace.Parsing.DialectParseNode;
import Grace.Parsing.ExcludeParseNode;
import Grace.Parsing.ExplicitBracketRequestParseNode;
import Grace.Parsing.ExplicitReceiverRequestParseNode;
import Grace.Parsing.IdentifierParseNode;
import Grace.Parsing.ImplicitBracketRequestParseNode;
import Grace.Parsing.ImplicitReceiverRequestParseNode;
import Grace.Parsing.ImportParseNode;
import Grace.Parsing.InheritsParseNode;
import Grace.Parsing.InterpolatedStringParseNode;
import Grace.Parsing.MethodDeclarationParseNode;
import Grace.Parsing.NumberParseNode;
import Grace.Parsing.ObjectParseNode;
import Grace.Parsing.OperatorParseNode;
import Grace.Parsing.OrdinarySignaturePartParseNode;
import Grace.Parsing.ParenthesisedParseNode;
import Grace.Parsing.ParseNode;
import Grace.Parsing.PrefixOperatorParseNode;
import Grace.Parsing.ReturnParseNode;
import Grace.Parsing.SignatureParseNode;
import Grace.Parsing.StringLiteralParseNode;
import Grace.Parsing.TraitDeclarationParseNode;
import Grace.Parsing.TypedParameterParseNode;
import Grace.Parsing.TypeParseNode;
import Grace.Parsing.TypeStatementParseNode;
import Grace.Parsing.UsesParseNode;
import Grace.Parsing.VarDeclarationParseNode;

import Grace.Execution.Node;

/**
* Visitor for tree of ParseNodesType nodes are mapped to
*/
public interface ParseNodeVisitor <T>  
{
    /**
    * Visit a ParseNode
    *  @param p ParseNode to visit
    */
    T visit(ParseNode p)  ;

    /**
    * Visit an ObjectParseNode
    *  @param o ObjectParseNode to visit
    */
    T visit(ObjectParseNode o)  ;

    /**
    * Visit a NumberParseNode
    *  @param n NumberParseNode to visit
    */
    T visit(NumberParseNode n)  ;

    /**
    * Visit a MethodDeclarationParseNode
    *  @param d MethodDeclarationParseNode to visit
    */
    T visit(MethodDeclarationParseNode d)  ;

    /**
    * Visit an IdentifierParseNode
    *  @param i IdentifierParseNode to visit
    */
    T visit(IdentifierParseNode i)  ;

    /**
    * Visit an ImplicitReceiverRequestParseNode
    *  @param irrpn ImplicitReceiverRequestParseNode to visit
    */
    T visit(ImplicitReceiverRequestParseNode irrpn)  ;

    /**
    * Visit an ExplicitReceiverRequestParseNode
    *  @param errpn ExplicitReceiverRequestParseNode to visit
    */
    T visit(ExplicitReceiverRequestParseNode errpn)  ;

    /**
    * Visit an OperatorParseNode
    *  @param opn OperatorParseNode to visit
    */
    T visit(OperatorParseNode opn)  ;

    /**
    * Visit a TypedParameterParseNode
    *  @param tppn TypedParameterParseNode to visit
    */
    T visit(TypedParameterParseNode tppn)  ;

    /**
    * Visit a StringLiteralParseNode
    *  @param slpn StringLiteralParseNode to visit
    */
    T visit(StringLiteralParseNode slpn)  ;

    /**
    * Visit an InterpolatedStringParseNode
    *  @param ispn InterpolatedStringParseNode to visit
    */
    T visit(InterpolatedStringParseNode ispn)  ;

    /**
    * Visit a VarDeclarationParseNode
    *  @param vdpn VarDeclarationParseNode to visit
    */
    T visit(VarDeclarationParseNode vdpn)  ;

    /**
    * Visit a DefDeclarationParseNode
    *  @param vdpn DefDeclarationParseNode to visit
    */
    T visit(DefDeclarationParseNode vdpn)  ;

    /**
    * Visit a BindParseNode
    *  @param bpn BindParseNode to visit
    */
    T visit(BindParseNode bpn)  ;

    /**
    * Visit a PrefixOperatorParseNode
    *  @param popn PrefixOperatorParseNode to visit
    */
    T visit(PrefixOperatorParseNode popn)  ;

    /**
    * Visit a BlockParseNode
    *  @param bpn BlockParseNode to visit
    */
    T visit(BlockParseNode bpn)  ;

    /**
    * Visit a ClassDeclarationParseNode
    *  @param bpn ClassDeclarationParseNode to visit
    */
    T visit(ClassDeclarationParseNode bpn)  ;

    /**
    * Visit a TraitDeclarationParseNode
    *  @param bpn TraitDeclarationParseNode to visit
    */
    T visit(TraitDeclarationParseNode bpn)  ;

    /**
    * Visit a ReturnParseNode
    *  @param rpn ReturnParseNode to visit
    */
    T visit(ReturnParseNode rpn)  ;

    /**
    * Visit a CommentParseNode
    *  @param cpn CommentParseNode to visit
    */
    T visit(CommentParseNode cpn)  ;

    /**
    * Visit a TypeStatementParseNode
    *  @param tspn TypeStatementParseNode to visit
    */
    T visit(TypeStatementParseNode tspn)  ;

    /**
    * Visit a TypeParseNode
    *  @param tpn TypeParseNode to visit
    */
    T visit(TypeParseNode tpn)  ;

    /**
    * Visit a ImportParseNode
    *  @param ipn ImportParseNode to visit
    */
    T visit(ImportParseNode ipn)  ;

    /**
    * Visit a DialectParseNode
    *  @param dpn DialectParseNode to visit
    */
    T visit(DialectParseNode dpn)  ;

    /**
    * Visit an InheritsParseNode
    *  @param ipn InheritsParseNode to visit
    */
    T visit(InheritsParseNode ipn)  ;

    /**
    * Visit a UsesParseNode
    *  @param upn UsesParseNode to visit
    */
    T visit(UsesParseNode upn)  ;

    /**
    * Visit an AliasParseNode
    *  @param ipn AliasParseNode to visit
    */
    T visit(AliasParseNode ipn)  ;

    /**
    * Visit an ExcludeParseNode
    *  @param ipn ExcludeParseNode to visit
    */
    T visit(ExcludeParseNode ipn)  ;

    /**
    * Visit a ParenthesisedParseNode
    *  @param ppn ParenthesisedParseNode to visit
    */
    T visit(ParenthesisedParseNode ppn)  ;

    /**
    * Visit an ImplicitBracketRequestParseNode
    *  @param ibrpn ImplicitBracketRequestParseNode to visit
    */
    T visit(ImplicitBracketRequestParseNode ibrpn)  ;

    /**
    * Visit an ExplicitBracketRequestParseNode
    *  @param ebrpn ExplicitBracketRequestParseNode to visit
    */
    T visit(ExplicitBracketRequestParseNode ebrpn)  ;

    /**
    * Visit a SignatureParseNode
    *  @param spn SignatureParseNode to visit
    */
    T visit(SignatureParseNode spn)  ;

    /**
    * Visit an OrdinarySignaturePartParseNode
    *  @param osppn OrdinarySignaturePartParseNode to visit
    */
    T visit(OrdinarySignaturePartParseNode osppn)  ;

}


