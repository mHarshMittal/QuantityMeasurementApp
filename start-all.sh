#!/bin/bash
# ============================================================
# Start all microservices in order
# Usage: ./start-all.sh
# ============================================================

set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "======================================================"
echo " Quantity Measurement - Microservices Startup"
echo "======================================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

LOG_DIR="$SCRIPT_DIR/logs"
mkdir -p "$LOG_DIR"

start_service() {
    local name=$1
    local dir=$2
    local port=$3
    local log="$LOG_DIR/${name}.log"

    echo -e "${CYAN}Starting ${name} on port ${port}...${NC}"
    cd "$SCRIPT_DIR/$dir"
    mvn spring-boot:run -q > "$log" 2>&1 &
    echo $! > "$LOG_DIR/${name}.pid"

    # Wait for service to be ready
    local retries=30
    local count=0
    while [ $count -lt $retries ]; do
        if curl -s "http://localhost:${port}/actuator/health" > /dev/null 2>&1 || \
           curl -s "http://localhost:${port}/" > /dev/null 2>&1 || \
           curl -s "http://localhost:${port}/auth/validate" > /dev/null 2>&1 || \
           curl -s "http://localhost:${port}/history" > /dev/null 2>&1; then
            echo -e "${GREEN}✓ ${name} is up on port ${port}${NC}"
            return 0
        fi
        sleep 2
        count=$((count+1))
        echo -n "."
    done
    echo -e "${RED}✗ ${name} failed to start. Check $log${NC}"
    return 1
}

# Start in dependency order
start_service "auth-service"     "auth-service"     8081
start_service "history-service"  "history-service"  8084
start_service "quantity-service" "quantity-service" 8083
start_service "admin-service"    "admin-service"    8082
start_service "api-gateway"      "api-gateway"      8080

echo ""
echo -e "${GREEN}======================================================"
echo " All services started!"
echo "======================================================"
echo ""
echo " API Gateway  : http://localhost:8080"
echo " Auth Service : http://localhost:8081"
echo " Admin Service: http://localhost:8082"
echo " Quantity Svc : http://localhost:8083"
echo " History Svc  : http://localhost:8084"
echo ""
echo " Frontend     : cd frontend && npm install && npm run dev"
echo " Frontend URL : http://localhost:3000"
echo -e "======================================================${NC}"
echo ""
echo "Logs are in: $LOG_DIR/"
echo "To stop all: ./stop-all.sh"
