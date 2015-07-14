#!/bin/sh

NOTE=$1

curl -v -X POST http://localhost:9000/things -H 'Content-type: application/json' \
 -d '{
    "note": "$NOTE",
    "link": "http://alerta.io",
    "type": "website",
    "userId": 44,
    "status": "new",
    "isPublic": true,
    "rating": 5,
    "votes": 1,
    "tags": [ "monitoring", "devops" ],
    "location": { "latitude": 1, "longitude": 2, "altitude": 3 },
    "image": "duggee.jpg"
}' | jq .
echo
