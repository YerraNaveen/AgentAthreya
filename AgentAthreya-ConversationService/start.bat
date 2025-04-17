@echo off
echo 🚀 Starting AgentAthreya backend...
docker-compose --env-file .env up -d --build
echo ✅ AgentAthreya backend is up and running!
pause