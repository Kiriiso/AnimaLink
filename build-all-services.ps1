# Script to build all 10 microservices
$services = @(
    @{ name = "auth_service"; path = "auth_service\auth_service" },
    @{ name = "users_service"; path = "users_service\users_service" },
    @{ name = "cliente_service"; path = "cliente_service\cliente_service" },
    @{ name = "pets_service"; path = "pets_service\pets_service" },
    @{ name = "appointment_service"; path = "appointment_service\appointment_service" },
    @{ name = "factura_service"; path = "factura_service\factura_service" },
    @{ name = "inventory_service"; path = "inventory_service\inventory_service" },
    @{ name = "notifications_service"; path = "notifications_service\notifications_service" },
    @{ name = "customer_service"; path = "customer_service\customer_service" },
    @{ name = "api-gateway"; path = "api-gateway\api-gateway" }
)

$basePath = "c:\Users\jimen\OneDrive\Escritorio\AnimaLink proyect"
$failedBuilds = @()

foreach ($service in $services) {
    $fullPath = Join-Path $basePath $service.path
    $mvnwPath = Join-Path $fullPath "mvnw.cmd"
    
    if (Test-Path $mvnwPath) {
        Write-Host "Building $($service.name)..."
        Push-Location $fullPath
        $output = & .\mvnw.cmd -q -DskipTests package 2>&1
        if ($LASTEXITCODE -ne 0) {
            $failedBuilds += $service.name
            Write-Host "FAILED: $($service.name)"
            Write-Host $output
        } else {
            Write-Host "SUCCESS: $($service.name)"
        }
        Pop-Location
    } else {
        Write-Host "mvnw not found for $($service.name)"
    }
}

if ($failedBuilds.Count -gt 0) {
    Write-Host "Failed builds: $($failedBuilds -join ', ')"
} else {
    Write-Host "All services built successfully!"
}
