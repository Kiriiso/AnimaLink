# Smoke test script to verify microservices are working
Write-Host "Starting smoke tests..." -ForegroundColor Green

$baseUrl = "http://localhost"
$services = @(
    @{ name = "api-gateway"; port = 8080; healthUrl = "/api/health" },
    @{ name = "cliente_service"; port = 8083; healthUrl = "/clientes" },
    @{ name = "pets_service"; port = 8084; healthUrl = "/pets" },
    @{ name = "appointment_service"; port = 8085; healthUrl = "/appointments" },
    @{ name = "notifications_service"; port = 8088; healthUrl = "/health" }
)

$results = @()

foreach ($service in $services) {
    $url = "$baseUrl`:$($service.port)$($service.healthUrl)"
    Write-Host "Testing $($service.name) at $url..." -ForegroundColor Cyan
    
    try {
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
        $results += @{ service = $service.name; status = "OK"; code = $response.StatusCode }
        Write-Host "  ✓ Success (Status: $($response.StatusCode))" -ForegroundColor Green
    } catch {
        $results += @{ service = $service.name; status = "FAIL"; error = $_.Exception.Message }
        Write-Host "  ✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`nSmoke Test Results:" -ForegroundColor Yellow
$results | ForEach-Object { 
    $statusColor = if ($_.status -eq "OK") { "Green" } else { "Red" }
    Write-Host "  $($_.service): $($_.status)" -ForegroundColor $statusColor 
}

$okCount = ($results | Where-Object { $_.status -eq "OK" }).Count
Write-Host "`nPassed: $okCount/$($results.Count)" -ForegroundColor Cyan
