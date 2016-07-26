//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 7:36:44 a.m.
//


//KJX BUGGY
//import CS2JNet.JavaSupport.Collections.Generic.LCC.CollectionSupport;
//import CS2JNet.System.Collections.LCC.CSList;
//import CS2JNet.System.LCC.Disposable;
//import CS2JNet.System.Reflection.Assembly;
//import CS2JNet.System.StringSplitOptions;
//import CS2JNet.System.StringSupport;

package Grace;

import Grace.StaticErrorException;
import Grace.OutputSink;
import Grace.Parsing.Token;
import java.io.BufferedReader;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/**
* Encapsulates behaviour relating to error reporting
*/
public class ErrorReporting   
{
    private static OutputSink sink = System.out::println;
    private static HashSet<String> SilencedErrors = new HashSet<String>();
    /**
    * While true, no error reports will be displayed. Exceptions
    * are still thrown.
    */
    private static boolean __SuppressAllErrors;
    public static boolean getSuppressAllErrors() {
        return __SuppressAllErrors;
    }

    public static void setSuppressAllErrors(boolean value) {
        __SuppressAllErrors = value;
    }

    /**
    * Retrieve the matching error message for a given code
    * from the highest-priority error message source.
    * 
    *  @param code Error code
    *  @param data Data to be used for matching messages
    *  @return 
    * The error string corresponding to the code and conditions,
    * or null
    * 
    * The search order is first user messages, from the
    * "UserErrorMessages.txt" file in the user import root,
    * then "OverlayErrorMessages.txt" next to the executable,
    * then "DefaultErrorMessages.txt" next to the executable.
    * Only the last of these is shipped with the default
    * distribution. The first matching entry will be used.
    */
    public static String getMessage(String code, HashMap<String,String> data)  {
	System.err.println("KJX BUGGY NO ERROR MESSAGES");
	return null;
    }

    /**
    * Retrieve the error message for a given code from the
    * message database in a given directory, using the
    * provided data to select from multiple options.
    * 
    *  @param code Error code
    *  @param dir 
    * Path to directory containing messages file to use
    * 
    *  @param filename 
    * Name of file to search within given directory
    * 
    *  @param data 
    * Dictionary of proposed substitute values to be used
    * for winnowing
    * 
    *  @return 
    * The error string corresponding to the code, or null
    * 
    *  @see ErrorReporting.GetMessage
    */
    public static String getMessageFromFile(String code, String dir, String filename, HashMap<String,String> data)  {
	System.err.println("KJX BUGGY NO ERROR MESSAGES");
	return null;
    }

    /**
    * Substitute variables into an error message string.
    * 
    *  @param message Error message
    *  @param vars Dictionary from variable names to
    * values to insert in their place.
    * The error string can contain substitution marks written in
    * ${...} that will be replaced by the variable value from
    * the 
    *  {@code vars}
    *  parameter.
    * 
    *  @return The 
    *  {@code message}
    *  string with
    * any substitutions made.
    *  @see ErrorReporting.GetMessage
    */
    public static String formatMessage(String message, Map<String,String> vars)  {
        String ret = message;
        for (String k : vars.keySet())
        {
            ret = ret.replace("${" + k + "}", vars.get(k));
        }
        return ret;
    }

    /**
    * Report a static error to the user
    * 
    *  @param module Module name where error found
    *  @param line Line number where error found
    *  @param code Error code
    *  @param vars Dictionary from variable names to
    * values to insert in the message in their place.
    *  @param localDescription A description of the error
    * given at the site of generation, which will be used if no
    * user error message for 
    *  {@code code}
    *  is found.
    * 
    * The error code will be translated into a message and
    * formatted, then displayed to the user using WriteError
    * as configured by the front end.
    * 
    *  @throws StaticErrorException Always thrown to
    * allow front-end code to handle a static failure.
    *  @see WriteError
    *  @see ErrorReporting.GetMessage
    */

    public static void ReportStaticError(String module, int line, String code, HashMap<String,String> vars, String localDescription)  {
        String baseMessage = getMessage(code,vars) != null ? getMessage(code,vars) : localDescription;
        String formattedMessage = formatMessage(baseMessage,vars);
        if (!SilencedErrors.contains(code) && !getSuppressAllErrors())
            WriteError(module,line,code,formattedMessage);
         
        throw new StaticErrorException(code,line,module,formattedMessage);
    }

    /**
    * Report a static error to the user
    * 
    *  @param module Module name where error found
    *  @param line Line number where error found
    *  @param code Error code
    *  @param localDescription A description of the error
    * given at the site of generation, which will be used if no
    * user error message for 
    *  {@code code}
    *  is found.
    * 
    * The error code will be translated into a message, then
    * displayed to the user using WriteError as configured by
    * the front end.
    * 
    *  @throws StaticErrorException Always thrown to
    * allow front-end code to handle a static failure.
    *  @see WriteError
    *  @see ErrorReporting.GetMessage
    */
    public static void ReportStaticError(String module, int line, String code, String localDescription)  {
        String baseMessage = getMessage(code,new HashMap<String,String>()) != null ? getMessage(code,new HashMap<String,String>()) : localDescription;
        if (!SilencedErrors.contains(code) && !getSuppressAllErrors())
            WriteError(module,line,code,baseMessage);
    }

