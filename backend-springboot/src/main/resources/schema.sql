-- Create Medicines Table
CREATE TABLE IF NOT EXISTS medicines (
    medicine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(255) NOT NULL,
    description TEXT,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Slots Table
CREATE TABLE IF NOT EXISTS slots (
    slot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_number VARCHAR(50) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    current_occupancy INT NOT NULL DEFAULT 0,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Batches Table
CREATE TABLE IF NOT EXISTS batches (
    batch_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_number VARCHAR(100) NOT NULL UNIQUE,
    medicine_id BIGINT NOT NULL,
    slot_id BIGINT,
    manufacturing_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    quantity INT NOT NULL,
    cost_price DECIMAL(10, 2) NOT NULL,
    selling_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES slots(slot_id) ON DELETE SET NULL
);

-- Create indexes for better performance
CREATE INDEX idx_medicine_name ON medicines(medicine_name);
CREATE INDEX idx_batch_number ON batches(batch_number);
CREATE INDEX idx_batch_expiry ON batches(expiry_date);
CREATE INDEX idx_batch_medicine ON batches(medicine_id);
CREATE INDEX idx_slot_number ON slots(slot_number);
CREATE INDEX idx_slot_available ON slots(is_available);
