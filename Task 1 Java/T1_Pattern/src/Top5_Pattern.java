import java.io.*;
import java.util.*;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.tools.dataset_converter.TransactionDatabaseConverter;
import ca.pfv.spmf.tools.resultConverter.ResultConverter;

public class Top5_Pattern {

    public static void main(String[] args) throws IOException {
        // Specify input ARFF files
        String input_file_yes = "C:/IFN645/assign2/dataset/bank_yes.arff";
        String input_file_no = "C:/IFN645/assign2/dataset/bank_no.arff";

        // Specify output files for the results
        String output_fp_yes = "C:/IFN645/assign2/output/bank_yes_fp.txt";
        String output_fp_no = "C:/IFN645/assign2/output/bank_no_fp.txt";

        // Specify files for the converted transaction datasets
        String input_converted_yes = "C:/IFN645/assign2/dataset/bank_yes_converted.txt";
        String input_converted_no = "C:/IFN645/assign2/dataset/bank_no_converted.txt";

        // Create an object of TransactionDatabaseConverter
        TransactionDatabaseConverter converter = new TransactionDatabaseConverter();

        // Convert the ARFF files to text files (transaction format)
        converter.convertARFFandReturnMap(input_file_yes, input_converted_yes, Integer.MAX_VALUE);
        converter.convertARFFandReturnMap(input_file_no, input_converted_no, Integer.MAX_VALUE);

        // Create an instance of the FP-Growth algorithm
        AlgoFPGrowth algo_FPGrowth = new AlgoFPGrowth();
        algo_FPGrowth.setMaximumPatternLength(3);  // Set maximum pattern length to 3 for size-3 patterns

        // Set minimum support
        double minsup = 0.4;

        // Run FP-Growth on the converted "bank_yes" dataset
        algo_FPGrowth.runAlgorithm(input_converted_yes, output_fp_yes, minsup);
        algo_FPGrowth.printStats();

        // Run FP-Growth on the converted "bank_no" dataset
        algo_FPGrowth.runAlgorithm(input_converted_no, output_fp_no, minsup);
        algo_FPGrowth.printStats();

        // Convert the results back to include the original item names if needed
        String final_output_fp_yes = "C:/IFN645/assign2/output/final_bank_yes_fp.txt";
        String final_output_fp_no = "C:/IFN645/assign2/output/final_bank_no_fp.txt";

        ResultConverter output_converter = new ResultConverter();
        output_converter.convert(input_converted_yes, output_fp_yes, final_output_fp_yes, null);
        output_converter.convert(input_converted_no, output_fp_no, final_output_fp_no, null);

        // Print the 5 most frequent size-3 patterns
        printTop5Patterns(final_output_fp_yes, "YES");
        printTop5Patterns(final_output_fp_no, "NO");
    }

    // Method to read patterns from a file and print the top 5 most frequent size-3 patterns
    private static void printTop5Patterns(String filePath, String className) throws IOException {
        List<Pattern> patterns = readPatternsFromFile(filePath);

        // Sort patterns by support in descending order
        patterns.sort((p1, p2) -> Integer.compare(p2.support, p1.support));

        // Print the top 5 patterns
        System.out.println("Top 5 most frequent size-3 patterns for " + className + " class:");
        for (int i = 0; i < Math.min(5, patterns.size()); i++) {
            System.out.println(patterns.get(i));
        }
        System.out.println();  // Add an extra line for better readability
    }

    // Method to read patterns from a file
    private static List<Pattern> readPatternsFromFile(String filePath) throws IOException {
        List<Pattern> patterns = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming pattern line format: "item1 item2 item3 #SUP: xxx"
                String[] parts = line.split("#SUP:");
                String[] items = parts[0].trim().split(" ");
                int support = Integer.parseInt(parts[1].trim());
                if (items.length == 3) {  // Only consider size-3 patterns
                    patterns.add(new Pattern(new HashSet<>(Arrays.asList(items)), support));
                }
            }
        }
        return patterns;
    }

    // Helper class to store patterns
    static class Pattern {
        Set<String> items;
        int support;

        Pattern(Set<String> items, int support) {
            this.items = items;
            this.support = support;
        }

        @Override
        public String toString() {
            return String.join(" ", items) + " #SUP: " + support;
        }
    }
}
