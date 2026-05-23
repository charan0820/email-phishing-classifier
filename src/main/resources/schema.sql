-- Create emails table
CREATE TABLE IF NOT EXISTS emails (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject CLOB,
    body CLOB,
    sender VARCHAR(255),
    recipient VARCHAR(255),
    is_phishing BOOLEAN,
    confidence_score DOUBLE,
    features CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create training_data table
CREATE TABLE IF NOT EXISTS training_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email_text CLOB,
    is_phishing BOOLEAN,
    used_for_training BOOLEAN DEFAULT FALSE
);

-- NEW: Create table for phishing keywords
CREATE TABLE IF NOT EXISTS phishing_keywords (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(100) NOT NULL UNIQUE,
    weight DOUBLE DEFAULT 1.0,
    category VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- NEW: Create table for phishing domains patterns
CREATE TABLE IF NOT EXISTS phishing_domains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    domain_pattern VARCHAR(100) NOT NULL UNIQUE,
    weight DOUBLE DEFAULT 1.0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- NEW: Create table for classification rules
CREATE TABLE IF NOT EXISTS classification_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL UNIQUE,
    rule_value DOUBLE NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);