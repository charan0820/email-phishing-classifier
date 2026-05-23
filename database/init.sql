CREATE DATABASE IF NOT EXISTS phishing_classifier;
USE phishing_classifier;

CREATE TABLE IF NOT EXISTS emails (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject TEXT,
    body LONGTEXT,
    sender VARCHAR(255),
    recipient VARCHAR(255),
    is_phishing BOOLEAN,
    confidence_score DOUBLE,
    features TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS training_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email_text LONGTEXT,
    is_phishing BOOLEAN,
    used_for_training BOOLEAN DEFAULT FALSE
);

-- Insert sample training data
INSERT INTO training_data (email_text, is_phishing) VALUES
('Urgent: Your account will be suspended! Click here to verify: http://fake-bank.com', TRUE),
('Security Alert: Unusual login detected from new device', FALSE),
('You won $1,000,000! Claim your prize now: http://lottery-scam.com', TRUE),
('Meeting reminder for tomorrow at 2 PM in conference room', FALSE),
('Update your payment information to avoid service interruption', TRUE),
('Your monthly statement is ready for review', FALSE);