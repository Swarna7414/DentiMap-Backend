@echo off
echo 🚀 Testing User Registration
echo =============================

set BASE_URL=http://localhost:8080

echo.
echo 1️⃣ Testing Regular User Registration...
curl -X POST "%BASE_URL%/api/auth/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"User\",\"dateOfBirth\":\"1990-01-01\",\"gender\":\"Male\",\"email\":\"test.user@example.com\",\"password\":\"password123\",\"confirmPassword\":\"password123\"}"

echo.
echo 2️⃣ Testing OAuth2 Login URL...
curl -X GET "%BASE_URL%/api/auth/oauth2/login" -H "Content-Type: application/json"

echo.
echo 3️⃣ OAuth2 Authorization URL:
echo Open this URL in your browser to register with Google:
echo %BASE_URL%/oauth2/authorization/google

echo.
echo ✅ User Registration Testing Complete!
echo.
echo 📝 Next Steps:
echo 1. For regular users: Use the registration endpoint
echo 2. For OAuth2 users: Open the Google OAuth2 URL in browser
echo 3. Check users in database or via API endpoints
pause 