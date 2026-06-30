# Quick start script - runs 3 main services for smoke testing
Write-Host "Starting microservices for smoke testing..." -ForegroundColor Green

$basePath = "c:\Users\jimen\OneDrive\Escritorio\AnimaLink proyect"

$services = @(
    @{ 
        name = "cliente_service"
        jar = "$basePath\cliente_service\cliente_service\target\cliente_service-0.0.1-SNAPSHOT.jar"
        port = "8083"
    },
    @{ 
        name = "pets_service"
        jar = "$basePath\pets_service\pets_service\target\pets_service-0.0.1-SNAPSHOT.jar"
        port = "8084"
    },
    @{ 
        name = "appointment_service"
        jar = "$basePath\appointment_service\appointment_service\target\appointment_service-0.0.1-SNAPSHOT.jar"
        port = "8085"
    }
)

foreach ($service in $services) {
    if (Test-Path $service.jar) {
        Write-Host "Starting $($service.name) on port $($service.port)..." -ForegroundColor Cyan
        Start-Process -FilePath "java" -ArgumentList "-jar", "`"$($service.jar)`"", "--server.port=$($service.port)" `
            -WindowStyle Minimized -PassThru | Out-Null
        Start-Sleep -Seconds 2
    } else {
        Write-Host "JAR not found: $($service.jar)" -ForegroundColor Red
    }
}

Write-Host "`nServices starting. Waiting 10 seconds for startup..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "`nRunning smoke tests..." -ForegroundColor Green

# Test cliente_service
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8083/clientes" -UseBasicParsing -TimeoutSec 5
    Write-Host "✓ cliente_service is UP" -ForegroundColor Green
} catch {
    Write-Host "✗ cliente_service is DOWN" -ForegroundColor Red
}

# Test pets_service
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/pets" -UseBasicParsing -TimeoutSec 5
    Write-Host "✓ pets_service is UP" -ForegroundColor Green
} catch {
    Write-Host "✗ pets_service is DOWN" -ForegroundColor Red
}

# Test appointment_service
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8085/appointments" -UseBasicParsing -TimeoutSec 5
    Write-Host "✓ appointment_service is UP" -ForegroundColor Green
} catch {
    Write-Host "✗ appointment_service is DOWN" -ForegroundColor Red
}

Write-Host "`nSmoke tests complete. Services running in background." -ForegroundColor Green
Write-Host "To stop, kill the Java processes or use Task Manager" -ForegroundColor Yellow
