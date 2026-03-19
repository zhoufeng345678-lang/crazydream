#!/bin/bash
# CrazyDream 应用停止脚本

APP_NAME="crazydream"
PID_FILE="${APP_NAME}.pid"

# 检查PID文件是否存在
if [ ! -f ${PID_FILE} ]; then
    echo "应用未运行（找不到PID文件）"
    exit 1
fi

# 读取PID
PID=$(cat ${PID_FILE})

# 检查进程是否存在
if ! ps -p ${PID} > /dev/null 2>&1; then
    echo "应用未运行（PID: ${PID}）"
    rm -f ${PID_FILE}
    exit 1
fi

# 停止应用
echo "正在停止 ${APP_NAME}（PID: ${PID}）..."
kill ${PID}

# 等待进程结束
COUNT=0
while ps -p ${PID} > /dev/null 2>&1; do
    sleep 1
    COUNT=$((COUNT + 1))
    
    # 如果30秒后还未停止，强制杀死
    if [ ${COUNT} -eq 30 ]; then
        echo "应用未正常停止，强制终止..."
        kill -9 ${PID}
        break
    fi
done

# 删除PID文件
rm -f ${PID_FILE}

echo "应用已停止"
