//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:26 a.m.
//

package Grace.Execution;

import Grace.ErrorReporting;
import static Grace.ErrorReporting.hash;
import Grace.Parsing.*;  //I claim this is defensible because it needs it all!
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import Grace.StringSupport;

import Grace.Execution.AnnotationsNode;
import Grace.Execution.BlockNode;
import Grace.Execution.DefDeclarationNode;
import Grace.Execution.DialectNode;
import Grace.Execution.ExplicitReceiverRequestNode;
import Grace.Execution.ForDoRequestNode;
import Grace.Execution.IfThenRequestNode;
import Grace.Execution.ImplicitReceiverRequestNode;
import Grace.Execution.ImportNode;
import Grace.Execution.InheritsNode;
import Grace.Execution.MethodNode;
import Grace.Execution.Node;
import Grace.Execution.NoopNode;
import Grace.Execution.NumberLiteralNode;
import Grace.Execution.ObjectConstructorNode;
import Grace.Execution.OrdinarySignaturePartNode;
import Grace.Execution.ParameterNode;
import Grace.Execution.RequestNode;
import Grace.Execution.RequestPartNode;
import Grace.Execution.ReturnNode;
import Grace.Execution.SignatureNode;
import Grace.Execution.SignaturePartNode;
import Grace.Execution.StringLiteralNode;
import Grace.Execution.TypeNode;
import Grace.Execution.VarDeclarationNode;

/**
* Translates a tree of ParseNodes into Nodes
*/
public class ExecutionTreeTranslator implements ParseNodeVisitor<Node> 
{
    private ObjectConstructorNode module;
    /**
    * Translate a tree rooted at a parse node for an
    * object into the corresponding Node tree
    *  @param obj Root of the tree
    */
    public Node translate(ObjectParseNode obj) throws Exception {
        return obj.visit(this);
    }

    /**
    * Default visit, which reports an error
    */
    public Node visit(ParseNode pn) throws Exception {
        throw new Exception("No ParseNodeVisitor override provided for " + pn);
    }

