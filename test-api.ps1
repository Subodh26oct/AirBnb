#!/usr/bin/env pwsh

# Test Airbnb API Endpoints

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "Airbnb API Testing Script" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8081/api/v1"
$testUser = @{
    email = "subodh@example.com"
    password = "Test@1234"
    firstName = "Subodh"
    lastName = "Kumar"
}

# Test 1: Health Check
Write-Host "Test 1: Health Check (Swagger UI)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
    Write-Host "✅ Server is running - Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Server is not responding - $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Test 2: User Signup
Write-Host "Test 2: User Registration (Signup)" -ForegroundColor Yellow
try {
    $signupBody = $testUser | ConvertTo-Json
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/signup" `
        -Method POST `
        -ContentType "application/json" `
        -Body $signupBody `
        -UseBasicParsing `
        -TimeoutSec 5 `
        -ErrorAction Stop
    
    $responseData = $response.Content | ConvertFrom-Json
    Write-Host "✅ Signup successful - Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($responseData | ConvertTo-Json)" -ForegroundColor Gray
    $jwtToken = $responseData.data.accessToken
} catch {
    Write-Host "⚠️ Signup failed - $($_.Exception.Message)" -ForegroundColor Yellow
    if ($_.Exception.Response -ne $null) {
        $error = $_.Exception.Response.Content.ReadAsStringAsync().Result
        Write-Host "Error Details: $error" -ForegroundColor Gray
    }
}

Write-Host ""

# Test 3: User Login
Write-Host "Test 3: User Login" -ForegroundColor Yellow
try {
    $loginBody = @{
        email = $testUser.email
        password = $testUser.password
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody `
        -UseBasicParsing `
        -TimeoutSec 5 `
        -ErrorAction Stop
    
    $responseData = $response.Content | ConvertFrom-Json
    Write-Host "✅ Login successful - Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Token: $($responseData.data.accessToken.Substring(0,20))..." -ForegroundColor Gray
    $jwtToken = $responseData.data.accessToken
} catch {
    Write-Host "⚠️ Login failed - $($_.Exception.Message)" -ForegroundColor Yellow
}

Write-Host ""

# Test 4: Get User Profile
Write-Host "Test 4: Get User Profile (with JWT)" -ForegroundColor Yellow
if ($jwtToken) {
    try {
        $headers = @{
            Authorization = "Bearer $jwtToken"
            "Content-Type" = "application/json"
        }
        
        $response = Invoke-WebRequest -Uri "$baseUrl/users/profile" `
            -Method GET `
            -Headers $headers `
            -UseBasicParsing `
            -TimeoutSec 5 `
            -ErrorAction Stop
        
        $profileData = $response.Content | ConvertFrom-Json
        Write-Host "✅ Profile retrieved - Status: $($response.StatusCode)" -ForegroundColor Green
        Write-Host "Profile: $($profileData | ConvertTo-Json)" -ForegroundColor Gray
    } catch {
        Write-Host "⚠️ Profile fetch failed - $($_.Exception.Message)" -ForegroundColor Yellow
    }
} else {
    Write-Host "⚠️ Skipping - No JWT token available" -ForegroundColor Yellow
}

Write-Host ""

# Test 5: Search Hotels (public endpoint)
Write-Host "Test 5: Search Hotels (Public)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/hotels/search?city=Delhi&checkIn=2026-04-01&checkOut=2026-04-05&guests=2" `
        -Method GET `
        -UseBasicParsing `
        -TimeoutSec 5 `
        -ErrorAction Stop
    
    $hotelsData = $response.Content | ConvertFrom-Json
    Write-Host "✅ Hotels search successful - Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Found: $($hotelsData.data.Count) hotels" -ForegroundColor Gray
} catch {
    Write-Host "⚠️ Hotels search - $($_.Exception.Message)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "Testing Complete!" -ForegroundColor Cyan
Write-Host "API Documentation: http://localhost:8081/api/v1/swagger-ui.html" -ForegroundColor Cyan
Write-Host "H2 Console: http://localhost:8081/h2-console" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
