This file contains information on what the algorithm in this project does and how to run it.

The first section is about the algorithms and how they perform on the different datasets and their runtime complexities.

Section one:

The code in this project searches for a pattern in a text file. Three types of search methods are used :
 the naive way of searching a string within a text file; using regular expressions; using pre-processing hash map.

 Let us investigate each approach :

 1. Naive approach :

 I have used a naive string matching algorithm where the pattern string (the smaller string) is slid
 over the larger String (which is the contents of the file). The worst case algorithmic time complexity for this
 method is O(m*n), where m = length of the pattern, n = length of the contents of the file.

 On doing a performance test over 2M search terms I got 6974.396 milli seconds.

 The naive algorithm could be improved by using state of the art algorithms like KMP or Boyer Moore, which
 brings down the average search time to linear or even sublinear in the case of Boyer Moore.

 2. Regex approach :

 The regex approach is even more time consuming than the naive method described above. The reason is in this method,
 we need to go through all the substrings inside the file and check each substring with the pattern. For a text
 of length 'n' the number of substrings is n^2 and hence it takes O(n^2) time.

 The performance test took a very very long time.

 3. The pre-processing approach :

 In this approach, I have created a hashmap for each of the substrings of the text. Granted that the preprocessing takes
 O(n^2) time, subsequent searches are done in O(1) time.

 There is a problem with this approach though. When I tried with large files, the code blew up giving a out of
 memory exception (even after I threw more heap at it). This is however expected. For a string of length 'n',
 it has n^2 substrings and as the value of n increases, the hashmap explodes. So this preprocessing is only valid
 for smaller files. This method only works for the file "warp_drive" and doesn't work for the other two due to the large size of the files.

 I have tried the Trie data structure too for preprocessing larger files, but even that gave me a out of memory
 exception. The Trie would give a O(m)  (m being the size of the search patter) access time as opposed to O(1) in a hashmap.

 The performance test gave me a time of 2313.60 milli seconds for 2M searches. This is expectedly lesser than the
 naive approach as the strings have already been processed and their occurrences stored in the hash map, so
 retrieving them takes on O(1) time.

 Now coming to the problem of handing large content, we could use map reduce to solve this problem. We could also use distributed hash table.
 If we have very large volume of requests coming in say 5000 requests/sec, one option would be to use a caching mechanism (LRU cache), where we cache the most frequently used words and their occurrences.

Section two:

How to run?

Please checkout the project through github. The only file you’ll need is PatternChecker.java.
Please mention the files you want to search (fully qualified paths) in the  "listOfFiles” array in the “ processInput” method and compile and run the program.

javac PatternCheck.java
java PatternCheck

OR

The green button in Eclipse.
