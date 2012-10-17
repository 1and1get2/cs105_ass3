import java.io.*;
import java.util.*;
 
/**
 * COMPSCI 105 S2 C, 2012 Assignment Three Question 1
 * 
 * @author qingquan zhu and qzhu496
 * @version 10.10
 **/

/*
 * -c file.txt file.txz file.freq 
 * -d file.txz file.freq file.txt
 * -c abr.txt abr.txz abr.freq
 * -d abr.txz abr.freq abr.cmd.txt
 */
public class TextZip {

	private final static String UPI = "qzhu496";
	private final static int asciiLength = 256; 
	private static boolean DEBUG = false;
	
	/**
	 * This method generates the huffman tree for the text: "abracadabra!"
	 * 
	 * @return the root of the huffman tree
	 */
	public static TreeNode<CharFreq> abracadbraTree() {
		TreeNode<CharFreq> n0 = new TreeNode<CharFreq>(new CharFreq('!', 1));
		TreeNode<CharFreq> n1 = new TreeNode<CharFreq>(new CharFreq('c', 1));
		TreeNode<CharFreq> n2 = new TreeNode<CharFreq>(
				new CharFreq('\u0000', 2), n0, n1);
		TreeNode<CharFreq> n3 = new TreeNode<CharFreq>(new CharFreq('r', 2));
		TreeNode<CharFreq> n4 = new TreeNode<CharFreq>(
				new CharFreq('\u0000', 4), n3, n2);
		TreeNode<CharFreq> n5 = new TreeNode<CharFreq>(new CharFreq('d', 1));
		TreeNode<CharFreq> n6 = new TreeNode<CharFreq>(new CharFreq('b', 2));
		TreeNode<CharFreq> n7 = new TreeNode<CharFreq>(
				new CharFreq('\u0000', 3), n5, n6);
		TreeNode<CharFreq> n8 = new TreeNode<CharFreq>(new CharFreq('\u0000',
				'7'), n7, n4);
		TreeNode<CharFreq> n9 = new TreeNode<CharFreq>(new CharFreq('a', 5));
		TreeNode<CharFreq> n10 = new TreeNode<CharFreq>(new CharFreq('\u0000',
				12), n9, n8);
		return n10;
	}

	/**
	 * This method decompresses a huffman compressed text file. The compressed
	 * file must be read one bit at a time using the supplied BitReader, and
	 * then by traversing the supplied huffman tree, each sequence of compressed
	 * bits should be converted to their corresponding characters. The
	 * decompressed characters should be written to the FileWriter
	 * 
	 * @param br
	 *            : the BitReader which reads one bit at a time from the
	 *            compressed file huffman: the huffman tree that was used for
	 *            compression, and hence should be used for decompression fw: a
	 *            FileWriter for storing the decompressed text file
	 */
	public static void decompress(BitReader br, TreeNode<CharFreq> huffman,
			FileWriter fw) throws Exception {
		// TODO go to hell!
		// 0 101 110 011110100010111001110
		// a\nb: 01101100
		//while (br.hasNext())  System.out.print(br.next()?"1":"0");
		
		
		//String outPut = "";
		TreeNode<CharFreq> currentTN = huffman; // new TreeNode(huffman);
		while (br.hasNext()) {
			currentTN = (br.next() ? currentTN.getRight() : currentTN.getLeft());
			if (currentTN.isLeaf()) {
				//outPut += ((CharFreq) (currentTN.getItem())).getChar();
				//outPut += (currentTN.getItem().getChar());
				if (DEBUG) System.out.print(currentTN.getItem().getChar());
				fw.write(currentTN.getItem().getChar());
				currentTN = huffman; // new TreeNode(huffman);
			}
		}
		// System.out.println("outPut: " + outPut);
		//fw.write(outPut);
		fw.flush();
	}
	public static void decompress(BitReader br, TreeNode<CharFreq> huffman,
			FileWriter fw, String input) throws Exception {
		BitReader newBR = new BitReader((String)input);
		while(newBR.hasNext()) System.out.print(newBR.next() ? "1" : "0");
		System.out.println();
		decompress(br, huffman, fw);
	}
	/**
	 * This method traverses the supplied huffman tree and prints out the codes
	 * associated with each character
	 * 
	 * @param t
	 *            : the root of the huffman tree to be traversed code: a String
	 *            used to build the code for each character as the tree is
	 *            traversed recursively
	 */
	public static void traverse(TreeNode<CharFreq> t, String code) {
		// IMPLEMENT THIS METHOD
		if (t.isLeaf()) {
			System.out.println(t.getItem().getChar() + " : " + code);
		} else {
			traverse(t.getLeft(), code + "0");
			traverse(t.getRight(), code + "1");
		}
	}

