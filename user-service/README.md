# Location Feature Setup

## Quick Setup

### 1. Database Setup
Open your MySQL client (MySQL Workbench, phpMyAdmin, or command line) and run:

```sql
-- Run this in MySQL client, NOT PowerShell
SOURCE complete_database_setup.sql;
SOURCE comprehensive_test_data.sql;
```

### 2. Alternative: Copy SQL Content
If you can't use SOURCE command, copy the content from:
- `complete_database_setup.sql` 
- `comprehensive_test_data.sql`

And paste directly into your MySQL client.

### 3. Verify Setup
```sql
-- Check if tables exist
SHOW TABLES;

-- Check test data
SELECT COUNT(*) as vets FROM vet_profiles WHERE clinic_latitude IS NOT NULL;
SELECT COUNT(*) as shelters FROM shelter_profiles WHERE shelter_latitude IS NOT NULL;
SELECT COUNT(*) as addresses FROM addresses WHERE latitude IS NOT NULL;
```

## Files
- `complete_database_setup.sql` - Creates all tables
- `comprehensive_test_data.sql` - Test data with 7 vets, 3 shelters
- `quick_location_test.sql` - Simple test data (3 vets)

## Test API
After setup, test these endpoints:
- `GET /v1/user/location/nearby/vets/coordinates?latitude=10.7769&longitude=106.7009&radiusKm=10`
- `GET /v1/user/location/debug/vets-with-location`
