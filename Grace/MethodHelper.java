package Grace;
import java.util.Collections;
import java.util.stream.Collectors;


public class MethodHelper {


        /// <summary>
        /// Return an arity-formatted version of a part name.
        /// </summary>
        /// <param name="name">Part name</param>
        /// <param name="count">Number of parameters/arguments</param>
        public static String ArityNamePart(String name, int count)
        {
            switch(count)
            {
                case 0:
                    return name;
                case 1:
                    return name + "(_)";
                case 2:
                    return name + "(_,_)";
                case 3:
                    return name + "(_,_,_)";
                default:
                   return name + "(" + 
		    Collections.nCopies(count, "_").stream()
		       .collect(Collectors.joining(","))
                        + ")";
            }
	}
}

