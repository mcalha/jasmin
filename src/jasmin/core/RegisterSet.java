package jasmin.core;

/**
 * @author Yang Guo
 *         this class is a container for the registers which belong together.
 *         specially designed for the GUI
 *         E indicates the 4-byte-value
 *         X indicates the lower 2-byte-value
 *         L indicates the lowest 1-byte-value
 *         H indiacates the higher 1-byte-value of X
 */
public class RegisterSet {
	
	/**
	 * lowest byte, for example AL
	 */
	public static int _L = 1;
	
	/**
	 * second lowest byte, for example AH
	 */
	public static int _H = 2;
	
	/**
	 * lower word, for example AX
	 */
	public static int _X = 4;
	
	/**
	 * the double word, for example EAX
	 */
	public static int E_X = 8;
        
        /**
	 * the quad word, for example RAX
	 */
	public static int R__ = 16;
        
	
	/**
	 * String names
	 */
	public String L = "";
	public String H = "";
	public String E = "";
	public String X = "";
        public String R = "";
	
	/**
	 * arguments to address the register parts directly
	 */
	public Address aL, aH, aE, aX, aR;
	
	/**
	 * passes all the arguments into the datatype
	 * 
	 * @param argL
	 *        argument (address) for the lowest byte
	 * @param argH
	 *        argument (address) for the second lowest byte
	 * @param argX
	 *        argument (address) for the lower word
	 * @param argE
	 *        argument (address) for the double word
	 * @param strL
	 *        name for the lowest byte
	 * @param strH
	 *        name for the second lowest byte
	 * @param strX
	 *        name for the lower word
	 * @param strE
	 *        name for the double word
	 */
	public RegisterSet(Address argL, Address argH, Address argX, Address argE, Address argR, String strL, String strH,
			String strX, String strE, String strR) {
		aL = argL;
		aH = argH;
		aE = argE;
		aX = argX;
                aR = argR;
		L = strL;
		H = strH;
		X = strX;
		E = strE;
                R = strR;
	}
	
}