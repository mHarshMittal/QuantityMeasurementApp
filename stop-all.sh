#!/bin/bash
LOG_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/logs"

echo "Stopping all microservices..."

for pid_file in "$LOG_DIR"/*.pid; do
    if [ -f "$pid_file" ]; then
        pid=$(cat "$pid_file")
        name=$(basename "$pid_file" .pid)
        if kill -0 "$pid" 2>/dev/null; then
            kill "$pid"
            echo "Stopped $name (PID: $pid)"
        fi
        rm -f "$pid_file"
    fi
done

# Also kill any leftover java processes on our ports
for port in 8080 8081 8082 8083 8084; do
    pid=$(lsof -ti:$port 2>/dev/null)
    if [ ! -z "$pid" ]; then
        kill -9 $pid 2>/dev/null
        echo "Killed process on port $port"
    fi
done

echo "All services stopped."
