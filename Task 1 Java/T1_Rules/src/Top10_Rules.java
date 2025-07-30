import java.io.IOException;

import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.AlgoTopKClassRules;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.Database;
import ca.pfv.spmf.tools.resultConverter.ResultConverter;

public class Top10_Rules { 
	
	public void topK_classRules(String input_dataset, String output_path, double minconf, int top_k, int[] itemToBeUsedAsConsequent, String output_file_suffix) {
		//Specify output files with a suffix to differentiate between Yes and No class
		String output = output_path + "Top10_Rules_" + output_file_suffix + ".txt";
		String final_output = output_path + "Final_Top10_Rules_" + output_file_suffix + ".txt";
		
		// To generate rules, we need to create a database from the input dataset
		Database db = new Database(); 
		try {
			db.loadFile(input_dataset);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Create an object of rule mining algorithm
		AlgoTopKClassRules topK_classRules = new AlgoTopKClassRules(); 
		
		// Generate association rules 
		topK_classRules.runAlgorithm(top_k, minconf, db, itemToBeUsedAsConsequent);
		topK_classRules.printStats();
		try {
			topK_classRules.writeResultTofile(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Convert outputs to include the original names for the items
		ResultConverter output_converter = new ResultConverter();
		try {
			output_converter.convert(input_dataset, output, final_output, null);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
	    // Specify input dataset
	    String input_dataset = "C:/IFN645/assign2/dataset/bank_converted.txt";
	    
	    // Specify output files
	    String output_path = "C:/IFN645/assign2/output/";
	    
	    // Specify minimum confidence
	    double minconf = 0.8;
	    
	    // Top-k 
	    int top_k = 10;
	    
	    // Items to be used as consequent for generating rules
	    int[] itemToBeUsedAsConsequentYes = new int[] {42}; // For "Yes" class
	    int[] itemToBeUsedAsConsequentNo = new int[] {11};  // For "No" class
	    
	    Top10_Rules generateRules = new Top10_Rules();
	    
	    // Generate rules for the "Yes" class with a unique suffix
	    generateRules.topK_classRules(input_dataset, output_path, minconf, top_k, itemToBeUsedAsConsequentYes, "Yes");
	    
	    // Generate rules for the "No" class with a unique suffix
	    generateRules.topK_classRules(input_dataset, output_path, minconf, top_k, itemToBeUsedAsConsequentNo, "No");
	}
}

