docker compose -f src/main/docker/app.yml down
git pull
npm run java:docker
docker compose -f src/main/docker/app.yml up -d
