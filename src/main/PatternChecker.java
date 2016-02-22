package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternChecker {

    static StringBuffer str = new StringBuffer();
    static List<String> newlist = new ArrayList<String>();
   
    static HashMap<String, HashMap<String, Integer>> fileNameToMap = new HashMap<String, HashMap<String, Integer>>();
    
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

    public static String readFile(String fileName) {
        BufferedReader br = null;
        str.setLength(0);

        try {

                String sCurrentLine;
                    
                br = new BufferedReader(new FileReader(fileName));

                while ((sCurrentLine = br.readLine()) != null) {
                    str.append(sCurrentLine);
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
        String arr[] = str.toString().split(" ");
        for (String s : arr) {
        	newlist.add(s);
        }
        System.out.println("The length of array list is = " + newlist.size());
                
        
        return str.toString();
        
    }
    
    public static void doDiagnostic(String file) {
    	Random random = new Random();
    	int max = newlist.size()-1;
    	int min = 0;
    	long startTime = System.nanoTime();
    	
    	String input = readFile(file);
    	for (long i = 0; i < 2000000; i++) {
    		//int randomIndex = random.nextInt(max - min + 1) + min;
    		int randomIndex = (int) (Math.random() * newlist.size());
    		String pattern = newlist.get(randomIndex);
    		//System.out.println(pattern);
    		//int val = findOccurancesNaive(input, pattern);
    		//int val = findIndexed(file, input, pattern);
    		int val = findOccurancesUsingRegex(input, pattern);
    		//System.out.println(val);
    		
    	}
        long endTime = System.nanoTime();
        System.out.println("That took " + (endTime - startTime) + " nanosecond");
    	
    }
    
    

    public static void trash() {
    	
    	

            // long startTime = System.nanoTime();;//System.currentTimeMillis();

            // System.out.println("The number of occurrences are for naive: " + findOccurancesNaive(str, "France"));

            // long endTime = System.nanoTime();//System.currentTimeMillis();

            // System.out.println("That took " + (endTime - startTime) + " nanosecond");

            // preprocessString(str);

            // System.out.println("After pre preprocessString");

            // startTime = System.nanoTime();//System.currentTimeMillis();

            // System.out.println("The number of occurrences are for indexed: " + map.get("France"));

            // endTime = System.nanoTime();//System.currentTimeMillis();
            // System.out.println("That took " + (endTime - startTime) + " nanosecond");
    }

    public static void doEvaluation(String listOfFiles[], String searchTerm, int searchWay) {
        System.out.println();
        System.out.println("Search results:");
        for (String file : listOfFiles) {
            String str = "";

            str = readFile(file);

            int numberOfOccurances = 0;
            long startTime = System.nanoTime();
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
            
            long endTime = System.nanoTime();
           // System.out.println("That took " + (endTime - startTime) + " nanosecond");

            System.out.println("         " + file.substring(file.lastIndexOf("/") + 1) + " - " + numberOfOccurances);
        }

    }

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