	/**
	 * This method traverses the supplied huffman tree and stores the codes
	 * associated with each character in the supplied array.
	 * 
	 * @param t
	 *            : the root of the huffman tree to be traversed code: a String
	 *            used to build the code for each character as the tree is
	 *            traversed recursively codes: an array to store the code for
	 *            each character. The indexes of this array range from 0 to 127
	 *            and represent the ASCII value of the characters.
	 */
	public static void traverse(TreeNode<CharFreq> t, String code,
			String[] codes) {
		if (t.isLeaf()) {
			if (DEBUG) System.out.println((int)t.getItem().getChar() + " : " + code);
			codes[(int) t.getItem().getChar()] = code;
		} else {
			traverse(t.getLeft(), code + "0", codes);
			traverse(t.getRight(), code + "1", codes);
		}
	}

	/**
	 * This method removes the TreeNode, from an ArrayList of TreeNodes, which
	 * contains the smallest item. The items stored in each TreeNode must
	 * implement the Comparable interface. The ArrayList must contain at least
	 * one element.
	 * 
	 * @param a
	 *            : an ArrayList containing TreeNode objects
	 * 
	 * @return the TreeNode in the ArrayList which contains the smallest item.
	 *         This TreeNode is removed from the ArrayList.
	 */
	public static TreeNode<CharFreq> removeMin(ArrayList<TreeNode<CharFreq>> a) {
		int minIndex = 0;
		for (int i = 0; i < a.size(); i++) {
			TreeNode<CharFreq> ti = a.get(i);
			TreeNode<CharFreq> tmin = a.get(minIndex);
			if ((ti.getItem()).compareTo(tmin.getItem()) < 0)
				minIndex = i;
		}
		TreeNode<CharFreq> n = a.remove(minIndex);
		return n;
	}

	/**
	 * This method counts the frequencies of each character in the supplied
	 * FileReader, and produces an output text file which lists (on each line)
	 * each character followed by the frequency count of that character. This
	 * method also returns an ArrayList which contains TreeNodes. The item
	 * stored in each TreeNode in the returned ArrayList is a CharFreq object,
	 * which stores a character and its corresponding frequency
	 * 
	 * @param fr
	 *            : the FileReader for which the character frequencies are being
	 *            counted pw: the PrintWriter which is used to produce the
	 *            output text file listing the character frequencies
	 * 
	 * @return the ArrayList containing TreeNodes. The item stored in each
	 *         TreeNode is a CharFreq object.
	 */
	public static ArrayList<TreeNode<CharFreq>> countFrequencies(FileReader fr,
			PrintWriter pw) throws Exception {
		ArrayList<TreeNode<CharFreq>> arrayListTN = new ArrayList<TreeNode<CharFreq>>();
		BufferedWriter bw = new BufferedWriter(pw);
		int[] charAr = new int[asciiLength];
		// initialize
		for (int i : charAr)
			i = 0;
		// read from file save into the array: charAr
		int readChar;
		while ((readChar = fr.read()) != -1) {
			// System.out.println((char)readChar);
			//if (DEBUG) System.out.print((int)readChar + " ");
			charAr[readChar]++;
		}
		// write to file
		// for (int i : charAr) pw.write((char) i + "\n");
		for (int i = 0; i < charAr.length; i++) {
			// TODO !!!!
			if (charAr[i] != 0)
				bw.write((char) i + "\t" + charAr[i] + "\n");
			
			// pw.println((char) i + "\t" + charAr[i]);
		}
		bw.flush();

		/*
		 * Create and return an array list of tree node from the non-zero
		 * frequencies characters in the frequency array. Note that the tree
		 * node list is originally arranged according to their ASCII order from
		 * left to right.
		 */
		for (int i = 0; i < charAr.length; i++) {
			if (charAr[i] != 0)
				arrayListTN.add(new TreeNode<CharFreq>(new CharFreq((char) i,
						charAr[i])));
		}
		// arrayListTN.toString();
		return arrayListTN;
	}

