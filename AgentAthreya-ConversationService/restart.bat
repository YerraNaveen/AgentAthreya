@echo off
echo Restarting AgentAthreya containers...
docker-compose down
docker-compose --env-file .env up -d --build
echo Restart complete. AgentAthreya is running fresh!
pause