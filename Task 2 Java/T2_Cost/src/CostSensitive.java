import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.CostMatrix;

public class CostSensitive {

    public static void main(String[] args) throws Exception {
        // Load data
        DataSource source = new DataSource("/Users/nathanedwards/Downloads/COVID19.arff");
        Instances data = source.getDataSet();

        // Setting class attribute (assuming it's the last attribute)
        data.setClass(data.attribute("infection_risk"));

        // Set up attribute selection
        GainRatioAttributeEval gain = new GainRatioAttributeEval();  // GainRatioAttributeEval
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(7);  // Select top 7 attributes

        // Define classifiers (J48 and PART only, since we're focusing on cost-sensitive)
        Classifier[] classifiers = {
            new PART(),
            new J48()
        };

        String[] classifierNames = { "PART", "J48" };

        // Create cost matrix
        CostMatrix costMatrix = new CostMatrix(2);  // 2-class problem
        costMatrix.setElement(0, 1, 5.0);  // Predicted: low, Actual: high
        costMatrix.setElement(1, 0, 1.0);  // Predicted: high, Actual: low

        // Train and evaluate each classifier with attribute selection and cost-sensitive analysis
        for (int i = 0; i < classifiers.length; i++) {
            // Set up AttributeSelectedClassifier for each classifier
            AttributeSelectedClassifier attributeSelectedClassifier = new AttributeSelectedClassifier();
            attributeSelectedClassifier.setEvaluator(gain);
            attributeSelectedClassifier.setSearch(ranker);
            attributeSelectedClassifier.setClassifier(classifiers[i]);

            // Set up CostSensitiveClassifier with the cost matrix
            CostSensitiveClassifier costSensitiveClassifier = new CostSensitiveClassifier();
            costSensitiveClassifier.setClassifier(attributeSelectedClassifier);
            costSensitiveClassifier.setCostMatrix(costMatrix);
            costSensitiveClassifier.setMinimizeExpectedCost(true);  // Minimize cost

            // Build classifier
            costSensitiveClassifier.buildClassifier(data);

            // Evaluate classifier
            Evaluation eval = new Evaluation(data, costMatrix);
            eval.crossValidateModel(costSensitiveClassifier, data, 10, new Random(1));

            // Output the results
            System.out.println(classifierNames[i] + " Results:");
            System.out.println("Correctly classified instances: " + eval.correct());
            System.out.println("Accuracy: " + eval.pctCorrect() + "%");
            System.out.println("Total Cost: " + eval.totalCost());
            System.out.println("--------------------------------");
        }
    }
}