	/**
	 * This method builds a huffman tree from the supplied ArrayList of
	 * TreeNodes. Initially, the items in each TreeNode in the ArrayList store a
	 * CharFreq object. As the tree is built, the smallest two items in the
	 * ArrayList are removed, merged to form a tree with a CharFreq object
	 * storing the sum of the frequencies as the root, and the two original
	 * CharFreq objects as the children. The right child must be the second of
	 * the two elements removed from the ArrayList (where the ArrayList is
	 * scanned from left to right when the minimum element is found). When the
	 * ArrayList contains just one element, this will be the root of the
	 * completed huffman tree.
	 * 
	 * @param trees
	 *            : the ArrayList containing the TreeNodes used in the algorithm
	 *            for generating the huffman tree
	 * 
	 * @return the TreeNode referring to the root of the completed huffman tree
	 */
	public static TreeNode<CharFreq> buildTree(
			ArrayList<TreeNode<CharFreq>> trees) throws IOException {

		while (trees.size() != 1) {
			int minIndex1 = (trees.get(0).getItem().getFreq() < trees.get(1)
					.getItem().getFreq() ? 0 : 1),
			// TODO: can be optimized
			minIndex2 = (minIndex1 == 0 ? 1 : 0);

			// get the most least frequency
			for (int i = 0; i < trees.size(); i++) {
				if (trees.get(i).getItem().getFreq() < trees.get(minIndex1)
						.getItem().getFreq()) {
					int temp = minIndex1;
					minIndex1 = i;
					minIndex2 = temp;
				} else if (i != minIndex1
						&& trees.get(i).getItem().getFreq() < trees
								.get(minIndex2).getItem().getFreq()) {
					minIndex2 = i;
				}
			}
			// create a new TreeNode as the root of the two
			trees.add(new TreeNode<CharFreq>(new CharFreq('\u0000', trees
					.get(minIndex1).getItem().getFreq()
					+ trees.get(minIndex2).getItem().getFreq()), trees
					.get(minIndex1), trees.get(minIndex2)));
			trees.remove(Math.max(minIndex1, minIndex2));
			trees.remove(Math.min(minIndex1, minIndex2));
		}
		return trees.get(0);
	}

	/**
	 * This method compresses a text file using huffman encoding. Initially, the
	 * supplied huffman tree is traversed to generate a lookup table of codes
	 * for each character. The text file is then read one character at a time,
	 * and each character is encoded by using the lookup table. The encoded bits
	 * for each character are written one at a time to the specified BitWriter.
	 * 
	 * @param fr
	 *            : the FileReader which contains the text file to be encoded
	 *            huffman: the huffman tree that was used for compression, and
	 *            hence should be used for decompression bw: the BitWriter used
	 *            to write the compressed bits to file
	 */
	public static void compress(FileReader fr, TreeNode<CharFreq> huffman,
			BitWriter bw) throws Exception {

		// IMPLEMENT THIS METHOD
		// BufferedWriter bfw = new BufferedWriter(bw);
		String[] codes = new String[asciiLength];
		traverse(huffman, "", codes);
		int readChar;
		if (DEBUG) System.out.println("in compress pethod: ");
		while ((int) (readChar = fr.read()) != -1) {
			// System.out.print((char)readChar + ": ");
			// bw.writeBit(Integer.parseInt(codes[readChar]));
			// for (Char singleChar : codes[readChar]){
			// bw.writeBit(Integer.parseInt(singleChar));
			// }
			for (int i = 0; i < codes[readChar].length(); i++) {
				bw.writeBit(Integer.parseInt("" + codes[readChar].charAt(i)));
				if (DEBUG) System.out.print(codes[readChar].charAt(i) + " ");
			}
		}
	}

