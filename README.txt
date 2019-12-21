The archive contains the following classes,DictionaryObject,IrwsAssignment,IndexObject,OutputQueryObject and the Stemmer class.
The main method is in IrwsAssignment and it contains two classes which extend thread.
DictionaryObject,IndexObject and OutputQueryObject are objects which are used by IrwsAssignment.
Part1 and Part2 are the two classes in IrwsAssignment.
Preprocessing,creation of dictionary,idf calculation,tdfidf calculation,getting the weight,finding the length of each document,
getting the normalised weight is done in Part1.At the end of Part1,the length of each document is generated as an output and stored in the textfile "Length of document.txt".
Part1 runs only once everytime the main program is ran.
Part2 deals with query handling,a query is accepted from the user and is compared with the similarity score from the normalised weight dictionary calculated in Part1.At the end of the calculation,a text file is generated as an output.
This text file contains the rank,the document id and its similarity score.Rank is limited to the first 100 documents with highest similarity score.
The results are not inflated to match 100 results with 0 value if the query returns less than 100 results.If the query has no matches,a blank text file is returned.
The name of the first output text file is Query Output1.txt.Each output is stored in a seperate file in the format "Query Outputx.txt" where x is the number of the next output(for eg Query Output2.txt).

How to run it:
1.Unzip the archive.
2.Store all the documents in C:\Assignment_Files\
3.Compile and run EndAssignment.
4.The program is divided into Part1 and Part2.
5.At the end of Part1,the length of each document is generated as an output in the form of a text file with the name 'Length of document.txt'.It is stored in C:\Assignment_Files\ in the format 'document id:length'.
6.Part1 runs only once everytime the program is initially executed.
7.Part2 recieves a query from the user and outputs the first 100 documents based on the similarity score(higher the score, higher the rank).If there are less than 100 documents,the output is not inflated with 0 values to make it 100 documents.
8.If there is no match,the output is a blank document.
8.If you want to query the authors entire name in a document,start with a .A and type in the authors name the exact way it is in the document.(For eg, .Abrenckman,m.)
9.Authors individual word searches are permitted.( For eg, brenckman)
10.If you want to query the bibliography in a document,start with a .B and type in the bibliography the exact way it is in the document.(For eg, .Bj. ae. scs. 25, 1958, 324.)
11.All other queries are permitted provided there is a space between each word(Space is the splitting condition for normal queries).For eg, I am free falling. 
12.Every output is stored in a seperate text file with the name 'Query Outputx.txt' where x is the number of the output file.It is stored in C:\Assignment_Files\
13.Part2 runs indefinitely until the program is terminated.Enter '-1' in query to terminate the program.

Tested on Windows 10 allocating maximum memory 1400M and takes approximately 30 seconds to run Part1.