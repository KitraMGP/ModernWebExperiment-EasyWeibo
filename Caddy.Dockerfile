# 构建前端
FROM node:22-slim AS frontend-build
WORKDIR /app
COPY frontend/package*.json .
RUN npm install
COPY frontend .
RUN npm run build

# 构建 Caddy 镜像
FROM caddy:2.10-alpine
COPY --from=frontend-build /app/dist /usr/share/caddy
COPY Caddyfile /etc/caddy/Caddyfile
ENTRYPOINT [ "caddy", "run", "--config", "/etc/caddy/Caddyfile" ]