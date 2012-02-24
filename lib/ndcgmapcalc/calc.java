package ndcgmapcalc;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class calc {
	
	HashMap<String, Integer> hm;
	
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	private calc(){
		hm = new HashMap<String, Integer>();
	}
	
	
	private void CreateHash(BufferedReader gr) throws IOException{
		int value;
		
		for (int i=0; i<10;i++){ // we are concerned with only first 10 results of google			
			if(i>4) 
				value=1;
			else
				value=6-i; // ranking order 6,5,4,3,2,1,1,1,1,1
			hm.put(gr.readLine(), value );			
		}
	}
	
	
	private float AveragePrecision(BufferedReader lr) throws IOException {
		float precision = 0;
		float num_relevant_matches=0;
		
		// calculates the average precision
		for (int i=0;i<10;i++){
			//System.out.println(lr.readLine());
			//String temp = lr.readLine().replaceAll(" ", "_");
			//System.out.println(temp);
			//if(hm.containsKey(temp))
			if(hm.containsKey(lr.readLine().replaceAll(" ", "_")))
			{
				num_relevant_matches++;
				//System.out.println( hm.get(temp));	
				precision = precision + ((float) num_relevant_matches)/(i+1);
				//System.out.println(precision);
			}			
		}	
		if (num_relevant_matches==0)
		{
		return 0;
		}
		return precision/num_relevant_matches;
	}
	
	
	private float NDCG_5(BufferedReader lr) throws IOException{
		float ndcg = 0;
		Integer value=0;
		
		for (int i=0;i<5;i++){
			//System.out.println(lr.readLine());
			
			value = hm.get(lr.readLine().replaceAll(" ", "_"));
			//System.out.println("\n In NDCG: "+value+" i "+i	);
			float log = 0;
			if (i==0 || i==1) // the if sequence helps in calculating the logarithm values
				log=1;
			else if (i==2)
				log=(float) 1.59;
			else if (i==3)
				log=2;
			else
				log=(float)2.32;
			
			if(value!=null){
				ndcg = ndcg + (((float)value)/log);
			}
		}	
		
		return ndcg;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		calc obj_calc = new calc();
		System.out.println("Enter the number of test queries run:"); // should be 30 for milestone 2
		Scanner scanner = new Scanner(System.in);
		
		int queries = scanner.nextInt();		
		float average_precision=0, ndcg=0;
		//int i =28;
		// check the average precision and NDCG @ 5 value for all files
		for(int i=1;i<=queries;i++){
			try {
				FileReader lucene_fr = new FileReader("/home/gautham/informationretrieval/javaprac/milestone1/src/done/lucene"+(i)+".txt");
				FileReader google_fr = new FileReader("/home/gautham/informationretrieval/javaprac/milestone1/src/done/googleout"+(i-1));
				
				BufferedReader lucene_reader = new BufferedReader(lucene_fr);
				BufferedReader google_reader = new BufferedReader(google_fr);
				lucene_reader.mark(10);
				google_reader.mark(10);
				/*
				for (int ii= 0 ;ii <10; ii++)
				{
				System.out.print(lucene_reader.readLine()+"\n");
				}
				for (int jj=0; jj<10; jj++){
				System.out.print(google_reader.readLine()+"\n");
				} */
				lucene_reader.reset();
				google_reader.reset();
											
				obj_calc.CreateHash(google_reader);
				
				
				google_reader.reset();
				
				//System.out.print(obj_calc.hm.entrySet().toString());
				//System.out.println(lucene_reader.readLine()+"gautham");
				average_precision=average_precision+obj_calc.AveragePrecision(lucene_reader);
				lucene_reader.reset();
				
				//System.out.println(obj_calc.NDCG_5(lucene_reader));
				ndcg=ndcg+(obj_calc.NDCG_5(lucene_reader)/obj_calc.NDCG_5(google_reader));
				//System.out.println(obj_calc.NDCG_5(google_reader));
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		float mean_average_precision=average_precision/queries;
		float mean_ndcg=ndcg/queries; 
		
		System.out.println("mean_average_precision: "+mean_average_precision+"\n");
		System.out.println("mean_ndcg: "+mean_ndcg+"\n");
		
	}

}
