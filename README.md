# Anti-Phishing Email Classifier (Java)

A Java-based machine learning project designed to detect and classify phishing emails using various email features and text analysis techniques.

---

## Overview

Phishing attacks are one of the most common cybersecurity threats today.  
This project focuses on building an intelligent email classifier that can identify whether an email is:

- ✅ Legitimate (Safe)
- ❌ Phishing (Malicious)

The classifier analyzes email content, links, sender information, and suspicious patterns to improve detection accuracy.

---

## Features

- Detect phishing emails using machine learning
- Analyze suspicious URLs and keywords
- Feature extraction from email content
- Email preprocessing and tokenization
- Classification using Java-based ML libraries
- Accuracy evaluation using test datasets
- Simple and modular project structure

---

## Technologies Used

- Java
- Java Collections Framework
- Apache OpenNLP / Weka (optional)
- File Handling
- Machine Learning Concepts
- NLP Basics

---

```bash
anti-phishing-email-classifier/
│
├── README.md
├── pom.xml
├── build-all.bat
├── run-backend.bat
├── run-frontend.bat
|
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── antiphishing/
│           │           └── backend/
│           │               ├── AntiPhishingApplication.java
│           │               ├── config/
│           │               │   └── WebSocketConfig.java
│           │               ├── controller/
│           │               │   └── PhishingController.java
│           │               ├── entity/
│           │               │   └── EmailAnalysis.java
│           │               ├── repository/
│           │               │   └── EmailAnalysisRepository.java
│           │               └── service/
│           │                   ├── FeatureExtractor.java
│           │                   └── PhishingDetectionService.java
│           └── resources/
│               ├── application.properties
│               └── schema.sql
│
└── frontend/
    ├── pom.xml
    └── src/
        └── main/
            └── webapp/
                ├── package.json
                ├── public/
                │   └── index.html
                └── src/
                    ├── App.js
                    ├── App.css
                    └── components/
                        ├── EmailAnalyzer.js
                        ├── Dashboard.js
                        └── AnalysisHistory.js
