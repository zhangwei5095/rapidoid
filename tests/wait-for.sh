PORT=$1

for i in $(seq 1 10); do
  curl -s "http://localhost:$PORT/" >> /dev/null || sleep 1
done
