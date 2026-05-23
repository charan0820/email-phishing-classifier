-- Insert sample training data
INSERT INTO training_data (email_text, is_phishing) VALUES
('Urgent: Your account will be suspended! Click here to verify: http://fake-bank.com', TRUE),
('Security Alert: Unusual login detected from new device', FALSE),
('You won $1,000,000! Claim your prize now: http://lottery-scam.com', TRUE),
('Meeting reminder for tomorrow at 2 PM in conference room', FALSE),
('Update your payment information to avoid service interruption', TRUE),
('Your monthly statement is ready for review', FALSE);

-- Insert sample analyzed emails
INSERT INTO emails (subject, body, sender, recipient, is_phishing, confidence_score, features) VALUES
('Account Verification Required', 'Dear user, we need to verify your account. Click here: http://verify-account.com', 'security@bank.com', 'user@example.com', TRUE, 0.85, '{"suspicious_keywords": 3, "has_urls": true, "suspicious_sender": true, "has_urgency": false, "grammar_errors": 1, "subject_length": 25, "body_length": 85}'),
('Meeting Update', 'Hi team, the meeting has been moved to 3 PM. Best regards, John', 'john@company.com', 'team@company.com', FALSE, 0.15, '{"suspicious_keywords": 0, "has_urls": false, "suspicious_sender": false, "has_urgency": false, "grammar_errors": 0, "subject_length": 14, "body_length": 65}');

-- NEW: Insert phishing keywords with weights
INSERT INTO phishing_keywords (keyword, weight, category) VALUES
('urgent', 1.0, 'urgency'),
('verify', 1.0, 'action'),
('password', 1.0, 'security'),
('account', 0.8, 'security'),
('suspended', 1.2, 'urgency'),
('winner', 1.5, 'scam'),
('prize', 1.5, 'scam'),
('click', 0.9, 'action'),
('login', 0.8, 'security'),
('confirm', 0.7, 'action'),
('update', 0.8, 'action'),
('security', 0.6, 'security'),
('alert', 0.7, 'urgency'),
('bank', 0.5, 'financial'),
('paypal', 0.5, 'financial'),
('immediately', 1.1, 'urgency'),
('dear', 0.3, 'greeting'),
('congratulations', 1.3, 'scam'),
('free', 1.0, 'scam'),
('limited', 0.9, 'urgency');

-- NEW: Insert phishing domain patterns
INSERT INTO phishing_domains (domain_pattern, weight) VALUES
('verify-', 1.0),
('security-', 1.0),
('account-', 1.0),
('login-', 1.0),
('update-', 1.0),
('confirm-', 1.0),
('bank-', 0.8),
('paypal-', 0.8),
('-alert', 0.7),
('-security', 0.7);

-- NEW: Insert classification rules and weights
INSERT INTO classification_rules (rule_name, rule_value, description) VALUES
('PHISHING_THRESHOLD', 0.6, 'Minimum confidence score to classify as phishing'),
('URL_WEIGHT', 0.3, 'Weight for URL presence'),
('KEYWORD_WEIGHT', 0.1, 'Base weight per suspicious keyword'),
('SENDER_WEIGHT', 0.2, 'Weight for suspicious sender'),
('URGENCY_WEIGHT', 0.15, 'Weight for urgency detection'),
('GRAMMAR_WEIGHT', 0.05, 'Weight per grammar error'),
('MAX_CONFIDENCE', 1.0, 'Maximum confidence score');