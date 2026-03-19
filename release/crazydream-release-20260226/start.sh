#!/bin/bash
# CrazyDream 应用启动脚本

# 设置Java环境变量（如果需要）
# export JAVA_HOME=/path/to/java
# export PATH=$JAVA_HOME/bin:$PATH

# 应用配置
APP_NAME="crazydream"
JAR_NAME="crazydream-0.0.1-SNAPSHOT.jar"
LOG_DIR="logs"
LOG_FILE="${LOG_DIR}/${APP_NAME}.log"
PID_FILE="${APP_NAME}.pid"

# 创建日志目录
mkdir -p ${LOG_DIR}

# 检查应用是否已经在运行
if [ -f ${PID_FILE} ]; then
    PID=$(cat ${PID_FILE})
    if ps -p ${PID} > /dev/null 2>&1; then
        echo "应用已经在运行，PID: ${PID}"
        exit 1
    else
        echo "删除过期的PID文件"
        rm -f ${PID_FILE}
    fi
fi

# 启动应用
echo "正在启动 ${APP_NAME}..."

# 从 .env 文件加载环境变量（如果存在）
if [ -f .env ]; then
    echo "加载环境变量配置..."
    export $(cat .env | grep -v '^#' | xargs)
fi

# 启动Java应用
nohup java -jar \
    -Xms512m \
    -Xmx1024m \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
    ${JAR_NAME} \
    > ${LOG_FILE} 2>&1 &

# 保存PID
echo $! > ${PID_FILE}

echo "应用启动成功！"
echo "PID: $(cat ${PID_FILE})"
echo "日志文件: ${LOG_FILE}"
echo ""
echo "查看日志: tail -f ${LOG_FILE}"
echo "停止应用: ./stop.sh"