	/**
	 * This method reads a frequency file (such as those generated by the
	 * countFrequencies() method) and initialises an ArrayList of TreeNodes
	 * where the item of each TreeNode is a CharFreq object storing a character
	 * from the frequency file and its corresponding frequency. This method
	 * provides the same functionality as the countFrequencies() method, but
	 * takes in a frequency file as parameter rather than a text file.
	 * 
	 * @param inputFreqFile
	 *            : the frequency file which stores characters and their
	 *            frequency (one character per line)
	 * 
	 * @return the ArrayList containing TreeNodes. The item stored in each
	 *         TreeNode is a CharFreq object.
	 */
	public static ArrayList<TreeNode<CharFreq>> readFrequencies(
			String inputFreqFile) throws Exception {

		// IMPLEMENT THIS METHOD
		BufferedReader br = new BufferedReader(new FileReader(inputFreqFile));
		int[] charAr = readFrequencies(br);
		ArrayList<TreeNode<CharFreq>> arrayListTN = new ArrayList<TreeNode<CharFreq>>();
		for (int i = 0; i < charAr.length; i++) {
			if (charAr[i] != 0)
				arrayListTN.add(new TreeNode<CharFreq>(new CharFreq((char) i,
						charAr[i])));
		}
		// arrayListTN.toString();
		return arrayListTN;
	}

	// TODO
	public static int[] readFrequencies(BufferedReader br) throws Exception {
		int[] charAr = new int[asciiLength];

		String nextLine;
		Scanner sc;
		int readChar;
		int freq;
		// TODO WADAFQ
/*		while ((nextLine = br.readLine()) != null) {
			//System.out.println("nextLine: " + nextLine);
			if (nextLine.length() == 0) continue;
			// new line
			sc = new Scanner(nextLine);
			if(nextLine.length() - nextLine.trim().length() == 1){
				readChar = '\n';
				freq = sc.nextInt();
			} else if(nextLine.length() - nextLine.trim().length() == 2){
				// /t
				readChar = nextLine.charAt(0);
				freq = sc.nextInt();
			}else {
				readChar = sc.next().charAt(0);
				freq = sc.nextInt();
			}
			System.out.println("readChar: " + (int)readChar + " freq: " + freq);
			charAr[(int) readChar] = freq;
		}*/
		// new thought
		if (DEBUG) System.out.println("readFrequencies");
		while((readChar = br.read()) != -1){
			br.read();
			// TODO might need a better way to read integers
			String freqS = "";
			while ((freq = br.read()-48) >=0 && (freq <= 9)){
				freqS += freq;
			}
			freq = Integer.parseInt(freqS);
			charAr[readChar] = freq;
			//br.read();
			if (DEBUG) System.out.println(readChar + " : " + freq);
		}
/*		sc = new Scanner(br);
		while(sc.hasNext()){
			System.out.print(sc.next());
//			String charInt = sc.next();
//			int frep = sc.nextInt();
//			charAr[(int) charInt.charAt(0)] = frep;
//			sc.next();
		}*/
		

		// for (int i = 0; i < charAr.length; i++){
		// System.out.println("char: " + (char)i + " frequency: " + charAr[i]);
		// }
		return charAr;
	}

	/**
	 * This method prints out the sizes (in bytes) of the compressed and the
	 * original files, and computes and prints out the compressed ratio.
	 * 
	 * @param file1
	 *            : full name of the first file file2: full name of the second
	 *            file
	 */
	public static void statistics(String file1, String file2) {
		File txzFile = new File(file1);
		File txtFile = new File(file2);
		long f2Len = txzFile.length();
		long f1Len = txtFile.length();
		System.out.println("Size of the compressed file: " + f2Len + " bytes");
		System.out.println("Size of the original file: " + f1Len + " bytes");
		// close
		// txzFile.close();
		// txtFile.close();
		System.out.println("Compressed ratio: " + 100
				* (f2Len / (double) f1Len) + "%");
	}

	/*
	 * This TextZip application should support the following command line flags:
	 * 
	 * QUESTION 1 PART 1 ================= -a : this uses a default prefix code
	 * tree and its compressed file, "a.txz", and decompresses the file, storing
	 * the output in the text file, "a.txt". It should also print out the size
	 * of the compressed file (in bytes), the size of the decompressed file (in
	 * bytes) and the compression ratio
	 * 
	 * QUESTION 1 PART 2 ================= -f : given a text file (args[1]) and
	 * the name of an output frequency file (args[2]) this should count the
	 * character frequencies in the text file and store these in the frequency
	 * file (with one character and its frequency per line). It should then
	 * build the huffman tree based on the character frequencies, and then print
	 * out the prefix code for each character
	 * 
	 * QUESTION 1 PART 3 ================= -c : given a text file (args[1]) and
	 * the name of the output compressed file (args[2]) and the name of an
	 * output frequency file (args[3]), this should compress the file
	 * 
	 * QUESTION 1 PART 4 ================= -d : given a compressed file
	 * (args[1]) and its corresponding frequency file (args[2]) and the name of
	 * the output decompressed text file (args[3]), this should decompress the
	 * file
	 */

