import java.io.*;
import java.util.*;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.tools.dataset_converter.TransactionDatabaseConverter;
import ca.pfv.spmf.tools.resultConverter.ResultConverter;

public class Top5_Max_Pattern {

    public static void main(String[] args) throws IOException {
        // Specify input ARFF files
        String input_file_yes = "C:/IFN645/assign2/dataset/bank_yes.arff";
        String input_file_no = "C:/IFN645/assign2/dataset/bank_no.arff";

        // Specify output files for the results
        String output_fp_yes = "C:/IFN645/assign2/output/Q3/bank_yes_fp.txt";
        String output_fp_no = "C:/IFN645/assign2/output/Q3/bank_no_fp.txt";

        // Specify files for the converted transaction datasets
        String input_converted_yes = "C:/IFN645/assign2/dataset/Q3/bank_yes_converted.txt";
        String input_converted_no = "C:/IFN645/assign2/dataset/Q3/bank_no_converted.txt";

        // Create an object of TransactionDatabaseConverter
        TransactionDatabaseConverter converter = new TransactionDatabaseConverter();

        // Convert the ARFF files to text files (transaction format)
        converter.convertARFFandReturnMap(input_file_yes, input_converted_yes, Integer.MAX_VALUE);
        converter.convertARFFandReturnMap(input_file_no, input_converted_no, Integer.MAX_VALUE);

        // Create an instance of the FP-Growth algorithm
        AlgoFPGrowth algo_FPGrowth = new AlgoFPGrowth();

        // Set minimum support
        double minsup = 0.1;

        // Run FP-Growth on the converted "bank_yes" dataset
        algo_FPGrowth.runAlgorithm(input_converted_yes, output_fp_yes, minsup);
        algo_FPGrowth.printStats();

        // Run FP-Growth on the converted "bank_no" dataset
        algo_FPGrowth.runAlgorithm(input_converted_no, output_fp_no, minsup);
        algo_FPGrowth.printStats();

        // Convert the results back to include the original item names if needed
        String final_output_fp_yes = "C:/IFN645/assign2/output/Q3/final_bank_yes_fp.txt";
        String final_output_fp_no = "C:/IFN645/assign2/output/Q3/final_bank_no_fp.txt";

        ResultConverter output_converter = new ResultConverter();
        output_converter.convert(input_converted_yes, output_fp_yes, final_output_fp_yes, null);
        output_converter.convert(input_converted_no, output_fp_no, final_output_fp_no, null);

        // Identify and print the top 5 maximum patterns for both datasets
        List<Pattern> maxPatternsYes = filterAndSortMaxPatterns(final_output_fp_yes);
        List<Pattern> maxPatternsNo = filterAndSortMaxPatterns(final_output_fp_no);

        // Output the top 5 patterns for the "Yes" class
        System.out.println("Top 5 Maximum Patterns for YES class:");
        printTopPatterns(maxPatternsYes, 5);

        // Output the top 5 patterns for the "No" class
        System.out.println("Top 5 Maximum Patterns for NO class:");
        printTopPatterns(maxPatternsNo, 5);
    }

    private static List<Pattern> filterAndSortMaxPatterns(String filePath) throws IOException {
        List<Pattern> patterns = readPatternsFromFile(filePath);

        // Filter out maximum patterns
        List<Pattern> maxPatterns = new ArrayList<>();
        for (Pattern p1 : patterns) {
            boolean isMax = true;
            for (Pattern p2 : patterns) {
                if (!p1.equals(p2) && p2.items.containsAll(p1.items)) {
                    isMax = false;
                    break;
                }
            }
            if (isMax) {
                maxPatterns.add(p1);
            }
        }

        // Sort the maximum patterns by support (descending)
        maxPatterns.sort((p1, p2) -> Integer.compare(p2.support, p1.support));

        return maxPatterns;
    }

    private static List<Pattern> readPatternsFromFile(String filePath) throws IOException {
        List<Pattern> patterns = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Pattern line format: "item1 item2 item3 #SUP: xxx"
                String[] parts = line.split("#SUP:");
                String[] items = parts[0].trim().split(" ");
                int support = Integer.parseInt(parts[1].trim());
                patterns.add(new Pattern(new HashSet<>(Arrays.asList(items)), support));
            }
        }
        return patterns;
    }

    private static void printTopPatterns(List<Pattern> patterns, int topN) {
        for (int i = 0; i < Math.min(topN, patterns.size()); i++) {
            System.out.println(patterns.get(i));
        }
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
