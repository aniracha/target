package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternChecker {

	//Stringbuffer so that we are not creating String objects all the time.
    static StringBuffer str = new StringBuffer();
    
    //This id for diagnostics.
    static List<String> newlist = new ArrayList<String>();
   
    // A map of file names to the occurrences of the various patterns in the file.
    static HashMap<String, HashMap<String, Integer>> fileNameToMap = new HashMap<String, HashMap<String, Integer>>();
    
    
    /*
     * Finds the occurrences of the pattern in an input String.
     * 
     * @param input
     * 			The input String
     * @param searchTerm
     * 			The pattern to be searched in the input String.
     * @return The number of times the pattern occurs in the input String.
     * 
     * */
    public static int findOccurancesNaive(String input, String pattern) {
        int lengthOfPattern = pattern.length();
        int lengthOfInput = input.length();
        int numberOfOccurances = 0;

        int j = 0;
        for (int i = 0; i <= lengthOfInput - lengthOfPattern; i++) {
            for (j = 0; j < lengthOfPattern; j++) {
                if (input.charAt(i+j) != pattern.charAt(j)) {
                    break;
                }
            }

            if (j == lengthOfPattern) {
                numberOfOccurances++;
            }
        }
        return numberOfOccurances;
    }
    
    /*
     * Private function to pre-process the String 
     * 
     * */
    private static void preprocessFile(String input, HashMap<String, Integer> map) {
    	if (!map.isEmpty()) {
    		return;
    	}
        int lengthOfInput = input.length();
        StringBuffer substring = new StringBuffer();
        for (int i = 0; i < lengthOfInput; i++) {
            
            for (int j = i; j < lengthOfInput; j++) {
            	substring.append(input.charAt(j));
                if (map.containsKey(substring.toString())) {
                    int value = map.get(substring.toString());
                    map.put(substring.toString(), ++value);
                } else {
                    map.put(substring.toString(), 1);
                }
            }
            substring.setLength(0);
        }
    }

    /*
     * Finds the occurrences of the pattern in an input String. The file is also given as an input
     * so that occurrences are stored per file. This uses hashmap to preprocess the String.
     * 
     * @param file
     *         The name of the file which comprises of the input String
     * @param input
     * 			The input String
     * @param searchTerm
     * 			The pattern to be searched in the input String.
     * @return The number of times the pattern occurs in the input String.
     * 
     * */
    public static int findIndexed(String file, String input, String searchTerm) {
    	HashMap<String, Integer> map = null;
    	if (fileNameToMap.get(file) == null) {
    		map = new HashMap<String, Integer>();
    		fileNameToMap.put(file, map);
    		preprocessFile(input, map);
    	} else {
    		map = fileNameToMap.get(file);
    	}
    	
        if (map.get(searchTerm) != null) {
            return map.get(searchTerm);
        } else {
            return 0;
        }
    }

    
    /*
     * Finds the occurrences of the regular expression pattern in an input String
     * 
     * @param input
     * 			The input String
     * @param regex
     * 			The regular expression to be searched in the input String.
     * @return The number of times the regex occurs in the input String.
     * 
     * */
    public static int findOccurancesUsingRegex(String input, String regex) {
        int lengthOfInput = input.length();
        int numberOfOccurances = 0;
        
        try {
        	Pattern p = Pattern.compile(regex);
            StringBuffer substring = new StringBuffer();
            for (int i = 0; i < lengthOfInput; i++) {
            	
                for (int j = i; j < lengthOfInput; j++) {
                	substring.append(input.charAt(j));
                	Matcher m = p.matcher(substring.toString());
                    if (m.matches()) {
                        numberOfOccurances++;
                    }
                }
                substring.setLength(0);
            }
        } catch(PatternSyntaxException e) {
        	return 0;
        }

        return numberOfOccurances;
    }

    /*
     * Reads the contents of a file and returns the contents.
     * 
     * @param fileName
     * 			The name of the file.
     * @return The contents of the file as a String
     * 
     * */
    public static String readFile(String fileName) {
        BufferedReader br = null;
        str.setLength(0);

        try {

                String currentLIne;
                    
                br = new BufferedReader(new FileReader(fileName));

                while ((currentLIne = br.readLine()) != null) {
                    str.append(currentLIne);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        
        //This is used for diagnostic purposes.
        String arr[] = str.toString().split(" ");
        for (String s : arr) {
        	newlist.add(s);
        }
                
        
        return str.toString();
        
    }
    
    /*
     * Calculates the time it takes to run a 2M query on the various search methods. 
     * 
     * @param file
     * 			A String denoting which file to search.
     * 
     * */
    public static void doDiagnostic(String file) {
    	long startTime = System.nanoTime();
    	
    	String input = readFile(file);
    	for (long i = 0; i < 2000000; i++) {
    		
    		int randomIndex = (int) (Math.random() * newlist.size());
    		String pattern = newlist.get(randomIndex);
    		
    		//findOccurancesNaive(input, pattern);
    		//findIndexed(file, input, pattern);
    		findOccurancesUsingRegex(input, pattern);
    		
    	}
        long endTime = System.nanoTime();
        System.out.println("That took " + (endTime - startTime) + " nanosecond");
    	
    }
    
    

    /*
     * Given a list of files, a search term and the search way/method, finds the occurrences
     * of the search term in the files.
     * 
     * @param listOfFiles
     * 			An array of Strings containing the list of files.
     * @param searchTerm
     * 			The search String
     * @param searchWay 
     * 			An integer denoting which method should be used to find the pattern.
     * 
     * */
    public static void doEvaluation(String listOfFiles[], String searchTerm, int searchWay) {
        System.out.println();
        System.out.println("Search results:");
        long startTime = System.nanoTime();
        for (String file : listOfFiles) {
            String str = "";

            str = readFile(file);

            int numberOfOccurances = 0;
           
            switch (searchWay) {
                case 1: numberOfOccurances = findOccurancesNaive(str, searchTerm);
                        break;
                case 2: numberOfOccurances = findOccurancesUsingRegex(str, searchTerm);
                        break;
                case 3: numberOfOccurances = findIndexed(file, str, searchTerm);
                        break;
                default: System.out.println("Error inputing search option");
                        break;

            }

            System.out.println("         " + file.substring(file.lastIndexOf("/") + 1) + " - " + numberOfOccurances + " matches");
        }
        long endTime = System.nanoTime();
        System.out.println();
        System.out.println("Elapsed time " + (endTime - startTime)/1000000 + " millisecond");

    }

    /*
     * Entry point of the program. Called from the main method.
     * Processes the input files and finds the presence of the pattern in the search files.
     * 
     * */
    public static void processInput() {
        System.out.println("Enter the search term:");
        Scanner reader = new Scanner(System.in);
        String searchTerm = reader.next();
        System.out.println();
        System.out.println("Search method: 1) String Match 2) Regular Expression 3) Indexed");
        reader = new Scanner(System.in);
        int searchWay = reader.nextInt();
        
        String listOfFiles[] = {"/Users/anirbanacharjee/Downloads/sample_text_2/warp_drive.txt",
                                 "/Users/anirbanacharjee/Downloads/sample_text_2/test1.txt",
                                "/Users/anirbanacharjee/Downloads/sample_text_2/test2.txt"
        						};
        doEvaluation(listOfFiles, searchTerm, searchWay);

    }

    public static void main(String[] args) {

        processInput();
    	//doDiagnostic("/Users/anirbanacharjee/Downloads/sample_text_2/warp_drive.txt");

    }
}
