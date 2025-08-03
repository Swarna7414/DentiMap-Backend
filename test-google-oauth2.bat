@echo off
echo 🔐 Google OAuth2 User Registration Test
echo ========================================

set BASE_URL=http://localhost:8080

echo.
echo 1️⃣ Getting OAuth2 Login URL...
curl -X GET "%BASE_URL%/api/auth/oauth2/login" -H "Content-Type: application/json"

echo.
echo 2️⃣ Google OAuth2 Authorization URL:
echo ======================================
echo Open this URL in your browser to register with Google:
echo %BASE_URL%/oauth2/authorization/google
echo.
echo This will:
echo - Redirect to Google's OAuth2 consent screen
echo - Ask you to sign in with your Google account
echo - Request permissions for your application
echo - Create a new OAuth2 user in the database
echo.

echo 3️⃣ After completing OAuth2 flow, test success handler:
echo GET %BASE_URL%/api/auth/oauth2-success

echo.
echo 4️⃣ Check all OAuth2 users:
echo GET %BASE_URL%/api/auth/oauth2/users

echo.
echo ✅ Google OAuth2 Testing Instructions Complete!
echo.
echo 📝 Step-by-Step Process:
echo 1. Copy the OAuth2 URL above
echo 2. Paste it in your browser
echo 3. Sign in with your Google account
echo 4. Grant permissions
echo 5. Check the success response
echo 6. Verify user was created in oauth2_users table
pause 