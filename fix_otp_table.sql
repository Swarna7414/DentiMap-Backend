-- Fix the otp_type column length in the otps table
ALTER TABLE otps MODIFY COLUMN otp_type VARCHAR(30) NOT NULL; 