# Read .env file
$envFile = Join-Path $PSScriptRoot ".env"
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            [Environment]::SetEnvironmentVariable($name, $value, 'Process')
            Write-Host "Set $name environment variable"
        }
    }
    Write-Host "Environment variables loaded successfully"
} else {
    Write-Host "Error: .env file not found at $envFile"
    exit 1
} 