	public static void main(String[] args) throws Exception {

		/*
		 * This is a standard sample command line implementation.
		 */
//		if (args[0] != null && args[4] != null){
//			DEBUG = Integer.parseInt(args[4]) == 1 ? true : false;
//		}
		
		if (args[0] != null && args[0].equals("-a")) {
			System.out.println("a.txz decompressed by " + UPI);
			BitReader br = new BitReader("a.txz");
			FileWriter fw = new FileWriter("a.txt");
			// Get the default prefix code tree
			TreeNode<CharFreq> tn = abracadbraTree();
			// Decompress the default file "a.txz"
			decompress(br, tn, fw);
			// Close the ouput file
			fw.close();
			// Output the compression ratio
			statistics("a.txz", "a.txt");
		}
		
		else if (args[0] != null && args[0].equals("-f")) {
			System.out.println(args[1] + " prefix codes by " + UPI
					+ "\ncharacter code:");
			FileReader fr = new FileReader(args[1]);
			PrintWriter pw = new PrintWriter(new FileWriter(args[2]));
			// Calculate the frequencies from the .txt file
			ArrayList<TreeNode<CharFreq>> trees = countFrequencies(fr, pw);
			// Close the files
			fr.close();
			pw.close();
			// Build the huffman tree
			TreeNode<CharFreq> n = buildTree(trees);
			// Display the codes
			traverse(n, "");
		}

		else if (args[0] != null && args[0].equals("-c")) {
			System.out.println(args[1] + " compressed by " + UPI);
			FileReader fr = new FileReader(args[1]);
			PrintWriter pw = new PrintWriter(new FileWriter(args[3]));
			// Calculate the frequencies from the .txt file
			ArrayList<TreeNode<CharFreq>> trees = countFrequencies(fr, pw);
			fr.close();
			pw.close();
			// Build the huffman tree
			TreeNode<CharFreq> n = buildTree(trees);
			// Compress the .txt file
			fr = new FileReader(args[1]);
			BitWriter bw = new BitWriter(args[2]);
			compress(fr, n, bw);
			bw.close();
			fr.close();
			// Output the compression ratio
			statistics(args[2], args[1]);
		}

		else if (args[0] != null && args[0].equals("-d")) {
			System.out.println(args[1] + " decompressed by " + UPI);
			// Read in the frequencies from the .freq file
			ArrayList<TreeNode<CharFreq>> a = readFrequencies(args[2]);
			// Build the huffman tree
			TreeNode<CharFreq> tn = buildTree(a);
			// Decompress the .txz file
			BitReader br = new BitReader(args[1]);
			//BitReader br = new BitReader("a.txz");
			FileWriter fw = new FileWriter(args[3]);
//			System.out.println("-------  args[1]  ------- : " + args[1]);
//			decompress(br, tn, fw, args[1]);
//			System.out.println("-------  a.txz  -------");
//			decompress(br, tn, fw, "a.txz");
//			System.out.println("-------  null  -------");
			decompress(br, tn, fw);
			
			fw.close();
			// Output the compression ratio
			statistics(args[1], args[3]);
		}
		else {
			System.out.println(
					"---------------   WRONG ARGUMENTS   ---------------\n" + 
					"valid input:\n" +
					"	-a\n" +
					"	-f <input txt file>(to get frequency, i.e. a.txt)	<output.freq>(i.e. a.freq)\n" +
					"	-d <input txt file>(to decompress, i.e. file.txz)	<input freq file>(frequency file i.e. file.freq) <output txt file>\n" +
					" 	-c file.txt file.txz file.freq" +
					"  \n" +
					"  \n"
					);
		}
	} // end main method
}
