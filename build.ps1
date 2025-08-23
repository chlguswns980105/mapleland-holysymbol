# JDK 경로 (bin까지 쓰지 말고 루트까지만)
$env:JAVA_HOME = "D:\jdk-17.0.0.1"

# 프로젝트 루트로 이동
Set-Location "C:\Users\qorrh\IdeaProjects\mapleland-holysymbol"

# 기존 빌드 삭제 후 shadowJar 빌드
.\gradlew clean shadowJar

# 빌드된 JAR
$sourceJar = "build\libs\timer-1.0-SNAPSHOT.jar"

# 최종 배포 폴더
$destFolder = "D:\timer"
$destJar = Join-Path $destFolder "timer.jar"

# 배포 폴더가 없으면 생성
if (-not (Test-Path $destFolder)) {
    New-Item -ItemType Directory -Path $destFolder | Out-Null
}

# 기존 JAR 삭제
if (Test-Path $destJar) {
    Remove-Item $destJar -Force
}

# 이름 변경하며 복사
Copy-Item $sourceJar -Destination $destJar
