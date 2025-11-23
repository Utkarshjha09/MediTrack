# Backend API Guide - Medicine Expiry Tracker

## 🚀 Quick Start

Your backend is a REST API running on **http://localhost:8080**

Start it by running: `backend-springboot/start-backend.bat`

---

## 📊 How Your Backend Works

### 1. **Medicines** (Main Product Database)
Stores information about medicine products (like a catalog).

**Data Structure:**
```json
{
  "medicineName": "Paracetamol",
  "type": "Tablet",
  "manufacturer": "ABC Pharma",
  "description": "Pain reliever and fever reducer",
  "pricePerUnit": 5.50,
  "stockQuantity": 500,
  "reorderLevel": 50
}
```

**API Endpoints:**
- `GET /api/medicines` - Get all medicines
- `POST /api/medicines` - Add new medicine
- `GET /api/medicines/{id}` - Get specific medicine
- `PUT /api/medicines/{id}` - Update medicine
- `DELETE /api/medicines/{id}` - Delete medicine

---

### 2. **Batches** (Stock with Expiry Dates)
Each batch represents a specific shipment of medicine with an expiry date.

**Data Structure:**
```json
{
  "batchNumber": "BATCH001",
  "medicineId": 1,
  "slotId": 5,
  "quantity": 100,
  "manufactureDate": "2025-01-15",
  "expiryDate": "2027-01-15",
  "receivedDate": "2025-02-01"
}
```

**Important:** Before creating a batch, you MUST:
1. Create a medicine first (to get `medicineId`)
2. Create a slot first (to get `slotId`)

**API Endpoints:**
- `GET /api/batches` - Get all batches
- `POST /api/batches` - Add new batch
- `GET /api/batches/expiring?date=2025-12-31` - Get expiring batches
- `GET /api/batches/medicine/{medicineId}` - Get all batches of a medicine

---

### 3. **Slots** (Storage Locations)
Physical locations where medicine is stored (shelf numbers, storage areas).

**Data Structure:**
```json
{
  "slotNumber": "A-101",
  "location": "Warehouse A",
  "capacity": 200,
  "currentLoad": 150,
  "isAvailable": true
}
```

**API Endpoints:**
- `GET /api/slots` - Get all slots
- `POST /api/slots` - Add new slot
- `GET /api/slots/available` - Get empty slots
- `GET /api/slots/location/{location}` - Get slots in a location

---

## 🔄 Workflow Example

### Step 1: Create a Storage Slot
```bash
curl -X POST http://localhost:8080/api/slots \
  -H "Content-Type: application/json" \
  -d '{
    "slotNumber": "A-101",
    "location": "Main Warehouse",
    "capacity": 500,
    "currentLoad": 0,
    "isAvailable": true
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Slot created successfully",
  "data": {
    "slotId": 1,
    "slotNumber": "A-101",
    ...
  }
}
```

### Step 2: Create a Medicine
```bash
curl -X POST http://localhost:8080/api/medicines \
  -H "Content-Type: application/json" \
  -d '{
    "medicineName": "Paracetamol 500mg",
    "type": "Tablet",
    "manufacturer": "ABC Pharma",
    "description": "Fever and pain relief",
    "pricePerUnit": 2.50,
    "stockQuantity": 0,
    "reorderLevel": 100
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Medicine created successfully",
  "data": {
    "medicineId": 1,
    "medicineName": "Paracetamol 500mg",
    ...
  }
}
```

### Step 3: Create a Batch (with medicineId=1 and slotId=1)
```bash
curl -X POST http://localhost:8080/api/batches \
  -H "Content-Type: application/json" \
  -d '{
    "batchNumber": "BATCH-2025-001",
    "medicineId": 1,
    "slotId": 1,
    "quantity": 500,
    "manufactureDate": "2025-01-15",
    "expiryDate": "2027-01-15",
    "receivedDate": "2025-02-20"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Batch created successfully",
  "data": {
    "batchId": 1,
    "batchNumber": "BATCH-2025-001",
    "medicineName": "Paracetamol 500mg",
    "slotNumber": "A-101",
    ...
  }
}
```

---

## 🎯 Frontend Integration

Your React frontend (`src/api.js`) has helper functions:

```javascript
import { medicineAPI, batchAPI, slotAPI } from './api';

// Create medicine
const response = await medicineAPI.create({
  medicineName: "Aspirin",
  type: "Tablet",
  manufacturer: "XYZ Corp",
  pricePerUnit: 3.00,
  stockQuantity: 200,
  reorderLevel: 50
});

// Get all medicines
const medicines = await medicineAPI.getAll();

// Create batch (after getting medicineId and slotId)
const batch = await batchAPI.create({
  batchNumber: "BATCH-002",
  medicineId: 1,
  slotId: 1,
  quantity: 300,
  manufactureDate: "2025-03-01",
  expiryDate: "2027-03-01",
  receivedDate: "2025-03-15"
});
```

---

## ⚠️ Common Errors

### 1. "Cannot create batch without medicine"
**Problem:** You're trying to create a batch but the medicine doesn't exist.

**Solution:** Create the medicine first, then use its `medicineId`.

### 2. "Cannot create batch without slot"
**Problem:** You're trying to create a batch but the storage slot doesn't exist.

**Solution:** Create a slot first, then use its `slotId`.

### 3. "Cannot connect to backend"
**Problem:** Backend server is not running.

**Solution:** Run `start-backend.bat` or check if port 8080 is in use.

---

## 🧪 Testing with Swagger UI

Open in browser: **http://localhost:8080/swagger-ui.html**

This gives you a visual interface to:
- See all API endpoints
- Test requests directly
- View request/response formats
- No coding needed!

---

## 📝 H2 Database Console

Open in browser: **http://localhost:8080/h2-console**

**Login:**
- JDBC URL: `jdbc:h2:mem:medicine_tracker_dev`
- Username: `sa`
- Password: (leave blank)

You can run SQL queries:
```sql
SELECT * FROM medicines;
SELECT * FROM batches;
SELECT * FROM slots;
```

---

## 🔍 Response Format

All API responses follow this structure:

**Success:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

**Error:**
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

---

## 💡 Tips

1. **Always create in order:** Slot → Medicine → Batch
2. **Check IDs:** Note the `medicineId` and `slotId` from responses
3. **Date format:** Use ISO format: `"2025-11-23"` (YYYY-MM-DD)
4. **Test in Swagger:** Use Swagger UI for quick testing before coding
5. **Database resets:** H2 in-memory DB resets when you restart the backend

---

## 🛠️ Quick Test Commands

**Windows PowerShell:**
```powershell
# Test backend is running
curl http://localhost:8080/api/medicines

# Create a slot
curl -Method POST -Uri "http://localhost:8080/api/slots" -ContentType "application/json" -Body '{"slotNumber":"A-1","location":"Main","capacity":100,"currentLoad":0,"isAvailable":true}'

# Get all slots
curl http://localhost:8080/api/slots
```

---

**Need help?** Open Swagger UI and try the "Try it out" button on any endpoint!
