#!/bin/sh

NOTE=$1

curl -v -X POST http://localhost:9000/things -H 'Content-type: application/json' \
 -d '{
    "note": "test",
    "link": "http://alerta.io",
    "type": "website",
    "userId": 44,
    "status": "new",
    "isPublic": true,
    "rating": 5,
    "votes": 1,
    "tags": [ "graphite", "devops", "alerta" ],
    "location": { "latitude": 10, "longitude": 20, "altitude": 30 },
    "image": "duggee2.jpg"
}' | jq .
echo
