@echo off
echo 🧪 Google OAuth2 API Testing for Postman
echo =========================================

set BASE_URL=http://localhost:8080

echo.
echo 📋 Updated OAuth2 Credentials:
echo ===============================
echo Client ID: 879169140311-sa4qr57lch24bt9lcpeqokp410vks92s.apps.googleusercontent.com
echo Client Secret: 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
echo Gmail: harshithreddy010704@gmail.com
echo.

echo 📋 Postman Test Steps:
echo ======================
echo.
echo 1️⃣ Test OAuth2 Login URL:
echo GET %BASE_URL%/api/auth/oauth2/login
echo.
echo 2️⃣ Start OAuth2 Flow:
echo GET %BASE_URL%/oauth2/authorization/google
echo.
echo 3️⃣ Complete in Browser:
echo - Copy the URL from step 2
echo - Paste in browser
echo - Sign in with Google account: harshithreddy010704@gmail.com
echo - Grant permissions
echo.
echo 4️⃣ Check Success:
echo GET %BASE_URL%/api/auth/oauth2-success
echo.
echo 5️⃣ View All OAuth2 Users:
echo GET %BASE_URL%/api/auth/oauth2/users
echo.
echo 6️⃣ Get Specific User:
echo GET %BASE_URL%/api/auth/oauth2/users/harshithreddy010704@gmail.com
echo.
echo 7️⃣ Delete User (Optional):
echo DELETE %BASE_URL%/api/auth/oauth2/users/harshithreddy010704@gmail.com
echo.
echo ✅ Ready for Postman Testing!
echo.
echo 🎯 Expected Results:
echo - User created in oauth2_users table
echo - Google profile data extracted (name, email, picture)
echo - Login type: GOOGLE_OAUTH2
echo - Session created for frontend
echo.
echo 🔗 For React Frontend:
echo Update your Google login button to redirect to:
echo %BASE_URL%/oauth2/authorization/google
pause 