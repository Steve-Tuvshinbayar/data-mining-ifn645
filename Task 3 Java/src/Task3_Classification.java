import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Task3_Classification {

    public void doFilteredClassification(Instances data) {
        try {
            // Set the class index of the dataset
            data.setClassIndex(1);
            
            // Create a StringToWordVector filter
            StringToWordVector swFilter = new StringToWordVector();
            
            //Specify range of attributes to act on. We want to work on the entire list of words
            swFilter.setAttributeIndices("first-last");
            
            // Configure the filter
            swFilter.setIDFTransform(true);
			swFilter.setTFTransform(true);
            swFilter.setOutputWordCounts(true);
            swFilter.setStopwordsHandler(new Rainbow());
            swFilter.setStemmer(new LovinsStemmer());
            swFilter.setTokenizer(new AlphabeticTokenizer());
            swFilter.setWordsToKeep(100);
            

            // Create classifiers
            IBk ibk = new IBk();
            SMO smo = new SMO();
            J48 j48 = new J48();
            HoeffdingTree hoeffdingTree = new HoeffdingTree();

            // Evaluate each classifier
            evaluateClassifier(data, swFilter, ibk, "IBk");
            evaluateClassifier(data, swFilter, smo, "SMO");
            evaluateClassifier(data, swFilter, j48, "J48");
            evaluateClassifier(data, swFilter, hoeffdingTree, "HoeffdingTree");
            
        } catch (Exception e) {
            System.out.println("Error in classification...");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void evaluateClassifier(Instances data, StringToWordVector swFilter, 
                                    weka.classifiers.Classifier classifier, String classifierName) {
        try {
            // Create a FilteredClassifier object
            FilteredClassifier filter_classifier = new FilteredClassifier();
            filter_classifier.setFilter(swFilter);
            filter_classifier.setClassifier(classifier);

            // Measure time taken to build the classifier
            long startTime = System.currentTimeMillis();
            filter_classifier.buildClassifier(data);
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            // Evaluate the classifier
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(filter_classifier, data, 10, new Random(1));
            
            // Display the results
            System.out.println("===== " + classifierName + " =====");
            System.out.println("Time taken: " + timeTaken + " ms");
            System.out.println("Correctly Classified Instances: " + eval.correct() + "/" + data.numInstances());
            System.out.println("Accuracy: " + (eval.pctCorrect()) + "%");
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toClassDetailsString());
            System.out.println("===== Evaluation done =====\n");

        } catch (Exception e) {
            System.out.println("Error in evaluating " + classifierName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource("C:/IFN645/data/news.arff");
        Instances data = source.getDataSet();
        
        Task3_Classification tc = new Task3_Classification();
        tc.doFilteredClassification(data);
    }
}
