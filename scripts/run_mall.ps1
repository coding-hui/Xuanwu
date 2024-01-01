# 步骤 1: 获取当前脚本运行的路径做为 ROOT_PATH
$rootPath = $pwd

# 步骤 2: 修改当前目录下配置文件 application-prod.yml
$configPath = Join-Path $rootPath "application-prod.yml"
(Get-Content $configPath) -replace "DATABASE_URL", "jdbc:h2:file:$rootPath/data" | Set-Content $configPath

# 步骤 3: 设置 Java 运行命令为当前目录下的 java/bin
$javaPath = Join-Path $rootPath "java\bin"
$env:PATH = "$javaPath;$env:PATH"

# 步骤 4: 设置 Java 启动参数 activate profile 为 application-prod.yml
$profileParam = "-Dspring.profiles.active=prod"

# 步骤 5: 设置 Jar 包路径为当前目录下的 lib/xuanwu-mall-0.9-SNAPSHOT.jar
$jarPath = Join-Path $rootPath "lib\xuanwu-mall-0.9-SNAPSHOT.jar"

# 步骤 6: 使用 java -jar 启动程序
Start-Process "java.exe" -ArgumentList ("-jar", $jarPath, $profileParam) -WorkingDirectory $rootPath -Wait
