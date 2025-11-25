-- Create database
CREATE DATABASE IF NOT EXISTS medicine_tracker;
USE medicine_tracker;

-- ========================
--   MEDICINE TABLE
-- ========================
CREATE TABLE IF NOT EXISTS medicine (
  medicine_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  manufacturer VARCHAR(200),
  supplier VARCHAR(200)
);

-- ========================
--   SLOT TABLE
-- ========================
CREATE TABLE IF NOT EXISTS slot (
  slot_id INT AUTO_INCREMENT PRIMARY KEY,
  zone VARCHAR(50) NOT NULL,               -- A, B, C etc.
  shelf_number VARCHAR(50) NOT NULL,       -- A-1, B-3, etc.
  composition_tag VARCHAR(200) DEFAULT NULL,
  capacity INT NOT NULL DEFAULT 100,
  current_quantity INT NOT NULL DEFAULT 0,
  UNIQUE(zone, shelf_number)
);

-- ========================
--   BATCH TABLE
-- ========================
CREATE TABLE IF NOT EXISTS batch (
  batch_id INT AUTO_INCREMENT PRIMARY KEY,
  medicine_id INT NOT NULL,
  composition VARCHAR(200),
  quantity INT NOT NULL,
  mfg_date DATE,
  exp_date DATE,
  slot_id INT NULL,

  FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON DELETE CASCADE,
  FOREIGN KEY (slot_id) REFERENCES slot(slot_id) ON DELETE SET NULL
);

-- ========================
--   ALERT TABLE
-- ========================
CREATE TABLE IF NOT EXISTS alerts (
  alert_id INT AUTO_INCREMENT PRIMARY KEY,
  batch_id INT NULL,
  message VARCHAR(1000),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (batch_id) REFERENCES batch(batch_id) ON DELETE SET NULL
);
