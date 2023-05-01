import java.util.Random;
import java.util.HashMap;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	private HashMap<String, int[]> map = new HashMap<>();

	private int order;

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;

		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		String appended = text + this.NOCHARACTER;
		int len = text.length();
		for (int i = 0; i < len - this.order; i++) {
			String sub = appended.substring(i, i + this.order);
			int pos = (int) appended.charAt(i + this.order);
			if (!map.containsKey(sub)) {
				int[] arr = new int[256];
				for (int j : arr) {
					j = 0;
				}
				map.put(sub, arr);
			}
			map.get(sub)[pos] += 1;
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != this.order) {
			return -1;
		}

		int[] values = map.get(kgram);
		if (values == null) return 0;

		int total = 0;
		for (int i : values) {
			total += i;
		}
		return total;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != this.order) {
			return -1;
		}

		int[] values = map.get(kgram);
		return values == null
				? 0
				: values[(int) c];
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		if (kgram.length() != this.order) {
			return this.NOCHARACTER;
		}
		int[] values = map.get(kgram);
		if (values == null) return NOCHARACTER;

		int rand = generator.nextInt(getFrequency(kgram));
		int ascii = (int) NOCHARACTER;
		for (int i = 0; i < 256; i++) {
			rand -= values[i];
			if (rand < 0) {
				ascii = i;
				break;
			}
		}
		return (char) ascii;
	}
}
