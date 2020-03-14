Compile: javac *.java
Run: java PrevYearAnalytics
  * ccy.data, client.data, sales.data, and transaction.data need to be in the same directory
Output file: out.txt

Performance-wise, the program is O(nlogn) overall.
The program reads each of the input files once, and iterates through a set containing transaction data once in O(n) time complexity. When sorting the companies in alphabetical order, it uses the built-in Arrays.sort function, which is O(nlogn).
Every other data structure is a map, which is accessed in constant time.