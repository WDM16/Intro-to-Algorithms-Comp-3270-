import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.BufferedReader;
import java.util.Random;

/**
 * MaximumSumContiguousSubvector
 * 
 * @author willmay (wdm0032)
 * 
 * @description Computes the sum of the subsequence of numbers in an array
 * of numbers that sum to the largest value possible.
 *
 */
public class MaximumSumContiguousSubvector {
	
	public static int[] randArray1 = new int[10];
	public static int[] randArray2 = new int[15];
	public static int[] randArray3 = new int[20];
	public static int[] randArray4 = new int[25];
	public static int[] randArray5 = new int[30];
	public static int[] randArray6 = new int[35];
	public static int[] randArray7 = new int[40];
	public static int[] randArray8 = new int[45];
	public static int[] randArray9 = new int[50];
	public static int[] randArray10 = new int[55];
	public static int[] randArray11 = new int[60];
	public static int[] randArray12 = new int[65];
	public static int[] randArray13 = new int[70];
	public static int[] randArray14 = new int[75];
	public static int[] randArray15 = new int[80];
	public static int[] randArray16 = new int[85];
	public static int[] randArray17 = new int[90];
	public static int[] randArray18 = new int[95];
	public static int[] randArray19 = new int[100];
	
	public static int[][] matrix = new int[19][8];
                    
/**
 * Main method that will run the program.
 * @param args
 */
	public static void main(String[] args) {
		int[] X = readFile();
		
		int max = 0; 
		max = algorithm1(X);
		System.out.println("algorithm-1: " + max + "\n");
		max = algorithm2(X);
		System.out.println("algorithm-2: " + max + "\n");
		max = maxSum(X, 0, 9);
		System.out.println("algorithm-3: " + max + "\n");
		max = algorithm4(X);
		System.out.println("algorithm-4: " + max + "\n");
		
		randomArrayHandler();
		outputToFile();

	}
/**
 * readFile method will read the file and inputs the values into an array
 * 
 * http://www.java2s.com/Code/Java/File-Input-Output/Readeachlineinacommaseparatedfileintoanarray.htm
 * https://stackoverflow.com/questions/18838781/converting-string-array-to-an-integer-array
 */
	public static int[] readFile() {
		String[] strFileInput = new String[10];
		int[] intFileInp = new int[10];
		try {
			BufferedReader br = new BufferedReader(new FileReader("phw_input.txt"));
			String inputln = null;
			
			while((inputln = br.readLine()) != null)
				strFileInput = inputln.split(",");
				
			for(int i = 0; i < strFileInput.length; i++)
				intFileInp[i] = Integer.parseInt(strFileInput[i]);
			
			br.close();
		}
		catch(IOException e) {
			System.out.println(e); 
		}
		return intFileInp;
		}
	
/**
 * Algorithm-1 that was provided in instruction document
 * @param X 
 * @return maxSoFar 
 */
	public static int algorithm1(int X[]) {
		int maxSoFar = 0;
		int P = 0;
		int Q = X.length;
		
		for(int L = P; L < Q; L++) {
			for(int U = L; U < Q; U++) {
				int sum = 0; 
				for (int I = L; I <= U; I++) {
					sum = sum + X[I];
				}
				maxSoFar = Math.max(maxSoFar, sum);
			}
		}
		return maxSoFar;
	}
	
/**
 * Algorithm-2 that was provided in instruction document
 * @param X 
 * @return maxSoFar 
 */
	public static int algorithm2(int X[]) {
		int maxSoFar = 0, P = 0;
		int Q = X.length;
		
		for(int L = P; L < Q; L++) {
			int sum = 0; 
			for(int U = L ; U < Q; U++) {
				sum = sum + X[U]; 
				maxSoFar = Math.max(maxSoFar, sum);
			}
		}
		return maxSoFar;
		
	}
	
/**
 * maxSum that was provided in instruction document
 * @param X 
 * @return maxSoFar 
 */
	public static int maxSum(int X[], int L, int U) {
		if (L > U) 
			return 0;
		if (L == U)
			return Math.max(0, X[L]);
		
		int M = (L + U)/2;
		int sum = 0, maxToLeft = 0;
		
		for(int I = M; I >= L; I--) {
			sum = sum + X[I];
			maxToLeft = Math.max(maxToLeft, sum); 
		}
		
		sum = 0;
		int maxToRight = 0; 
		
		for(int I = M+1; I <= U; I++) {
			sum = sum + X[I]; 
			maxToRight = Math.max(maxToRight, sum);
		}
		
		int maxCrossing = maxToLeft + maxToRight;
		
		int maxInA = maxSum(X, L, M);
		int maxInB = maxSum(X, M+1, U); 
		
		return Math.max(maxCrossing, Math.max(maxInA, maxInB));
	}
	
/**
 * Algorithm-4 that was provided in instruction document
 * @param X
 * @return maxSoFar 
 */
	public static int algorithm4(int X[]) {
		int maxSoFar = 0;
		int maxEndingHere = 0;
		int P = 0; 
		int Q = X.length;
		
		for(int I = P; I < Q; I++) {
			maxEndingHere = Math.max(0, maxEndingHere + X[I]);
			maxSoFar = Math.max(maxSoFar, maxEndingHere);
		}
		return maxSoFar;
	}
	
/**
 * popArray method will populate random integers ranging from -100 to 100.
 * @param arrayLength
 * @return tempArray
 * https://www.tutorialspoint.com/generate-a-random-array-of-integers-in-java#:~:text=In%20order%20to%20generate%20random,this%20random%20number%20generator%20sequence.
 */
	private static int[] popArray(int arrayLength) { 
		Random rand = new Random();	
		int[] tempArray = new int[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			tempArray[i] = rand.nextInt(200) - 100;
			
		}
		return tempArray;

	}

/**
 * measureTime method measures the average run time for each algorithm on the 19 arrays.
 * This method will also measure T(n) for each algorithm as well.
 * @param tempArray
 * https://www.programiz.com/java-programming/examples/calculate-methods-execution-time
 */
	public static void measureRunTime(int[] tempArray, int row) {
		long start = 0; 
		long end = 0;
		long execution = 0;
		
		int avgTimeAlg1 = 0;
		int avgTimeAlg2 = 0;
		int avgTimeAlg3 = 0;
		int avgTimeAlg4 = 0; 
		
		double tn_Alg1 = 0;
		double tn_Alg2 = 0;
		double tn_Alg3 = 0;
		double tn_Alg4 = 0;
		
		int arrayLength = tempArray.length;
		
		for(int i = 0; i < 500; i++) {
			
			start = System.nanoTime();
			int max = algorithm1(tempArray);
			end = System.nanoTime(); 
			
			execution = end - start; 
			avgTimeAlg1 += execution; 
			System.out.println(avgTimeAlg1); 
			
			start = System.nanoTime();
			max = algorithm2(tempArray);
			end = System.nanoTime();
			
			execution = end - start;
			avgTimeAlg2 += execution;
			
			start = System.nanoTime();
			max = maxSum(tempArray, 0, arrayLength - 1); 
			end = System.nanoTime();
			
			execution = end - start; 
			avgTimeAlg3 += execution; 
			
			start = System.nanoTime();
			max = algorithm4(tempArray);
			end = System.nanoTime();
			
			execution = end - start; 
			avgTimeAlg4 += execution; 
		}
		avgTimeAlg1 = avgTimeAlg1 / 500;
		avgTimeAlg2 = avgTimeAlg2 / 500;
		avgTimeAlg3 = avgTimeAlg3 / 500;
		avgTimeAlg4 = avgTimeAlg4 / 500;

		tn_Alg1 = calculateTC(arrayLength, 1);
		tn_Alg2 = calculateTC(arrayLength, 2);
		tn_Alg3 = calculateTC(arrayLength, 3);
		tn_Alg4 = calculateTC(arrayLength, 4);
		
		int[] arrayOfTime = {avgTimeAlg1, avgTimeAlg2, avgTimeAlg3, avgTimeAlg4, (int) tn_Alg1, (int) tn_Alg2, (int) tn_Alg3, (int) tn_Alg4};
		
		for(int j = 0; j < 8; j++) {
			matrix[row][j] = arrayOfTime[j];
		}
	
	}
	
/**
 * calculateTC calculates the time complexity.
 * @param n
 * @param algorithmNum
 * @return tc
 * https://www.geeksforgeeks.org/math-pow-method-in-java-with-example/
 */
	private static double calculateTC(int n, int algorithmNum) {
		double tc = 0; 
		switch(algorithmNum) {
		case 1:
			tc = ((7/2) * Math.pow(n, 3)) + (3 * Math.pow(n, 2)) + ((33/2) * n) + 3;
			break;
		case 2:
			tc = Math.pow(n, 2) + ((17/2) * n) + 4;
			break;
		case 3:
			tc = (12 * n) * (Math.log(2)/Math.log(n)) + (12 * n);
			break;
		case 4:
			tc = (13 * n) + 5;
			break;
		}
		return tc;
	
	}
	
/**
 *  randomArrayHandler method will fill all of the global variable arrays with random integers
 *  that contain negative, zero, and positive integers that will also measure
 *  the time complexity of each populated array
 */
	public static void randomArrayHandler() {
		randArray1 = popArray(10);
		randArray2 = popArray(15);
		randArray3 = popArray(20);
		randArray4 = popArray(25);
		randArray5 = popArray(30);
		randArray6 = popArray(35);
		randArray7 = popArray(40);
		randArray8 = popArray(45);
		randArray9 = popArray(50);
		randArray10 = popArray(55);
		randArray11 = popArray(60);
		randArray12 = popArray(65);
		randArray13 = popArray(70);
		randArray14 = popArray(75);
		randArray15 = popArray(80);
		randArray16 = popArray(85);
		randArray17 = popArray(90);
		randArray18 = popArray(95);
		randArray19 = popArray(100);
		
		measureRunTime(randArray1, 0);
		measureRunTime(randArray2, 1);
		measureRunTime(randArray3, 2);
		measureRunTime(randArray4, 3);
		measureRunTime(randArray5, 4);
		measureRunTime(randArray6, 5);
		measureRunTime(randArray7, 6);
		measureRunTime(randArray8, 7);
		measureRunTime(randArray9, 8);
		measureRunTime(randArray10, 9);
		measureRunTime(randArray11, 10);
		measureRunTime(randArray12, 11);
		measureRunTime(randArray13, 12);
		measureRunTime(randArray14, 13);
		measureRunTime(randArray15, 14);
		measureRunTime(randArray16, 15);
		measureRunTime(randArray17, 16);
		measureRunTime(randArray18, 17);
		measureRunTime(randArray19, 18);
	}
	
/**
 * outputToFile will print the contents out to a file.
 * https://www.w3schools.com/java/java_files_create.asp
 */
	public static void outputToFile() {
		
		try {
			File file = new File("WillMay_phw_output.txt");
			FileWriter fileWriter = new FileWriter("WillMay_phw_output.txt");
			
			fileWriter.write("algorithm-1,algorithm-2,algorithm-3,algorithm-4,T1(n),T2(n),T3(n),T4(n)\n");
			
			for(int i = 0; i < 19; i++) {
				for(int j = 0; j < 8; j++) {
					String value = Integer.toString(matrix[i][j]);
					fileWriter.write(value);
					if(j < 7) {
						fileWriter.write(", ");
					}	
				}
				fileWriter.write("\n");
			}
			fileWriter.close();
		}
		catch(IOException e) {
			System.out.println(e); 
		} 
	}	

}
