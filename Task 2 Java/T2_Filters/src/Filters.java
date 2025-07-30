import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;

public class Filters {

    public static void main(String[] args) {
    	try {
        // Load data
        DataSource source = new DataSource("/Users/nathanedwards/Downloads/COVID19.arff");
        Instances data = source.getDataSet();

        // Setting class attribute (assuming it's the last attribute)
        data.setClass(data.attribute("infection_risk"));

        // Set up attribute selection
        GainRatioAttributeEval gain = new GainRatioAttributeEval();  // Use GainRatioAttributeEval
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(7);  // Select top 7 attributes

        // Define classifiers
        Classifier[] classifiers = {
            new NaiveBayes(),
            new OneR(),
            new PART(),
            new J48()
        };

        String[] classifierNames = { "NaiveBayes", "OneR", "PART", "J48" };

        // Train and evaluate each classifier
        for (int i = 0; i < classifiers.length; i++) {
            // Set up AttributeSelectedClassifier for each classifier
            AttributeSelectedClassifier attributeSelectedClassifier = new AttributeSelectedClassifier();
            attributeSelectedClassifier.setEvaluator(gain);
            attributeSelectedClassifier.setSearch(ranker);
            attributeSelectedClassifier.setClassifier(classifiers[i]);

            // Build classifier with attribute selection applied
            attributeSelectedClassifier.buildClassifier(data);

            // Evaluate classifier
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(attributeSelectedClassifier, data, 10, new Random(1));

            // Output the results
            System.out.println(classifierNames[i] + " Results:");
            System.out.println("Correctly classified instances: " + eval.correct());
            System.out.println("Accuracy: " + eval.pctCorrect() + "%");
            System.out.println("--------------------------------");
        }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
