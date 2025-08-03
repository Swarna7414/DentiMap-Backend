@echo off
echo 🔐 Testing OAuth2 API Endpoints
echo ==================================

set BASE_URL=http://localhost:8080

echo.
echo 1️⃣ Getting OAuth2 Login URLs...
curl -X GET "%BASE_URL%/api/auth/oauth2/login" -H "Content-Type: application/json"

echo.
echo 2️⃣ Testing OAuth2 Success Handler...
curl -X GET "%BASE_URL%/api/auth/oauth2-success" -H "Content-Type: application/json"

echo.
echo 3️⃣ Testing OAuth2 Failure Handler...
curl -X GET "%BASE_URL%/api/auth/oauth2-failure" -H "Content-Type: application/json"

echo.
echo 4️⃣ Google OAuth2 Authorization URL:
echo Open this URL in your browser to start OAuth2 flow:
echo %BASE_URL%/oauth2/authorization/google

echo.
echo ✅ OAuth2 API Testing Complete!
echo.
echo 📝 Next Steps:
echo 1. Open http://localhost:8080/oauth2/authorization/google in your browser
echo 2. Complete the Google OAuth2 flow
echo 3. Check the success/failure responses
pause 