    /**
    * Write out a static error message according to the
    * configuration provided by the front end.
    * 
    *  @param module Module name where error found
    *  @param line Line number where error found
    *  @param code Error code
    *  @param message Formatted error message
    * The message is written to the 
    *  {@code 
    * OutputSink}
    *  configured by the front end.
    * 
    *  @throws StaticErrorException Always thrown to
    * allow front-end code to handle a static failure.
    *  @see WriteError
    *  @see ErrorReporting.GetMessage
    */
    public static void WriteError(String module, int line, String code, String message)  {

// KJX BUGGY
//         if (!Console.IsErrorRedirected)
//         {
//             Console.ForegroundColor = ConsoleColor.Red;
//         }
         
           sink.WriteLine(module + ":" + line + ": " + code + ": " + message);

//         if (!Console.IsErrorRedirected)
//         {
//             Console.ResetColor();
//         }
// KJX BUGGY ENDS 
        throw new StaticErrorException(code,line,module,message);
    }

    /**
    * Write a line of text to the error sink.
    * 
    *  @param line 
    * Output
    */
    public static void writeLine(String line)  {
        sink.WriteLine(line);
    }

    /**
    * Write out a runtime error message for a Grace exception
    * according to the configuration provided by the front end.
    * 
    *  @param gep Exception packet to print
    * The message is written to the 
    *  {@code 
    * OutputSink}
    *  configured by the front end.
    * 
    *  @see WriteError
    */
//KJX BUGGY
//Not so much buggy as I don't think I'll need this actually
    //    public static void writeException(GraceExceptionPacket gep)  {

//         if (!Console.IsErrorRedirected)
//         {
//             Console.ForegroundColor = ConsoleColor.Red;
//         }
         
//           sink.WriteLine(gep.Description);

//         if (!Console.IsErrorRedirected)
//         {
//             Console.ResetColor();
//         }


//    }
//KJX  BUGGY ENDS
    /**
    * Raise a Grace exception for a particular error.
    * 
    *  @param ctx Evaluation context to raise from
    *  @param code Error code
    *  @param vars Dictionary from variable names to
    * values to insert in the message in their place.
    *  @param localDescription A description of the error
    * given at the site of generation, which will be used if no
    * user error message for 
    *  {@code code}
    *  is found.
    * 
    * A new 
    *  {@code GraceExceptionPacket
    * }
    *  is created and thrown with the retrieved method and
    * current call stack.
    * 
    *  @see WriteException
    */
//  OOPS: never expect to get this far - jernan won't have EvaluationContexts like this
//
//     public static void raiseError(EvaluationContext ctx, String code, HashMap<String,String> vars, String localDescription)  {
//         String baseMessage = getMessage(code,vars) != null ? getMessage(code,vars) : localDescription;
// 	String[] parts = baseMessage.split("[ :]", 2); //KJX BUGGY
// 	System.err.println("KJX BUGGY");
//         String kind = parts[0];
// 	String msg = parts[1];
//         msg = FormatMessage(msg, vars);
//         GraceExceptionPacket.Throw(kind, code + ": " + msg, ctx.GetStackTrace());
//     }

    /**
    * Set the sink where error messages will be written.
    * 
    *  @param s Destination for error messages
    */
    public static void setSink(OutputSink s)  {
        sink = s;
    }

    /**
    * Suppress printing of a particular static error.
    * 
    *  @param code Error to silence
    */
    public static void silenceError(String code)  {
        SilencedErrors.add(code);
    }

    private static List<ErrorRecord> reportedErrors = new ArrayList<>();
    /**
    * Record an error at a given location with a code and
    * message, with the mapping of variables as well.
    * 
    *  @param t Location of the error
    *  @param code Error code
    *  @param message Error message
    *  @param vars Mapping of replacement variables
    */
    public static void record(Token t, String code, String message, HashMap<String,String> vars)  {
        reportedErrors.add(new ErrorRecord(t, code, message, vars));
    }

    //KJX I think this is acutaly better than the tuple in Kernan
    //KJX would be more graceful in Grace
    //KJX but yes in Java, not making them accessors is lazy.  I don't care.
    private static class ErrorRecord {
	public final Token token;
	public final String code;
	public final String message;
	public final HashMap<String,String> vars;
	
	ErrorRecord(Token token, String code, String message, HashMap<String,String> vars) {
	    this.token = token;
	    this.code = code;
	    this.message = message;
	    this.vars = vars;
	};
    } 

    /**
    * Write all recorded (likely dialect) errors to
    * the screen.
    */
    public static void writeAllRecorded()  {
        for (ErrorRecord tp : reportedErrors)
        {

	    String module = tp.token.module;
	    int line = tp.token.line;
            String code = tp.code;
            String localDescription = tp.message;
            HashMap<String,String> vars = tp.vars;
            try
            {
		//KJX BUGGY
                String baseMessage = localDescription;
                String formattedMessage = formatMessage(baseMessage, vars);
                WriteError(module, line, code, formattedMessage);
            }
            catch (StaticErrorException __dummyCatchVar0)
            {
            }
        
        }
    }

    /**
    * Empty the collected list of errors reported so far.
    */
    public static void clearRecordedErrors()  {
        reportedErrors.clear();
    }

    /**
    * True iff at least one recorded, but not immediately fatal,
    * error has been given already.
    */
    public static boolean getHasRecordedError()  {
        return reportedErrors.size() != 0;
    }

    //KJX
    public static HashMap<String,String> hash() { return new HashMap<String,String>(); }
    public static HashMap<String,String> hash(String k, String v) {
	HashMap<String,String> h = new HashMap<>(); h.put(k,v); return h; }
    public static HashMap<String,String> hash(String k1, String v1, String k, String v) {
	HashMap<String,String> h = hash(k1,v1); h.put(k,v); return h; }
    public static  HashMap<String,String> hash(String k2, String v2, String k1, String v1, String k, String v) {
	HashMap<String,String> h = hash(k2,v2,k1,v1); h.put(k,v); return h; }
    public static HashMap<String,String> hash(String k3, String v3, String k2, String v2, String k1, String v1, String k, String v) {
	HashMap<String,String> h = hash(k3,v3,k2,v2,k1,v1); h.put(k,v); return h; }



}