    /**
    * 
    */
    public Node visit(ObjectParseNode obj) throws Exception {
        ObjectConstructorNode ret = new ObjectConstructorNode(obj.getToken(), obj);
        if (module == null)
            module = ret;
         
        for (Object __dummyForeachVar0 : obj.getBody())
        {
            ParseNode p = (ParseNode)__dummyForeachVar0;
            Node n = p.visit(this);
            if (!(p instanceof CommentParseNode))
                ret.add(n);
             
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(NumberParseNode n) throws Exception {
        return new NumberLiteralNode(n.getToken(),n);
    }

    /**
    * 
    */
    public Node visit(StringLiteralParseNode n) throws Exception {
        return new StringLiteralNode(n.getToken(),n);
    }

    /**
    * 
    */
    public Node visit(InterpolatedStringParseNode n) throws Exception {
        Node ret = null;
        for (Object __dummyForeachVar1 : n.getParts())
        {
            ParseNode part = (ParseNode)__dummyForeachVar1;
            if (ret == null)
            {
                ret = part.visit(this);
            }
            else
            {
                ExplicitReceiverRequestNode errn = new ExplicitReceiverRequestNode(n.getToken(), n, ret);
                List<Node> args = new ArrayList<Node>();
                if (!(part instanceof StringLiteralParseNode))
                {
                    RequestPartNode rpnAS = new RequestPartNode("asString", new ArrayList<Node>(), new ArrayList<Node>());
                    ExplicitReceiverRequestNode errnAS = new ExplicitReceiverRequestNode(n.getToken(), n, part.visit(this));
                    errnAS.addPart(rpnAS);
                    args.add(errnAS);
                }
                else
                {
                    args.add(part.visit(this));
                } 
                RequestPartNode rpn = new RequestPartNode("++", new ArrayList<Node>(), args);
                errn.addPart(rpn);
                ret = errn;
            } 
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(SignatureParseNode spn) throws Exception {
        SignatureNode ret = new SignatureNode(spn.getToken(),spn);
        for (SignaturePartParseNode part : spn.getParts())
        {
            ret.addPart((SignaturePartNode)part.visit(this));
        }
        if (spn.getReturnType() != null)
            ret.setReturnType(spn.getReturnType().visit(this));
         
        addAnnotations(spn.getAnnotations(), ret.getAnnotations());
        return ret;
    }

    private void addAnnotations(AnnotationsParseNode source, AnnotationsNode dest) throws Exception {
        if (source != null)
            dest.addAnnotations(source.getAnnotations().stream()
				.map(x -> x.visit(this))
				.collect(Collectors.toList()));
    }

    /**
    * 
    */
    public Node visit(OrdinarySignaturePartParseNode osppn) throws Exception {
        List<Node> parameters = new ArrayList<Node>();
        for (ParseNode p : osppn.getParameters())
        {
            // f
	    IdentifierParseNode id = p instanceof IdentifierParseNode 
		? (IdentifierParseNode)p : (IdentifierParseNode)null;

	    TypedParameterParseNode tppn = p instanceof TypedParameterParseNode
		? (TypedParameterParseNode)p : (TypedParameterParseNode)null;

	    VarArgsParameterParseNode vappn = p instanceof VarArgsParameterParseNode
		? (VarArgsParameterParseNode)p : (VarArgsParameterParseNode)null;
            if (id != null)
                parameters.add(new ParameterNode(id.getToken(), id));
            else if (tppn != null)
            {
                parameters.add(
   	          new ParameterNode(
                     tppn.getToken(), 
		     tppn.getName() instanceof IdentifierParseNode
   		        ? (IdentifierParseNode)tppn.getName()
			: (IdentifierParseNode)null, 
		     tppn.getType().visit(this)));
            }
            else if (vappn != null)
            {
                // Inside could be either an identifier or a
                // TypedParameterParseNode - check for both.
                IdentifierParseNode inIPN = 
		    vappn.getName() instanceof IdentifierParseNode
		      ? (IdentifierParseNode)vappn.getName()
		      : (IdentifierParseNode)null;
		TypedParameterParseNode inTPPN =
		    vappn.getName() instanceof TypedParameterParseNode
		      ? (TypedParameterParseNode)vappn.getName() 
		      : (TypedParameterParseNode)null;

                if (inIPN != null)
                    parameters.add(
		      new ParameterNode(inIPN.getToken(), inIPN, true));
                else // Variadic
                if (inTPPN != null)
                    // Variadic
                    parameters.add(
		      new ParameterNode(
		        inTPPN.getToken(),
			inTPPN.getName() instanceof IdentifierParseNode
			  ? (IdentifierParseNode)inTPPN.getName()
			  : (IdentifierParseNode)null, 
			true, 
			inTPPN.getType().visit(this)));
                  
            }
            else
            {
                throw new Exception("unimplemented - unusual parameters");
            }   
        }
        List<Node>  generics = new ArrayList<Node>();
        for (ParseNode p : osppn.getGenericParameters())
        {
            IdentifierParseNode id = p instanceof IdentifierParseNode
		? (IdentifierParseNode)p 
		: (IdentifierParseNode)null;
            if (id != null)
            {
                generics.add(new ParameterNode(id.getToken(), id));
            }
            else
            {
                throw new Exception("unimplemented - bad generic parameters");
            } 
        }
        if (osppn.getName().startsWith("circumfix"))
            return new OrdinarySignaturePartNode(osppn.getToken(), osppn, parameters, generics, false);
         
        return new OrdinarySignaturePartNode(osppn.getToken(), osppn, parameters, generics);
    }

    /**
    * 
    */
    public Node visit(MethodDeclarationParseNode d) throws Exception {
        MethodNode ret = new MethodNode(d.getToken(), d);
        SignatureNode sig = (SignatureNode)d.getSignature().visit(this);
        ret.setSignature(sig);
        ret.setAnnotations(sig.getAnnotations());
        String name = sig.getName();
        ret.setConfidential((d.getSignature().getAnnotations() != null && d.getSignature().getAnnotations().hasAnnotation("confidential")));
        ret.setAbstract((d.getSignature().getAnnotations() != null && d.getSignature().getAnnotations().hasAnnotation("abstract")));
        for (Object __dummyForeachVar5 : d.getBody())
        {
            ParseNode p = (ParseNode)__dummyForeachVar5;
            if (!(p instanceof CommentParseNode))
                ret.add(p.visit(this));
             
        }
        // Indicate whether this method returns a fresh object
        ret.setFresh((d.getBody().size() > 0
		      && d.getBody().get(d.getBody().size() - 1) instanceof ObjectParseNode));
        return ret;
    }

    /**
    * 
    */
    public Node visit(IdentifierParseNode i) throws Exception {
        ImplicitReceiverRequestNode ret = new ImplicitReceiverRequestNode(i.getToken(), i);
        RequestPartNode rpn = new RequestPartNode(i.getName(), new ArrayList<Node>(), new ArrayList<Node>());
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ImplicitReceiverRequestParseNode irrpn) throws Exception {
        ImplicitReceiverRequestNode ret = null;
        if (StringSupport.equals(irrpn.getName(), "if then"))
        {
            ret = new IfThenRequestNode(irrpn.getToken(), irrpn);
        }
        else if (StringSupport.equals(irrpn.getName(), "for do"))
        {
            ret = new ForDoRequestNode(irrpn.getToken(), irrpn);
        }
        else
        {
            ret = new ImplicitReceiverRequestNode(irrpn.getToken(), irrpn);
        }  
        for (int i = 0;i < irrpn.getArguments().size();i++)
        {
            RequestPartNode rpn = 
              new RequestPartNode(
		((IdentifierParseNode)irrpn.getNameParts().get(i)).getName(), 
		map(irrpn.getGenericArguments().get(i)), 
		map(irrpn.getArguments().get(i)));
            ret.addPart(rpn);
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(ExplicitReceiverRequestParseNode irrpn) throws Exception {
        ExplicitReceiverRequestNode ret = 
	    new ExplicitReceiverRequestNode(
	      irrpn.getToken(), irrpn, irrpn.getReceiver().visit(this));
        for (int i = 0;i < irrpn.getArguments().size();i++)
        {
            RequestPartNode rpn = 
              new RequestPartNode(
  	        ((IdentifierParseNode)irrpn.getNameParts().get(i)).getName(),
		map(irrpn.getGenericArguments().get(i)),
		map(irrpn.getArguments().get(i)));
            ret.addPart(rpn);
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(PrefixOperatorParseNode popn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(popn.getToken(), popn, popn.getReceiver().visit(this));
        RequestPartNode rpn = new RequestPartNode("prefix" + popn.getName(), new ArrayList<Node>(), new ArrayList<Node>());
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(OperatorParseNode opn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(opn.getToken(), opn, opn.getLeft().visit(this));
        List<Node> args = new ArrayList<Node>();
        args.add(opn.getRight().visit(this));
        RequestPartNode rpn = new RequestPartNode(opn.name, new ArrayList<Node>(), args);
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(VarDeclarationParseNode vdpn) throws Exception {
        Node val = null;
        Node type = null;
        if (vdpn.getValue() != null)
            val = vdpn.getValue().visit(this);
         
        if (vdpn.getType() != null)
            type = vdpn.getType().visit(this);
         
        VarDeclarationNode ret = new VarDeclarationNode(vdpn.getToken(),vdpn,val,type);
        addAnnotations(vdpn.getAnnotations(), ret.getAnnotations());
        if (vdpn.getAnnotations() != null && vdpn.getAnnotations().hasAnnotation("public"))
        {
            ret.setReadable(true);
            ret.setWritable(true);
        }
        else
        {
            ret.setReadable((vdpn.getAnnotations() != null && vdpn.getAnnotations().hasAnnotation("readable")));
            ret.setWritable((vdpn.getAnnotations() != null && vdpn.getAnnotations().hasAnnotation("writable")));
        } 
        return ret;
    }

    /**
    * 
    */
    public Node visit(DefDeclarationParseNode vdpn) throws Exception {
        Node val = null;
        Node type = null;
        if (vdpn.getValue() != null)
            val = vdpn.getValue().visit(this);
         
        if (vdpn.getType() != null)
            type = vdpn.getType().visit(this);
         
        DefDeclarationNode ret = new DefDeclarationNode(vdpn.getToken(),vdpn,val,type);
        addAnnotations(vdpn.getAnnotations(), ret.getAnnotations());
        if (vdpn.getAnnotations() != null && vdpn.getAnnotations().hasAnnotation("public"))
            ret.setPublic(true);
         
        ret.setPublic((vdpn.getAnnotations() != null && (vdpn.getAnnotations().hasAnnotation("public") || vdpn.getAnnotations().hasAnnotation("readable"))));
        return ret;
    }

    /**
    * 
    */
    public Node visit(BindParseNode bpn) throws Exception {
	Node ret = bpn.getLeft().visit(this);
	Node right = bpn.getRight().visit(this);
        RequestNode lrrn = ret instanceof RequestNode 
	    ? (RequestNode)ret : (RequestNode)null;
        if (lrrn != null)
        {
            lrrn.makeBind(right);
            if (bpn.getLeft() instanceof OperatorParseNode ||
		   bpn.getLeft() instanceof InterpolatedStringParseNode)
                lrrn = null;
             
        }
         
        if (lrrn == null)
        {
	    String name = ret.getClass().getName();
            name = "KJX-WTFFF" + name.substring(0, name.length() - 4); //KJXWTFFFF
            if (bpn.getLeft() instanceof OperatorParseNode)
                name = "Operator";
             
            if (bpn.getLeft() instanceof InterpolatedStringParseNode)
                name = "StringLiteral";
             
            ErrorReporting.ReportStaticError(
 	       bpn.getToken().getModule(), bpn.getLine(), "P1044", 
	       hash("lhs", name), "Cannot assign to " + name);
        }
         
        return ret;
    }

    /**
    * 
    */
    public Node visit(BlockParseNode d) throws Exception {
	List<Node> parameters = new ArrayList<Node>();
        Node forcedPattern = null;
        for (Object __dummyForeachVar6 : d.getParameters())
        {
            ParseNode p = (ParseNode)__dummyForeachVar6;
	    IdentifierParseNode id = p instanceof IdentifierParseNode 
		? (IdentifierParseNode)p : (IdentifierParseNode)null;
	    TypedParameterParseNode tppn = p instanceof TypedParameterParseNode 
		? (TypedParameterParseNode)p : (TypedParameterParseNode)null;
	    VarArgsParameterParseNode vappn = p instanceof VarArgsParameterParseNode 
		? (VarArgsParameterParseNode)p : (VarArgsParameterParseNode)null;
            if (id != null)
                parameters.add(new ParameterNode(id.getToken(), id));
            else if (tppn != null)
            {
                parameters.add(new ParameterNode(tppn.getToken(), tppn.getName() instanceof IdentifierParseNode ? (IdentifierParseNode)tppn.getName() : (IdentifierParseNode)null, tppn.getType().visit(this)));
            }
            else if (vappn != null)
            {
                // Inside could be either an identifier or a
                // TypedParameterParseNode - check for both.
		IdentifierParseNode inIPN = vappn.getName() instanceof IdentifierParseNode ? (IdentifierParseNode)vappn.getName() : (IdentifierParseNode)null;
		TypedParameterParseNode inTPPN = 
		    vappn.getName() instanceof TypedParameterParseNode
		      ? (TypedParameterParseNode)vappn.getName() 
		      : (TypedParameterParseNode)null;
                if (inIPN != null)
                    parameters.add(new ParameterNode(inIPN.getToken(), inIPN, true));
                else // Variadic
                if (inTPPN != null)
                    // Variadic
                    parameters.add(
		      new ParameterNode(
                        inTPPN.getToken(),
			inTPPN.getName() instanceof IdentifierParseNode
			  ? (IdentifierParseNode)inTPPN.getName()
			  : (IdentifierParseNode)null,
			true,
			inTPPN.getType().visit(this)));
                  
            }
            else if (p instanceof NumberParseNode || p instanceof StringLiteralParseNode || p instanceof OperatorParseNode)
            {
                parameters.add(p.visit(this));
            }
            else if (p instanceof ParenthesisedParseNode)
            {

		Token tok = p.getToken();
		IdentifierToken it = new IdentifierToken(tok.module, tok.line, tok.column, "_");
                id = new IdentifierParseNode(it);
                parameters.add(new ParameterNode(tok, id, p.visit(this)));
            }
            else
            {
                throw new Exception("unimplemented - unusual parameters");
            }     
        }
        BlockNode ret = new BlockNode(d.getToken(), d, parameters, map(d.getBody()), forcedPattern);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ClassDeclarationParseNode d) throws Exception {
	MethodDeclarationParseNode constructor = new MethodDeclarationParseNode(d.getToken());
        constructor.setSignature(d.getSignature());
	ObjectParseNode instanceObj = new ObjectParseNode(d.getToken());
        instanceObj.setBody(d.getBody());
	constructor.getBody().add(instanceObj);
        MethodNode ret = (MethodNode)constructor.visit(this);
        // Classes are public by default.
        // The next line makes them public always; it is not
        // possible to have a confidential class. It is unclear
        // whether that should be permitted or not.
        ret.setConfidential(false);
        return ret;
    }

    /**
    * 
    */
    public Node visit(TraitDeclarationParseNode d) throws Exception {
	MethodDeclarationParseNode constructor = new MethodDeclarationParseNode(d.getToken());
        constructor.setSignature(d.getSignature());
	ObjectParseNode instanceObj = new ObjectParseNode(d.getToken());
        instanceObj.setBody(d.getBody());
        constructor.getBody().add(instanceObj);
        MethodNode ret = (MethodNode)constructor.visit(this);
        // Traits are public by default.
        // The next line makes them public always; it is not
        // possible to have a confidential trait. It is unclear
        // whether that should be permitted or not.
        ret.setConfidential(false);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ReturnParseNode rpn) throws Exception {
        if (rpn.getReturnValue() == null)
            return new ReturnNode(rpn.getToken(),rpn,null);
         
	return new ReturnNode(rpn.getToken(), rpn, rpn.getReturnValue().visit(this));
    }

    /**
    * 
    */
    public Node visit(CommentParseNode cpn) throws Exception {
        return new NoopNode(cpn.getToken(), cpn);
    }

    /**
    * 
    */
    public Node visit(TypeStatementParseNode tspn) throws Exception {
	MethodDeclarationParseNode meth = 
	    new MethodDeclarationParseNode(tspn.getToken());
	SignatureParseNode spn = 
	    new SignatureParseNode(tspn.getToken());
	OrdinarySignaturePartParseNode spp = 
	    new OrdinarySignaturePartParseNode(
	       (IdentifierParseNode)tspn.getBaseName());
        spp.setGenericParameters(tspn.getGenericParameters());
        spn.addPart(spp);
        meth.setSignature(spn);
	TypeParseNode tpn = tspn.getBody() instanceof TypeParseNode
	    ? (TypeParseNode)tspn.getBody() : (TypeParseNode)null;
        if (tpn != null)
        {
            tpn.setName(((IdentifierParseNode)tspn.getBaseName()).getName());
        }
         
        meth.getBody().add(tspn.getBody());
        return meth.visit(this);
    }

    /**
    * 
    */
    public Node visit(TypeParseNode tpn) throws Exception {
        TypeNode ret = new TypeNode(tpn.getToken(), tpn);
        if (tpn.getName() != null)
            ret.setName(tpn.getName());
         
        for (ParseNode p : tpn.getBody())
            ret.getBody().add((SignatureNode)p.visit(this));
        return ret;
    }

    /**
    * 
    */
    public Node visit(ImportParseNode ipn) throws Exception {
        Node type = null;
        if (ipn.getType() != null)
            type = ipn.getType().visit(this);
         
        return new ImportNode(ipn.getToken(),ipn,type);
    }

    /**
    * 
    */
    public Node visit(DialectParseNode dpn) throws Exception {
        return new DialectNode(dpn.getToken(),dpn,module);
    }

    /**
    * 
    */
    public Node visit(InheritsParseNode ipn) throws Exception {
	Node frm = ipn.getFrom().visit(this);
        if (!(frm instanceof RequestNode))
            ErrorReporting.ReportStaticError(ipn.getFrom().getToken().getModule(), ipn.getFrom().getLine(), "P1045", hash(), "Can only inherit from method requests");
         
        return new InheritsNode(ipn.getToken(), ipn, frm,
				ipn.getAliases().stream()
				.collect(Collectors.toMap(
							 (apn -> ((SignatureNode)apn.getNewName().visit(this)).getName()),
							 (apn -> ((SignatureNode)apn.getOldName().visit(this))) )),
				ipn.getExcludes().stream()
				.map(x -> x.getName().getName())
				.collect(Collectors.toList()));
    }

    /**
    * 
    */
    public Node visit(UsesParseNode upn) throws Exception {
	//this method is an exact copy of the method above for InheritsParseNode
	//with the variable names changed
	Node frm = upn.getFrom().visit(this);
        if (!(frm instanceof RequestNode))
            ErrorReporting.ReportStaticError(upn.getFrom().getToken().getModule(), upn.getFrom().getLine(), "P1045", hash(),  "Can only inherit from method requests");
         
        return new InheritsNode(upn.getToken(), upn, frm,
				upn.getAliases().stream()
				.collect(Collectors.toMap(
							 (apn -> ((SignatureNode)apn.getNewName().visit(this)).getName()),
							 (apn -> ((SignatureNode)apn.getOldName().visit(this))) )),
				upn.getExcludes().stream()
				.map(x -> x.getName().getName())
				.collect(Collectors.toList()));
    }

    /**
    * 
    */
    public Node visit(AliasParseNode ipn) throws Exception {
        return null;
    }

    /**
    * 
    */
    public Node visit(ExcludeParseNode ipn) throws Exception {
        return null;
    }

    /**
    * 
    */
    public Node visit(ParenthesisedParseNode ppn) throws Exception {
        return ppn.getExpression().visit(this);
    }

    /**
    * 
    */
    public Node visit(TypedParameterParseNode tppn) throws Exception {
        ErrorReporting.ReportStaticError(
	 tppn.getToken().getModule(), 
	 tppn.getLine(), "P1023",
         hash( "token", "" + tppn.getToken()) ,
	 "Unexpected ':' in argument list");
        return null;
    }

    /**
    * 
    */
    public Node visit(ImplicitBracketRequestParseNode ibrpn) throws Exception {
        ImplicitReceiverRequestNode ret = new ImplicitReceiverRequestNode(ibrpn.getToken(), ibrpn);
        RequestPartNode rpn = new RequestPartNode("circumfix" + ibrpn.getName(), new ArrayList<Node>(), map(ibrpn.getArguments()), false);
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ExplicitBracketRequestParseNode ebrpn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(ebrpn.getToken(), ebrpn, ebrpn.getReceiver().visit(this));
        RequestPartNode rpn = new RequestPartNode(ebrpn.getName(), new ArrayList<Node>(), map(ebrpn.getArguments()));
        ret.addPart(rpn);
        return ret;
    }

    /**
    * Transforms a list of ParseNodes into a list of the
    * corresponding Nodes
    */
    private List<Node> map(Iterable<ParseNode> l) throws Exception {
        List<Node> ret = new ArrayList<Node>();
        for (Object __dummyForeachVar8 : l)
        {
            ParseNode p = (ParseNode)__dummyForeachVar8;
            if (!(p instanceof CommentParseNode))
                ret.add(p.visit(this));
        }
        return ret;
    }

}


