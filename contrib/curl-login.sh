#!/usr/bin/env bash

BASEURL="http://localhost:8080"
USERNAME=admin
PASSWORD=admin
curl -i -H "Content-Type: application/x-www-form-urlencoded" -c cookies.txt -H "Origin: http://localhost:8080/" -d "username=$USERNAME&password=$PASSWORD" -X POST http://localhost:8080/spring_security_check
csrf=$(curl --silent --cookie cookies.txt --cookie-jar cookies.txt -L "$BASEURL/spring_security_check" --data "username=$USERNAME&password=$PASSWORD&_csrf=$csrf" | grep -E -o -m1 "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}" )