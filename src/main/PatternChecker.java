package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PatternChecker {
    
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

    public static int findIndexed(String input, String searchTerm) {
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
        int lengthOfInput = input.length();

        for (int i = 0; i < lengthOfInput; i++) {
            String substring = "";
            for (int j = i; j < lengthOfInput; j++) {
                substring += input.charAt(j);
                //System.out.println(substring);

                if (map.containsKey(substring)) {
                    int value = map.get(substring);
                    map.put(substring, ++value);
                } else {
                    map.put(substring, 1);
                }
            }
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

        for (int i = 0; i < lengthOfInput; i++) {
            String substring = "";
            for (int j = i; j < lengthOfInput; j++) {
                substring += input.charAt(j);
                if (Pattern.matches(regex, substring)) {
                    System.out.println(substring);
                    numberOfOccurances++;
                }
                
            }
        }

        return numberOfOccurances;
    }

    public static String readFile(String fileName) {
        BufferedReader br = null;
        String str = "";

        try {

                String sCurrentLine;
                    
                br = new BufferedReader(new FileReader(fileName));

                while ((sCurrentLine = br.readLine()) != null) {
                    str += sCurrentLine;
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
        return str;
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
            switch (searchWay) {
                case 1: numberOfOccurances = findOccurancesNaive(str, searchTerm);
                        break;
                case 2: numberOfOccurances = findOccurancesUsingRegex(str, searchTerm);
                        break;
                case 3: numberOfOccurances = findIndexed(str, searchTerm);
                        break;
                default: System.out.println("Error inputing search option");
                        break;

            }

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
        String listOfFiles[] = {"/Users/anirbanacharjee/Downloads/sample_text/warp_drive.txt"};
        doEvaluation(listOfFiles, searchTerm, searchWay);

    }

    public static void main(String[] args) {

        processInput();

    }
}
