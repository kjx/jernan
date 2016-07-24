//
// Translated by CS2J (http://www.cs2j.com): 25/07/2016 7:36:44 a.m.
//

package Grace;

//KJX BUGGY import CS2JNet.System.LCC.Disposable;

/**
* Represents the fact that a static error occurred
*/
public class StaticErrorException  extends Exception 
{
    /**
    * Error code (X####) of this error
    */
    private String __Code;
    public String getCode() {
        return __Code;
    }

    public void setCode(String value) {
        __Code = value;
    }

    /**
    * Line number of this error
    */
    private int __Line;
    public int getLine() {
        return __Line;
    }

    public void setLine(int value) {
        __Line = value;
    }

    /**
    * Module of this error
    */
    private String __Module;
    public String getModule() {
        return __Module;
    }

    public void setModule(String value) {
        __Module = value;
    }

    /**
    * Message of this error
    */
    private String __Message;
    public String getMessage() {
        return __Message;
    }

    public void setMessage(String value) {
        __Message = value;
    }

    /**
    * @param code Error code (X####) of this error
    *  @param line Line number of this error
    *  @param module Module of this error
    *  @param message Message of this error
    */
    public StaticErrorException(String code, int line, String module, String message) throws Exception {
        setCode(code);
        setLine(line);
        setModule(module);
        setMessage(message);
    }

}


