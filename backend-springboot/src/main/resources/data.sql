-- Sample Medicines Data
INSERT INTO medicines (medicine_name, type, manufacturer, description, price_per_unit, stock_quantity, reorder_level) VALUES
('Paracetamol 500mg', 'Tablet', 'PharmaCorp', 'Pain reliever and fever reducer', 2.50, 1000, 100),
('Amoxicillin 250mg', 'Capsule', 'MediLife', 'Antibiotic for bacterial infections', 15.00, 500, 50),
('Ibuprofen 400mg', 'Tablet', 'HealthPlus', 'Anti-inflammatory pain reliever', 5.00, 750, 75),
('Cetirizine 10mg', 'Tablet', 'AllergyFree', 'Antihistamine for allergies', 3.00, 600, 60),
('Omeprazole 20mg', 'Capsule', 'DigestCare', 'Proton pump inhibitor', 8.00, 400, 40);

-- Sample Slots Data
INSERT INTO slots (slot_number, location, capacity, current_occupancy, is_available) VALUES
('A-01', 'Warehouse A - Shelf 1', 100, 50, TRUE),
('A-02', 'Warehouse A - Shelf 2', 100, 75, TRUE),
('B-01', 'Warehouse B - Shelf 1', 150, 100, TRUE),
('B-02', 'Warehouse B - Shelf 2', 150, 0, TRUE),
('C-01', 'Cold Storage - Shelf 1', 80, 60, TRUE);

-- Sample Batches Data
INSERT INTO batches (batch_number, medicine_id, slot_id, manufacturing_date, expiry_date, quantity, cost_price, selling_price) VALUES
('PARA-2024-001', 1, 1, '2024-01-15', '2026-01-15', 500, 1.50, 2.50),
('PARA-2024-002', 1, 1, '2024-06-01', '2026-06-01', 500, 1.50, 2.50),
('AMOX-2024-001', 2, 2, '2024-03-10', '2025-03-10', 300, 10.00, 15.00),
('AMOX-2024-002', 2, 2, '2024-08-20', '2025-08-20', 200, 10.00, 15.00),
('IBUP-2024-001', 3, 3, '2024-02-05', '2026-02-05', 400, 3.00, 5.00),
('IBUP-2024-002', 3, 3, '2024-09-15', '2026-09-15', 350, 3.00, 5.00),
('CETI-2024-001', 4, 4, '2024-04-12', '2026-04-12', 300, 2.00, 3.00),
('CETI-2024-002', 4, 4, '2024-11-01', '2026-11-01', 300, 2.00, 3.00),
('OMEP-2024-001', 5, 5, '2024-05-20', '2025-05-20', 250, 5.00, 8.00),
('OMEP-2024-002', 5, 5, '2024-10-10', '2025-10-10', 150, 5.00, 8.00);
