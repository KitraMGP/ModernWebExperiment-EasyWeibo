@echo off
:: 切换到 bat 脚本所在目录
cd %~dp0
:: 检查 Caddy 是否存在
where caddy > nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo 系统上未安装 Caddy。
    echo 请先安装 Caddy 并添加到 PATH 环境变量中。
    pause
    exit
)
:: 启动 Caddy
caddy run
:: 结束
echo Caddy 已结束运行。
pause