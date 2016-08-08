package Grace;

import som.compiler.MixinBuilder;
import som.compiler.MethodBuilder;

public class TranslationContext {
	public TranslationContext(MethodBuilder meth, MixinBuilder mxn, boolean bm) {
			methodBuilder = meth;
		    mixinBuilder = mxn;   	
		    buildingMethod = bm; //true iFF building a method, false iff building an OC / class / mixin
	}
	
   public TranslationContext spawn(MethodBuilder meth1, MixinBuilder mxn1) 
   		{ return new TranslationContext(meth1, mxn1, buildingMethod); } 
   public TranslationContext spawn(MethodBuilder meth1, MixinBuilder mxn1, boolean buildingMethod1) 
		{ return new TranslationContext(meth1, mxn1, buildingMethod1); } 

   public MethodBuilder methodBuilder;
   public MixinBuilder mixinBuilder;  
   public boolean buildingMethod;
}

