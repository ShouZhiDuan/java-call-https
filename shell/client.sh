#!/bin/bash

mkdir client
openssl genrsa -des3 -out ./client/client.key 2048
openssl req -new -key ./client/client.key -out ./client/client.csr
openssl ca -in ./client/client.csr -cert ./ca.crt -keyfile ./private/cakey.pem -out ./client/client.crt -config "/etc/pki/tls/openssl.cnf"
openssl pkcs12 -export -clcerts -in ./client/client.crt -inkey ./client/client.key -out ./client/client.p12