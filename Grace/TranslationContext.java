package Grace;

import som.compiler.MixinBuilder;
import som.compiler.MethodBuilder;

public class TranslationContext {
	public TranslationContext(MethodBuilder meth, MixinBuilder mxn) {
		    methodBuilder = meth;
		    mixinBuilder = mxn;   		
	}
   public MethodBuilder methodBuilder;
   public MixinBuilder mixinBuilder;   
}

