USE medicine_tracker;

INSERT INTO batch (medicine_id, composition, quantity, mfg_date, exp_date, slot_id)
VALUES
(1, 'Paracetamol 500mg', 50, '2023-11-10', '2025-02-15', NULL),
(2, 'Dolo 650mg', 100, '2023-12-05', '2025-03-10', NULL),
(3, 'Azithromycin 500mg', 40, '2023-10-15', '2025-01-25', NULL),
(4, 'Vitamin C 500mg', 200, '2023-09-01', '2024-12-01', NULL),
(5, 'Ibuprofen 400mg', 120, '2024-01-10', '2025-01-10', NULL);
