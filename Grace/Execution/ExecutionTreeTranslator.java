//
// Translated by CS2J (http://www.cs2j.com): 26/07/2016 8:30:26 a.m.
//

package Grace.Execution;

import CS2JNet.System.StringSupport;
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
        return obj.Visit(this);
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
        ObjectConstructorNode ret = new ObjectConstructorNode(obj.Token, obj);
        if (module == null)
            module = ret;
         
        for (Object __dummyForeachVar0 : obj.Body)
        {
            ParseNode p = (ParseNode)__dummyForeachVar0;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ n = p.<Node>Visit(this);
            if (!(p instanceof CommentParseNode))
                ret.Add(n);
             
        }
        return ret;
    }

    /**
    * 
    */
    public Node visit(NumberParseNode n) throws Exception {
        return new NumberLiteralNode(n.Token,n);
    }

    /**
    * 
    */
    public Node visit(StringLiteralParseNode n) throws Exception {
        return new StringLiteralNode(n.Token,n);
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
                ret = part.Visit(this);
            }
            else
            {
                ExplicitReceiverRequestNode errn = new ExplicitReceiverRequestNode(n.Token, n, ret);
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ args = new List<Node>();
                if (!(part instanceof StringLiteralParseNode))
                {
                    RequestPartNode rpnAS = new RequestPartNode("asString", new List<Node>(), new List<Node>());
                    ExplicitReceiverRequestNode errnAS = new ExplicitReceiverRequestNode(n.Token, n, part.Visit(this));
                    errnAS.addPart(rpnAS);
                    args.Add(errnAS);
                }
                else
                {
                    args.Add(part.Visit(this));
                } 
                RequestPartNode rpn = new RequestPartNode("++", new List<Node>(), args);
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
        SignatureNode ret = new SignatureNode(spn.Token,spn);
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ part : spn.Parts)
        {
            ret.addPart((SignaturePartNode)part.Visit(this));
        }
        if (spn.ReturnType != null)
            ret.setReturnType(spn.ReturnType.Visit(this));
         
        addAnnotations(spn.Annotations, ret.getAnnotations());
        return ret;
    }

    private void addAnnotations(AnnotationsParseNode source, AnnotationsNode dest) throws Exception {
        if (source != null)
            dest.AddAnnotations();
         
    }

    /**
    * 
    */
    public Node visit(OrdinarySignaturePartParseNode osppn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ parameters = new List<Node>();
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : osppn.Parameters)
        {
            // f
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ id = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tppn = p instanceof TypedParameterParseNode ? (TypedParameterParseNode)p : (TypedParameterParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ vappn = p instanceof VarArgsParameterParseNode ? (VarArgsParameterParseNode)p : (VarArgsParameterParseNode)null;
            if (id != null)
                parameters.Add(new ParameterNode(id.Token, id));
            else if (tppn != null)
            {
                parameters.Add(new ParameterNode(tppn.Token, tppn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)tppn.Name : (IdentifierParseNode)null, tppn.Type.Visit(this)));
            }
            else if (vappn != null)
            {
                // Inside could be either an identifier or a
                // TypedParameterParseNode - check for both.
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inIPN = vappn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)vappn.Name : (IdentifierParseNode)null;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inTPPN = vappn.Name instanceof TypedParameterParseNode ? (TypedParameterParseNode)vappn.Name : (TypedParameterParseNode)null;
                if (inIPN != null)
                    parameters.Add(new ParameterNode(inIPN.Token, inIPN, true));
                else // Variadic
                if (inTPPN != null)
                    // Variadic
                    parameters.Add(new ParameterNode(inTPPN.Token, inTPPN.Name instanceof IdentifierParseNode ? (IdentifierParseNode)inTPPN.Name : (IdentifierParseNode)null, true, inTPPN.Type.Visit(this)));
                  
            }
            else
            {
                throw new Exception("unimplemented - unusual parameters");
            }   
        }
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ generics = new List<Node>();
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : osppn.GenericParameters)
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ id = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            if (id != null)
            {
                generics.Add(new ParameterNode(id.Token, id));
            }
            else
            {
                throw new Exception("unimplemented - bad generic parameters");
            } 
        }
        if (osppn.Name.StartsWith("circumfix", StringComparison.Ordinal))
            return new OrdinarySignaturePartNode(osppn.Token, osppn, parameters, generics, false);
         
        return new OrdinarySignaturePartNode(osppn.Token, osppn, parameters, generics);
    }

    /**
    * 
    */
    public Node visit(MethodDeclarationParseNode d) throws Exception {
        MethodNode ret = new MethodNode(d.Token, d);
        SignatureNode sig = (SignatureNode)d.Signature.Visit(this);
        ret.setSignature(sig);
        ret.setAnnotations(sig.getAnnotations());
        String name = sig.getName();
        ret.setConfidential((d.Signature.Annotations != null && d.Signature.Annotations.HasAnnotation("confidential")));
        ret.setAbstract((d.Signature.Annotations != null && d.Signature.Annotations.HasAnnotation("abstract")));
        for (Object __dummyForeachVar5 : d.Body)
        {
            ParseNode p = (ParseNode)__dummyForeachVar5;
            if (!(p instanceof CommentParseNode))
                ret.Add(p.Visit(this));
             
        }
        // Indicate whether this method returns a fresh object
        ret.setFresh((d.Body.Count > 0 && d.Body[d.Body.Count - 1] instanceof ObjectParseNode));
        return ret;
    }

    /**
    * 
    */
    public Node visit(IdentifierParseNode i) throws Exception {
        ImplicitReceiverRequestNode ret = new ImplicitReceiverRequestNode(i.Token, i);
        RequestPartNode rpn = new RequestPartNode(i.Name, new List<Node>(), new List<Node>());
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
            ret = new IfThenRequestNode(irrpn.Token, irrpn);
        }
        else if (StringSupport.equals(irrpn.Name, "for do"))
        {
            ret = new ForDoRequestNode(irrpn.Token, irrpn);
        }
        else
        {
            ret = new ImplicitReceiverRequestNode(irrpn.Token, irrpn);
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
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(irrpn.Token, irrpn, irrpn.Receiver.Visit(this));
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
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(popn.Token, popn, popn.Receiver.Visit(this));
        RequestPartNode rpn = new RequestPartNode("prefix" + popn.Name, new List<Node>(), new List<Node>());
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(OperatorParseNode opn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(opn.Token, opn, opn.Left.Visit(this));
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ args = new List<Node>();
        args.Add(opn.Right.Visit(this));
        RequestPartNode rpn = new RequestPartNode(opn.name, new List<Node>(), args);
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
            val = vdpn.Value.Visit(this);
         
        if (vdpn.Type != null)
            type = vdpn.Type.Visit(this);
         
        VarDeclarationNode ret = new VarDeclarationNode(vdpn.Token,vdpn,val,type);
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
            val = vdpn.Value.Visit(this);
         
        if (vdpn.Type != null)
            type = vdpn.Type.Visit(this);
         
        DefDeclarationNode ret = new DefDeclarationNode(vdpn.Token,vdpn,val,type);
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
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ ret = bpn.Left.Visit(this);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ right = bpn.Right.Visit(this);
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
             
            ErrorReporting.ReportStaticError(bpn.Token.Module, bpn.Line, "P1044", new Dictionary<String, String>{ { "lhs", name } }, "Cannot assign to " + name);
        }
         
        return ret;
    }

    /**
    * 
    */
    public Node visit(BlockParseNode d) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ parameters = new List<Node>();
        Node forcedPattern = null;
        for (Object __dummyForeachVar6 : d.Parameters)
        {
            ParseNode p = (ParseNode)__dummyForeachVar6;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ id = p instanceof IdentifierParseNode ? (IdentifierParseNode)p : (IdentifierParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tppn = p instanceof TypedParameterParseNode ? (TypedParameterParseNode)p : (TypedParameterParseNode)null;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ vappn = p instanceof VarArgsParameterParseNode ? (VarArgsParameterParseNode)p : (VarArgsParameterParseNode)null;
            if (id != null)
                parameters.Add(new ParameterNode(id.Token, id));
            else if (tppn != null)
            {
                parameters.Add(new ParameterNode(tppn.Token, tppn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)tppn.Name : (IdentifierParseNode)null, tppn.Type.Visit(this)));
            }
            else if (vappn != null)
            {
                // Inside could be either an identifier or a
                // TypedParameterParseNode - check for both.
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inIPN = vappn.Name instanceof IdentifierParseNode ? (IdentifierParseNode)vappn.Name : (IdentifierParseNode)null;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ inTPPN = vappn.Name instanceof TypedParameterParseNode ? (TypedParameterParseNode)vappn.Name : (TypedParameterParseNode)null;
                if (inIPN != null)
                    parameters.Add(new ParameterNode(inIPN.Token, inIPN, true));
                else // Variadic
                if (inTPPN != null)
                    // Variadic
                    parameters.Add(new ParameterNode(inTPPN.Token, inTPPN.Name instanceof IdentifierParseNode ? (IdentifierParseNode)inTPPN.Name : (IdentifierParseNode)null, true, inTPPN.Type.Visit(this)));
                  
            }
            else if (p instanceof NumberParseNode || p instanceof StringLiteralParseNode || p instanceof OperatorParseNode)
            {
                parameters.Add(p.Visit(this));
            }
            else if (p instanceof ParenthesisedParseNode)
            {
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tok = p.Token;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ it = new IdentifierToken(tok.module, tok.line, tok.column, "_");
                id = new IdentifierParseNode(it);
                parameters.Add(new ParameterNode(tok, id, p.Visit(this)));
            }
            else
            {
                throw new Exception("unimplemented - unusual parameters");
            }     
        }
        BlockNode ret = new BlockNode(d.Token, d, parameters, map(d.Body), forcedPattern);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ClassDeclarationParseNode d) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ constructor = new MethodDeclarationParseNode(d.Token);
        constructor.Signature = d.Signature;
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ instanceObj = new ObjectParseNode(d.Token);
        instanceObj.Body = d.Body;
        constructor.Body.Add(instanceObj);
        MethodNode ret = (MethodNode)constructor.Visit(this);
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
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ constructor = new MethodDeclarationParseNode(d.Token);
        constructor.Signature = d.Signature;
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ instanceObj = new ObjectParseNode(d.Token);
        instanceObj.Body = d.Body;
        constructor.Body.Add(instanceObj);
        MethodNode ret = (MethodNode)constructor.Visit(this);
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
            return new ReturnNode(rpn.Token,rpn,null);
         
        return new ReturnNode(rpn.Token, rpn, rpn.ReturnValue.Visit(this));
    }

    /**
    * 
    */
    public Node visit(CommentParseNode cpn) throws Exception {
        return new NoopNode(cpn.Token, cpn);
    }

    /**
    * 
    */
    public Node visit(TypeStatementParseNode tspn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ meth = new MethodDeclarationParseNode(tspn.Token);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ spn = new SignatureParseNode(tspn.Token);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ spp = new OrdinarySignaturePartParseNode((IdentifierParseNode)tspn.BaseName);
        spp.GenericParameters = tspn.GenericParameters;
        spn.AddPart(spp);
        meth.Signature = spn;
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ tpn = tspn.Body instanceof TypeParseNode ? (TypeParseNode)tspn.Body : (TypeParseNode)null;
        if (tpn != null)
        {
            tpn.Name = ((IdentifierParseNode)tspn.BaseName).Name;
        }
         
        meth.Body.Add(tspn.Body);
        return meth.Visit(this);
    }

    /**
    * 
    */
    public Node visit(TypeParseNode tpn) throws Exception {
        TypeNode ret = new TypeNode(tpn.Token, tpn);
        if (tpn.Name != null)
            ret.setName(tpn.Name);
         
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ p : tpn.Body)
            ret.getBody().Add((SignatureNode)p.Visit(this));
        return ret;
    }

    /**
    * 
    */
    public Node visit(ImportParseNode ipn) throws Exception {
        Node type = null;
        if (ipn.Type != null)
            type = ipn.Type.Visit(this);
         
        return new ImportNode(ipn.Token,ipn,type);
    }

    /**
    * 
    */
    public Node visit(DialectParseNode dpn) throws Exception {
        return new DialectNode(dpn.Token,dpn,module);
    }

    /**
    * 
    */
    public Node visit(InheritsParseNode ipn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ frm = ipn.From.Visit(this);
        if (!(frm instanceof RequestNode))
            ErrorReporting.ReportStaticError(ipn.From.Token.Module, ipn.From.Line, "P1045", new Dictionary<String, String>{  }, "Can only inherit from method requests");
         
        return new InheritsNode(ipn.Token, ipn, frm);
    }

    /**
    * 
    */
    public Node visit(UsesParseNode upn) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ frm = upn.From.Visit(this);
        if (!(frm instanceof RequestNode))
            ErrorReporting.ReportStaticError(upn.From.Token.Module, upn.From.Line, "P1045", new Dictionary<String, String>{  }, "Can only inherit from method requests");
         
        return new InheritsNode(upn.Token, upn, frm);
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
        return ppn.Expression.Visit(this);
    }

    /**
    * 
    */
    public Node visit(TypedParameterParseNode tppn) throws Exception {
        ErrorReporting.ReportStaticError(tppn.Token.Module, tppn.Line, "P1023", new Dictionary<String, String>{ { "token", "" + tppn.Token } }, "Unexpected ':' in argument list");
        return null;
    }

    /**
    * 
    */
    public Node visit(ImplicitBracketRequestParseNode ibrpn) throws Exception {
        ImplicitReceiverRequestNode ret = new ImplicitReceiverRequestNode(ibrpn.Token, ibrpn);
        RequestPartNode rpn = new RequestPartNode("circumfix" + ibrpn.Name, new List<Node>(), map(ibrpn.Arguments), false);
        ret.addPart(rpn);
        return ret;
    }

    /**
    * 
    */
    public Node visit(ExplicitBracketRequestParseNode ebrpn) throws Exception {
        ExplicitReceiverRequestNode ret = new ExplicitReceiverRequestNode(ebrpn.Token, ebrpn, ebrpn.Receiver.Visit(this));
        RequestPartNode rpn = new RequestPartNode(ebrpn.Name, new List<Node>(), map(ebrpn.Arguments));
        ret.addPart(rpn);
        return ret;
    }

    /**
    * Transforms a list of ParseNodes into a list of the
    * corresponding Nodes
    */
    private List<Node> map(IEnumerable<ParseNode> l) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ ret = new List<Node>();
        for (Object __dummyForeachVar8 : l)
        {
            ParseNode p = (ParseNode)__dummyForeachVar8;
            if (!(p instanceof CommentParseNode))
                ret.Add(p.Visit(this));
             
        }
        return ret;
    }

}


