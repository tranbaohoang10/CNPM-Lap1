param(
    [string]$BaseUrl = 'http://localhost:8080/CineBook_war_exploded',
    [string]$Test = '',
    [switch]$Headless
)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $runArgs = @('test', "-Dcinebook.baseUrl=$BaseUrl", "-Dcinebook.headless=$($Headless.IsPresent.ToString().ToLowerInvariant())")
    if ($Test) { $runArgs += "-Dtest=$Test" }
    & .\mvnw.cmd @runArgs
    $runExitCode = $LASTEXITCODE
} finally { Pop-Location }
exit $runExitCode
