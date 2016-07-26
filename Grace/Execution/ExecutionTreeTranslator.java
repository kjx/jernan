//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:26 a.m.
//

package Grace.Execution;
import Grace.Parsing.Token;
import Grace.Parsing.ParseNode;
import Grace.Parsing.SignaturePartParseNode;
import Grace.Parsing.ParseNodeVisitor;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

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
public class ExecutionTreeTranslator  extends ParseNodeVisitor<Node> 
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
        for (Object __dummyForeachVar1 : n.Parts)
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
        if (spn.ReturnType != null)
            ret.setReturnType(spn.ReturnType.visit(this));
         
        addAnnotations(spn.Annotations, ret.getAnnotations());
        return ret;
    }

    private void addAnnotations(AnnotationsParseNode source, AnnotationsNode dest) throws Exception {
        if (source != null)
            dest.addAnnotations();
         
    }

    /**
    * 
    */
    public Node visit(OrdinarySignaturePartParseNode osppn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ parameters = new ArrayList<Node>();
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : osppn.Parameters)
        {
            // f
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ id = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tppn = p instanceof TypedParameterParseNode ? (TypedParameterParseNode)p : (TypedParameterParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ vappn = p instanceof VarArgsParameterParseNode ? (VarArgsParameterParseNode)p : (VarArgsParameterParseNode)null;
            if (id != null)
                parameters.add(new ParameterNode(id.getToken(), id));
            else if (tppn != null)
            {
                parameters.add(new ParameterNode(tppn.getToken(), tppn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)tppn.Name : (IdentifierParseNode)null, tppn.Type.visit(this)));
            }
            else if (vappn != null)
            {
                // Inside could be either an identifier or a
                // TypedParameterParseNode - check for both.
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inIPN = vappn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)vappn.Name : (IdentifierParseNode)null;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inTPPN = vappn.Name instanceof TypedParameterParseNode ? (TypedParameterParseNode)vappn.Name : (TypedParameterParseNode)null;
                if (inIPN != null)
                    parameters.add(new ParameterNode(inIPN.getToken(), inIPN, true));
                else // Variadic
                if (inTPPN != null)
                    // Variadic
                    parameters.add(new ParameterNode(inTPPN.getToken(), inTPPN.Name instanceof IdentifierParseNode ? (IdentifierParseNode)inTPPN.Name : (IdentifierParseNode)null, true, inTPPN.Type.visit(this)));
                  
            }
            else
            {
                throw new Exception("unimplemented - unusual parameters");
            }   
        }
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ generics = new ArrayList<Node>();
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : osppn.GenericParameters)
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ id = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            if (id != null)
            {
                generics.add(new ParameterNode(id.getToken(), id));
            }
            else
            {
                throw new Exception("unimplemented - bad generic parameters");
            } 
        }
        if (osppn.Name.StartsWith("circumfix", StringComparison.Ordinal))
            return new OrdinarySignaturePartNode(osppn.getToken(), osppn, parameters, generics, false);
         
        return new OrdinarySignaturePartNode(osppn.getToken(), osppn, parameters, generics);
    }

    /**
    * 
    */
    public Node visit(MethodDeclarationParseNode d) throws Exception {
        MethodNode ret = new MethodNode(d.getToken(), d);
        SignatureNode sig = (SignatureNode)d.Signature.visit(this);
        ret.setSignature(sig);
        ret.setAnnotations(sig.getAnnotations());
        String name = sig.getName();
        ret.setConfidential((d.Signature.Annotations != null && d.Signature.Annotations.HasAnnotation("confidential")));
        ret.setAbstract((d.Signature.Annotations != null && d.Signature.Annotations.HasAnnotation("abstract")));
        for (Object __dummyForeachVar5 : d.Body)
        {
            ParseNode p = (ParseNode)__dummyForeachVar5;
            if (!(p instanceof CommentParseNode))
                ret.add(p.visit(this));
             
        }
        // Indicate whether this method returns a fresh object
        ret.setFresh((d.Body.Count > 0 && d.Body[d.Body.Count - 1] instanceof ObjectParseNode));
        return ret;
    }

    /**
    * 
    */
    public Node visit(IdentifierParseNode i) throws Exception {
        ImplicitReceiverRequestNode ret = new ImplicitReceiverRequestNode(i.getToken(), i);
        RequestPartNode rpn = new RequestPartNode(i.Name, new ArrayList<Node>(), new ArrayList<Node>());
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ImplicitReceiverRequestParseNode irrpn) throws Exception {
        ImplicitReceiverRequestNode ret = null;
        if (StringSupport.equals(irrpn.Name, "if then"))
        {
            ret = new IfThenRequestNode(irrpn.getToken(), irrpn);
        }
        else if (StringSupport.equals(irrpn.Name, "for do"))
        {
            ret = new ForDoRequestNode(irrpn.getToken(), irrpn);
        }
        else
        {
            ret = new ImplicitReceiverRequestNode(irrpn.getToken(), irrpn);
        }  
        for (int i = 0;i < irrpn.Arguments.Count;i++)
        {
            RequestPartNode rpn = new RequestPartNode(((IdentifierParseNode)irrpn.NameParts[i]).Name, map(irrpn.GenericArguments[i]), map(irrpn.Arguments[i]));
            ret.addPart(rpn);
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(ExplicitReceiverRequestParseNode irrpn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(irrpn.getToken(), irrpn, irrpn.Receiver.visit(this));
        for (int i = 0;i < irrpn.Arguments.Count;i++)
        {
            RequestPartNode rpn = new RequestPartNode(((IdentifierParseNode)irrpn.NameParts[i]).Name, map(irrpn.GenericArguments[i]), map(irrpn.Arguments[i]));
            ret.addPart(rpn);
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(PrefixOperatorParseNode popn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(popn.getToken(), popn, popn.Receiver.visit(this));
        RequestPartNode rpn = new RequestPartNode("prefix" + popn.Name, new ArrayList<Node>(), new ArrayList<Node>());
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(OperatorParseNode opn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(opn.getToken(), opn, opn.Left.visit(this));
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ args = new ArrayList<Node>();
        args.add(opn.Right.visit(this));
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
        if (vdpn.Value != null)
            val = vdpn.Value.visit(this);
         
        if (vdpn.Type != null)
            type = vdpn.Type.visit(this);
         
        VarDeclarationNode ret = new VarDeclarationNode(vdpn.getToken(),vdpn,val,type);
        addAnnotations(vdpn.Annotations, ret.getAnnotations());
        if (vdpn.Annotations != null && vdpn.Annotations.HasAnnotation("public"))
        {
            ret.setReadable(true);
            ret.setWritable(true);
        }
        else
        {
            ret.setReadable((vdpn.Annotations != null && vdpn.Annotations.HasAnnotation("readable")));
            ret.setWritable((vdpn.Annotations != null && vdpn.Annotations.HasAnnotation("writable")));
        } 
        return ret;
    }

    /**
    * 
    */
    public Node visit(DefDeclarationParseNode vdpn) throws Exception {
        Node val = null;
        Node type = null;
        if (vdpn.Value != null)
            val = vdpn.Value.visit(this);
         
        if (vdpn.Type != null)
            type = vdpn.Type.visit(this);
         
        DefDeclarationNode ret = new DefDeclarationNode(vdpn.getToken(),vdpn,val,type);
        addAnnotations(vdpn.Annotations, ret.getAnnotations());
        if (vdpn.Annotations != null && vdpn.Annotations.HasAnnotation("public"))
            ret.setPublic(true);
         
        ret.setPublic((vdpn.Annotations != null && (vdpn.Annotations.HasAnnotation("public") || vdpn.Annotations.HasAnnotation("readable"))));
        return ret;
    }

    /**
    * 
    */
    public Node visit(BindParseNode bpn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ ret = bpn.Left.visit(this);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ right = bpn.Right.visit(this);
        RequestNode lrrn = ret instanceof RequestNode ? (RequestNode)ret : (RequestNode)null;
        if (lrrn != null)
        {
            lrrn.MakeBind(right);
            if (bpn.Left instanceof OperatorParseNode || bpn.Left instanceof InterpolatedStringParseNode)
                lrrn = null;
             
        }
         
        if (lrrn == null)
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ name = ret.GetType().Name;
            name = name.Substring(0, name.Length - 4);
            if (bpn.Left instanceof OperatorParseNode)
                name = "Operator";
             
            if (bpn.Left instanceof InterpolatedStringParseNode)
                name = "StringLiteral";
             
            ErrorReporting.ReportStaticError(bpn.getToken().Module, bpn.Line, "P1044", new Dictionary<String, String>{ { "lhs", name } }, "Cannot assign to " + name);
        }
         
        return ret;
    }

    /**
    * 
    */
    public Node visit(BlockParseNode d) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ parameters = new ArrayList<Node>();
        Node forcedPattern = null;
        for (Object __dummyForeachVar6 : d.Parameters)
        {
            ParseNode p = (ParseNode)__dummyForeachVar6;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ id = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tppn = p instanceof TypedParameterParseNode ? (TypedParameterParseNode)p : (TypedParameterParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ vappn = p instanceof VarArgsParameterParseNode ? (VarArgsParameterParseNode)p : (VarArgsParameterParseNode)null;
            if (id != null)
                parameters.add(new ParameterNode(id.getToken(), id));
            else if (tppn != null)
            {
                parameters.add(new ParameterNode(tppn.getToken(), tppn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)tppn.Name : (IdentifierParseNode)null, tppn.Type.visit(this)));
            }
            else if (vappn != null)
            {
                // Inside could be either an identifier or a
                // TypedParameterParseNode - check for both.
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inIPN = vappn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)vappn.Name : (IdentifierParseNode)null;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inTPPN = vappn.Name instanceof TypedParameterParseNode ? (TypedParameterParseNode)vappn.Name : (TypedParameterParseNode)null;
                if (inIPN != null)
                    parameters.add(new ParameterNode(inIPN.getToken(), inIPN, true));
                else // Variadic
                if (inTPPN != null)
                    // Variadic
                    parameters.add(new ParameterNode(inTPPN.getToken(), inTPPN.Name instanceof IdentifierParseNode ? (IdentifierParseNode)inTPPN.Name : (IdentifierParseNode)null, true, inTPPN.Type.visit(this)));
                  
            }
            else if (p instanceof NumberParseNode || p instanceof StringLiteralParseNode || p instanceof OperatorParseNode)
            {
                parameters.add(p.visit(this));
            }
            else if (p instanceof ParenthesisedParseNode)
            {
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tok = p.getToken();
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ it = new IdentifierToken(tok.module, tok.line, tok.column, "_");
                id = new IdentifierParseNode(it);
                parameters.add(new ParameterNode(tok, id, p.visit(this)));
            }
            else
            {
                throw new Exception("unimplemented - unusual parameters");
            }     
        }
        BlockNode ret = new BlockNode(d.getToken(), d, parameters, map(d.Body), forcedPattern);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ClassDeclarationParseNode d) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ constructor = new MethodDeclarationParseNode(d.getToken());
        constructor.Signature = d.Signature;
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ instanceObj = new ObjectParseNode(d.getToken());
        instanceObj.Body = d.Body;
        constructor.Body.add(instanceObj);
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
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ constructor = new MethodDeclarationParseNode(d.getToken());
        constructor.Signature = d.Signature;
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ instanceObj = new ObjectParseNode(d.getToken());
        instanceObj.Body = d.Body;
        constructor.Body.add(instanceObj);
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
        if (rpn.ReturnValue == null)
            return new ReturnNode(rpn.getToken(),rpn,null);
         
        return new ReturnNode(rpn.getToken(), rpn, rpn.ReturnValue.visit(this));
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
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ meth = new MethodDeclarationParseNode(tspn.getToken());
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ spn = new SignatureParseNode(tspn.getToken());
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ spp = new OrdinarySignaturePartParseNode((IdentifierParseNode)tspn.BaseName);
        spp.GenericParameters = tspn.GenericParameters;
        spn.addPart(spp);
        meth.Signature = spn;
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tpn = tspn.Body instanceof TypeParseNode ? (TypeParseNode)tspn.Body : (TypeParseNode)null;
        if (tpn != null)
        {
            tpn.Name = ((IdentifierParseNode)tspn.BaseName).Name;
        }
         
        meth.Body.add(tspn.Body);
        return meth.visit(this);
    }

    /**
    * 
    */
    public Node visit(TypeParseNode tpn) throws Exception {
        TypeNode ret = new TypeNode(tpn.getToken(), tpn);
        if (tpn.Name != null)
            ret.setName(tpn.Name);
         
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : tpn.Body)
            ret.getBody().add((SignatureNode)p.visit(this));
        return ret;
    }

    /**
    * 
    */
    public Node visit(ImportParseNode ipn) throws Exception {
        Node type = null;
        if (ipn.Type != null)
            type = ipn.Type.visit(this);
         
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
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ frm = ipn.From.visit(this);
        if (!(frm instanceof RequestNode))
            ErrorReporting.ReportStaticError(ipn.From.getToken().Module, ipn.From.Line, "P1045", new Dictionary<String, String>{  }, "Can only inherit from method requests");
         
        return new InheritsNode(ipn.getToken(), ipn, frm);
    }

    /**
    * 
    */
    public Node visit(UsesParseNode upn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ frm = upn.From.visit(this);
        if (!(frm instanceof RequestNode))
            ErrorReporting.ReportStaticError(upn.From.getToken().Module, upn.From.Line, "P1045", new Dictionary<String, String>{  }, "Can only inherit from method requests");
         
        return new InheritsNode(upn.getToken(), upn, frm);
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
        return ppn.Expression.visit(this);
    }

    /**
    * 
    */
    public Node visit(TypedParameterParseNode tppn) throws Exception {
        ErrorReporting.ReportStaticError(tppn.getToken().Module, tppn.Line, "P1023", new Dictionary<String, String>{ { "token", "" + tppn.getToken() } }, "Unexpected ':' in argument list");
        return null;
    }

    /**
    * 
    */
    public Node visit(ImplicitBracketRequestParseNode ibrpn) throws Exception {
        ImplicitReceiverRequestNode ret = new ImplicitReceiverRequestNode(ibrpn.getToken(), ibrpn);
        RequestPartNode rpn = new RequestPartNode("circumfix" + ibrpn.Name, new ArrayList<Node>(), map(ibrpn.Arguments), false);
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ExplicitBracketRequestParseNode ebrpn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(ebrpn.getToken(), ebrpn, ebrpn.Receiver.visit(this));
        RequestPartNode rpn = new RequestPartNode(ebrpn.Name, new ArrayList<Node>(), map(ebrpn.Arguments));
        ret.addPart(rpn);
        return ret;
    }

    /**
    * Transforms a list of ParseNodes into a list of the
    * corresponding Nodes
    */
    private List<Node> map(IEnumerable<ParseNode> l) throws Exception {
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


