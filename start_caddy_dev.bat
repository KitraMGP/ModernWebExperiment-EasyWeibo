@echo off
:: �л��� bat �ű�����Ŀ¼
cd %~dp0
:: ��� Caddy �Ƿ����
where caddy > nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ϵͳ��δ��װ Caddy��
    echo ���Ȱ�װ Caddy ����ӵ� PATH ���������С�
    pause
    exit
)
:: ���� Caddy
caddy run
:: ����
echo Caddy �ѽ������С�
pause