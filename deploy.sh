#!/usr/bin/env bash
set -e

COMPOSE_FILE=/root/docker-compose.prod.yml
CURRENT_COLOR_FILE=/root/current_color

# 1) 현재 색 읽기 (없으면 blue로 가정)
if [ -f "$CURRENT_COLOR_FILE" ]; then
  CURRENT_COLOR=$(cat "$CURRENT_COLOR_FILE")
else
  CURRENT_COLOR=blue
fi

if [ "$CURRENT_COLOR" = "blue" ]; then
  NEW_COLOR=green
else
  NEW_COLOR=blue
fi

echo "Current color: $CURRENT_COLOR, New color: $NEW_COLOR"

# 2) 새 색 서비스에 새 이미지 pull + 컨테이너 up
docker compose -f $COMPOSE_FILE pull spring_$NEW_COLOR
docker compose -f $COMPOSE_FILE up -d spring_$NEW_COLOR

# 3) 헬스체크 (Spring Actuator 기준 예시)
echo "Health check for spring_$NEW_COLOR..."
for i in {1..10}; do
  if docker exec spring_$NEW_COLOR curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "spring_$NEW_COLOR is healthy."
    break
  fi
  echo "Waiting for health... ($i/10)"
  sleep 3
done

# 마지막까지 실패하면 실패 처리
if ! docker exec spring_$NEW_COLOR curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
  echo "spring_$NEW_COLOR failed health check. Abort."
  exit 1
fi

# 4) nginx upstream을 새 색으로 스위칭
NGINX_CONF=/root/nginx/conf.d/kitchen-log.conf

if [ "$NEW_COLOR" = "blue" ]; then
  sed -i 's/server spring_green:8080;/# server spring_green:8080;/' $NGINX_CONF
  sed -i 's/# server spring_blue:8080;/server spring_blue:8080;/' $NGINX_CONF
else
  sed -i 's/server spring_blue:8080;/# server spring_blue:8080;/' $NGINX_CONF
  sed -i 's/# server spring_green:8080;/server spring_green:8080;/' $NGINX_CONF
fi

docker compose -f $COMPOSE_FILE exec nginx nginx -t
docker compose -f $COMPOSE_FILE exec nginx nginx -s reload

# 5) 기존 색 컨테이너 정리 (선택)
docker compose -f $COMPOSE_FILE stop spring_$CURRENT_COLOR
docker compose -f $COMPOSE_FILE rm -f spring_$CURRENT_COLOR

# 6) 현재 색 기록 업데이트
echo "$NEW_COLOR" > "$CURRENT_COLOR_FILE"
echo "Blue-Green deploy complete. Active color: $NEW_COLOR"
