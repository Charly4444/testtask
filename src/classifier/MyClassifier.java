package classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClassifier {
	public static void main(String[] args) {
		// INTIAL SETUPS
		String prefix = "";
		String input_path = null;
		String output_dir = null;
		boolean stats=false, fullstats=false, canAppend=false;
		for (int i=0; i<args.length; i++) {
			switch (args[i]) {
			case "-i": 
				input_path = args[++i];
				break;
			case "-o":
				output_dir = args[++i];
				break;
			case "-a":
				canAppend=true;
				break;
			case "-s":
				stats=true;	//allow stats
				break;
			case "-f":
				fullstats=true;	//allow more stats
				break;
			case "-p":
				prefix=args[++i];
				break;
			default:
				System.out.println("invalid specifier");
				return;
			}
		}
		if (input_path==null || output_dir==null) {
			System.out.println("Usage java MyClassifier.java -i <input_filepath> -o <output_dir>");
			return;
		}
//		============= if Paths received we check they're valid ============
		File inputFile = new File(input_path);
		File outputDirectory = new File(output_dir);
		if (!inputFile.exists() || !inputFile.isFile()) {
			System.out.println("The input filepath is invalid!");
			return;
		}
		if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
			System.out.println("The output directory does not exist!");
			return;
		}
		
//		============= if Everything is okay, then proceed ============
		// paths for use
		String strings_path = output_dir+"/"+prefix+"result_strings.txt";
		String integers_path = output_dir+"/"+prefix+"result_integers.txt";
		String floats_path  = output_dir+"/"+prefix+"result_floats.txt";
		
		// Open the reading and writing operations as 'with' so it auto clears
		try (	
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input_path), StandardCharsets.UTF_8));
				BufferedWriter bw_s = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strings_path, canAppend), StandardCharsets.UTF_8));
				BufferedWriter bw_i = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(integers_path, canAppend), StandardCharsets.UTF_8));
				BufferedWriter bw_f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(floats_path, canAppend), StandardCharsets.UTF_8));
			){
			
			// Read a line
			String line = br.readLine();
			int no_string=0, no_integer=0, no_floats=0; float sum_floats=0.0f; long sum_integer=0;
			List<Integer> stringsLength = new ArrayList<Integer>();
			while (line != null) {
				// pattern and matcher by regex
				Pattern s_pattern = Pattern.compile("^(\\s*[\\u0041-\\u005A\\u0061-\\u007A\\u0410-\\u042F\\u0430-\\u044F\\u0401\\u0451]+\\s*)+$");
				Pattern i_pattern = Pattern.compile("^\\d+$");
				Pattern f1_pattern = Pattern.compile("^\\d+(\\.\\d+)$");
				Pattern f2_pattern = Pattern.compile("^-\\d+(\\.\\d+)$");
				Pattern f3_pattern = Pattern.compile("^\\d+(\\.\\d+)(E[+-]?\\d+)$");
				Pattern f4_pattern = Pattern.compile("^-\\d+(\\.\\d+)(E[+-]?\\d+)$");
				
				Matcher s_matcher = s_pattern.matcher(line);
				Matcher i_matcher = i_pattern.matcher(line);
				Matcher f1_matcher = f1_pattern.matcher(line);
				Matcher f2_matcher = f2_pattern.matcher(line);
				Matcher f3_matcher = f3_pattern.matcher(line);
				Matcher f4_matcher = f4_pattern.matcher(line);
				
				if (s_matcher.matches()) {
					bw_s.write(line+"\n");
					stringsLength.add(line.length());
					no_string++;
				}else if (i_matcher.matches()) {
					bw_i.write(line+"\n");
					sum_integer+=Long.parseLong(line);
					no_integer++;
				}else if (f1_matcher.matches() || f2_matcher.matches() || f3_matcher.matches() || f4_matcher.matches()) {
					bw_f.write(line+"\n");
					sum_floats+=Float.parseFloat(line);
					no_floats++;
				}
					
				line = br.readLine();
			}
//			show statistics
			if (stats) {showStats(no_string,no_integer,no_floats);}
			if (!stats && fullstats) {showFullstats(no_string,no_integer,no_floats,stringsLength,sum_floats,sum_integer);};
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
//	METHOD TO SHOW SOME STATS
	public static void showStats(int no_s, int no_i, int no_f) {
		System.out.printf("Strings: %d \n", no_s);
		System.out.printf("Integers: %d \n", no_i);
		System.out.printf("Floats: %d \n", no_f);
		return;
	}
	public static void showFullstats(int no_s, int no_i, int no_f, List<Integer> strlengths, float sum_flt, long sum_int) {
		if(!strlengths.isEmpty()) {
			int maxstr = Collections.max(strlengths);
			int minstr = Collections.min(strlengths);
			System.out.printf("min string length: %d\n", minstr);
			System.out.printf("max string length: %d\n", maxstr);
		}
		if (sum_int>0) {
			System.out.printf("Sum of integers= %d, average= %d\n", sum_int, (sum_int/no_i));
		}
		if (sum_flt>0.0f) {
			System.out.printf("Sum of floats= %.2f, average= %.2f\n", sum_flt, (sum_flt/no_f));
		}
		return;
	}

}
