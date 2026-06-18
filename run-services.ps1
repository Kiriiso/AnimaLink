# Script to run all 10 microservices in parallel
$services = @(
    @{ name = "auth_service"; port = 8081; path = "auth_service\auth_service" },
    @{ name = "users_service"; port = 8082; path = "users_service\users_service" },
    @{ name = "cliente_service"; port = 8083; path = "cliente_service\cliente_service" },
    @{ name = "pets_service"; port = 8084; path = "pets_service\pets_service" },
    @{ name = "appointment_service"; port = 8085; path = "appointment_service\appointment_service" },
    @{ name = "factura_service"; port = 8086; path = "factura_service\factura_service" },
    @{ name = "inventory_service"; port = 8087; path = "inventory_service\inventory_service" },
    @{ name = "notifications_service"; port = 8088; path = "notifications_service\notifications_service" },
    @{ name = "customer_service"; port = 8089; path = "customer_service\customer_service" },
    @{ name = "api-gateway"; port = 8080; path = "api-gateway\api-gateway" }
)

$basePath = "c:\Users\jimen\OneDrive\Escritorio\AnimaLink proyect"

foreach ($service in $services) {
    $fullPath = Join-Path $basePath $service.path   
    $jar = "$fullPath\target\$($service.name)-0.0.1-SNAPSHOT.jar"
    
    if (Test-Path $jar) {
        Write-Host "Starting $($service.name) on port $($service.port)..."
        Start-Process -FilePath "java" -ArgumentList "-jar", $jar, "--server.port=$($service.port)" `
            -WorkingDirectory $fullPath -NoNewWindow
    } else {
        Write-Host "JAR not found for $($service.name) at $jar"
    }
}

Write-Host "All services started. Check logs for errors."
Start-Sleep -Seconds 5
