# IFN645: Large Scale Data Mining – Capstone Project

This repository presents three data mining projects implemented in Java and Weka as part of the IFN645 unit at QUT. Each task addresses a unique data mining challenge: association rule mining, classification of infection risk, and news article text classification.

## 👥 Team Members
- Tuvshinbayar Ryenchindorj (11790300)
- Nathan Edwards (11657464)

---

## 📁 Files Included

```
├── Task 1 Java/
│   ├── T1_FrequentPatterns/src/Task1_FrequentPatterns.java
│   ├── T1_Max_Pattern/src/Top5_Max_Pattern.java
│   ├── T1_Pattern/src/Top5_Pattern.java
│   ├── T1_Rules/src/Top10_Rules.java
│
├── Task 2 Java/
│   ├── T2_Cost/src/CostSensitive.java
│   ├── T2_Filters/src/Filters.java
│
├── Task 3 Java/
│   └── src/Task3_Classification.java
│
├── IFN645_Report.pdf (⚠ GitHub upload limit – not included)
└── README.md
```

---

## 🔍 Project Summary

### 🧩 Task 1: Association Rule Mining
- Extracted frequent and maximal patterns using Apriori and FP-Growth.
- Developed separate modules for:
  - `Top5_Pattern.java`: Generates frequent itemsets
  - `Top5_Max_Pattern.java`: Filters for maximal patterns
  - `Top10_Rules.java`: Derives top rules from selected thresholds

### 🩺 Task 2: COVID-19 Infection Risk Classification
- Implemented Weka-based classification logic in Java.
- `Filters.java`: Applies attribute selection with Gain Ratio
- `CostSensitive.java`: Runs cost-sensitive classifiers (J48, NB, PART)

### 📰 Task 3: News Article Text Classification
- Preprocessed text using Weka filters (TF-IDF, tokeniser, stemmer)
- `Task3_Classification.java`: Classifies documents using J48, SMO, IBk

---

## 🧠 Skills Demonstrated

- Java-based data mining implementation
- Weka API integration
- Frequent pattern mining & rule generation
- Cost-sensitive model evaluation
- NLP preprocessing & text classification
- Feature engineering and attribute selection

---

📘 *This project was developed as a part of QUT's IFN645 unit for educational purposes.*
