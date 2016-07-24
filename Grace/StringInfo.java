package Grace;

public class StringInfo {

    //KJX BUGGY!!
    public static String GetNextTextElement(String code, int index) {
	int cp = code.codePointAt(index);
	return CodePointToString(cp);
    }

    //KJX BUGGY!!
    public static String CodePointToString(int codePoint) {
	int[] cps = { codePoint }; //KJX EVIL BUGGY
	return new String(cps, 0, 1);
    }


    // returns an array of the character starts in s
    public static int[] ParseCombiningCharacters(String s) {
	//KJX STUPID
	int count = Character.codePointCount(s, 0, s.length());
	int[] output = new int[count];
	if (count == 0) {return output;}
	int index = 0;
	for (int i = 0; i < count; i++) {
	    output[i] = index;
	    int cp = s.codePointAt(index); 
	    index = index + Character.charCount(cp);
	}
	return output;
    }

}
