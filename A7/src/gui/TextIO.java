package gui;

import java.io.*;

/** This class enables reading and writing text from/to a file.
 * @author MPatashnik 
 */
public class TextIO {
	
	/** Constructor: It prevents instantiation of TextIO */
	private TextIO() {}
	
	/** Write s to text file f. */
	public static void write(String f, String s) throws IOException {
		write(new File(f), s);
	}

	/** Write s to text file f.
	 * @param f - a file to write to
	 * @param s - text to write to the file f.
	 * @throws IOException
	 */
	public static void write(File f, String s) throws IOException {
		FileWriter fr= new FileWriter(f);
		BufferedWriter br= new BufferedWriter(fr);

		br.write(s);

		br.flush(); 
		br.close();
	}
	
	/** Read text file f and return it as a String.
	 * @param f - the File to read
	 * @return - a String of the contents of file f, with newlines as necessary.
	 * @throws IOException - if the file reading goes bad.
	 */
	public static String read(File f) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String s = "";
		String line = null;
		do {
			line = br.readLine();
			if (line != null) {
				s+="\n" + line;
			}
		} while (line != null);
		
		br.close();
		return s.substring(1); //Cut off preceding newline character
	}
	
	/** Read text file f  and return it with one line in each array element.
	 * @param f - the File to read
	 * @return - a String array of the contents of file f, each entry of which is a line
	 * @throws IOException - if the file reading goes bad.
	 */
	public static String[] readToArray(File f) throws IOException {
		return read(f).split("\\n");
	}
}