public class BaseDecoder {

	public static class Dec {
		public static void decode (String input) {
			if (!input.matches("^[0-9,-]+$")) {
				error_base = true;
				return;
			}
			long tempdec = Long.parseLong(input);
			if (input.length() > 20) {
				error_lim = true;
				return;
			}
			
			boolean neg = tempdec < 0;
			if (neg) {
				tempdec *= -1;
			}
			binary = long2Bin(tempdec);
			
			if (neg) {
				sig_decimal = input;

				boolean flip = false;						// negate and add one method shortcut
				String newbin = "";
				for (int i = num_bits - 1; i >= 0; i--) {
					if (binary.charAt(i) == '0' && !flip) {
						newbin = "0" + newbin;
					} else if (binary.charAt(i) == '1' && !flip) {
						newbin = "1" + newbin;
						flip = true;
					} else {
						if (binary.charAt(i) == '1') {
							newbin = "0" + newbin;
						} else {
							newbin = "1" + newbin;
						}
					}
				}
				binary = newbin;
				unsig_decimal = bin2Unsig(newbin);
				
			} else {
				unsig_decimal = input;
				if (binary.charAt(0) == '1') {
					long sig = get2ToThe(num_bits - 1);
					sig = tempdec - (2 * sig);
					sig_decimal = Long.toString(sig);
				} else {
					sig_decimal = input;
				}
			}
			
			hexadecimal = bin2Hex(binary);
		}
		
	}
	
	public static class Hex {
		public static void decode (String input) {
			if (!input.matches("^[0-9A-Fa-f]+$")) {
				error_base = true;
				return;
			}
			if (input.length() > num_bits / 4) {
				error_lim = true;
				return;
			}
			while (input.length() < num_bits / 4) {
				input = "0" + input;
			}
			input = input.toUpperCase();
			
			hexadecimal = input;
			binary = hex2Bin(input);
			unsig_decimal = bin2Unsig(binary);
			sig_decimal = bin2Sig(binary);
			
		}
	}
	
	public static class Bin {
		public static void decode (String input) {
			if (!input.matches("^[01]+$")) {
				error_base = true;
				return;
			}
			if (input.length() > num_bits) {
				error_lim = true;
				return;
			}
			while (input.length() < num_bits) {
				input = "0" + input;
			}
			binary = input;
			hexadecimal = bin2Hex(input);
			unsig_decimal = bin2Unsig(input);
			sig_decimal = bin2Sig(input);
		}
	}
	
	private static String unsig_decimal, sig_decimal, hexadecimal, binary;
	public static int num_bits = 32;
	public static boolean error_base, error_lim;
	
	public static String[] getSolution () {
		String[] temp = {unsig_decimal, sig_decimal, hexadecimal, binary};
		return temp;
	}

	public static void resetError () {
		error_base = false;
		error_lim = false;
	}
	
	private static long get2ToThe (int pow) {
		long total = 1;
		while (pow > 0) {
			total *= 2;
			pow--;
		}
		return total;
	}
	
	private static long get16ToThe (int pow) {
		long total = 1;
		while (pow > 0) {
			total *= 16;
			pow--;
		}
		return total;
	}
	
	private static String long2Bin (long temp) {
		String ret = "";
		while (ret.length() < num_bits) {
			ret = (temp % 2) + ret;
			temp /= 2;
		}
		return ret;
	}
	
	private static String bin2Hex (String input) {
		String ret = "";
		for (int i = 0; i < num_bits; i += 4) {
			switch (input.substring(i, i + 4)) {
				case "0000": ret += "0"; break; case "0001": ret += "1"; break; case "0010": ret += "2"; break;
				case "0011": ret += "3"; break; case "0100": ret += "4"; break; case "0101": ret += "5"; break;
				case "0110": ret += "6"; break; case "0111": ret += "7"; break; case "1000": ret += "8"; break;
				case "1001": ret += "9"; break; case "1010": ret += "A"; break; case "1011": ret += "B"; break;
				case "1100": ret += "C"; break; case "1101": ret += "D"; break; case "1110": ret += "E"; break;
				case "1111": ret += "F"; break; default: System.out.println("Something went wrong (bin2Hex)");
			}
		}
		return ret;
	}
	
	private static String hex2Bin (String input) {
		String ret = "";
		for (int i = 0; i < num_bits / 4; i++) {
			switch (input.substring(i, i + 1)) {
				case "0": ret += "0000"; break; case "1": ret += "0001"; break; case "2": ret += "0010"; break;
				case "3": ret += "0011"; break; case "4": ret += "0100"; break; case "5": ret += "0101"; break;
				case "6": ret += "0110"; break; case "7": ret += "0111"; break; case "8": ret += "1000"; break;
				case "9": ret += "1001"; break; case "A": ret += "1010"; break; case "B": ret += "1011"; break;
				case "C": ret += "1100"; break; case "D": ret += "1101"; break; case "E": ret += "1110"; break;
				case "F": ret += "1111"; break; default: System.out.println("Something went wrong (hex2Bin)");
			}
		}
		return ret;
	}
	
	private static String bin2Unsig (String bin) {
		long ret = 0;
		int index = 0;
		for (int pow = num_bits - 1; pow >= 0; pow--) {
			String frac = bin.substring(index, index + 1);
			ret += (Long.parseLong(frac) * get2ToThe(pow));
			index++;
		}
		return Long.toString(ret);
	}
	
	private static String bin2Sig (String bin) {
		long ret = 0;
		int index = 0;
		for (int pow = num_bits - 1; pow >= 0; pow--) {
			String frac = bin.substring(index, index + 1);
			if (index == 0) {
				ret -= (Long.parseLong(frac) * get2ToThe(pow));
			} else {
				ret += (Long.parseLong(frac) * get2ToThe(pow));
			}
			index++;
		}
		return Long.toString(ret);
	}
}