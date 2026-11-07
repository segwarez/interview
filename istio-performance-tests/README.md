# Istio Performance Tests

## Steps:

minikube start

helm repo add istio https://blob.istio.io/istio-release/charts
helm repo update
helm upgrade --install istio-base istio/base -n istio-system --create-namespace --set defaultRevision=default
helm upgrade --install istiod istio/istiod -n istio-system --wait
helm upgrade --install istio-ingressgateway istio/gateway -n istio-ingress --create-namespace

kubectl apply -f k8s/

openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout api.key -out api.crt -subj "/CN=api.segwarez.com" -addext "subjectAltName=DNS:api.segwarez.com"

kubectl -n istio-ingress create secret tls api-segwarez-com-tls --key=api.key --cert=api.crt --dry-run=client -o yaml | kubectl apply -f -

openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout perf-https.key -out perf-https.crt -subj "/CN=perf-https.passthrough.segwarez.com" -addext "subjectAltName=DNS:perf-https.passthrough.segwarez.com,DNS:perf-https.perf-test.svc.cluster.local"

kubectl -n perf-test create secret tls perf-https-tls --key=perf-https.key --cert=perf-https.crt --dry-run=client -o yaml | kubectl apply -f -

kubectl -n perf-test exec deploy/perf-client -c perf-client -- curl -sS -o /dev/null -w 'server: %{http_code}\n' http://perf-server/

kubectl -n perf-test exec deploy/perf-client -c perf-client -- curl -sS -o /dev/null -w 'error: %{http_code}\n' http://perf-error/

kubectl port-forward -n istio-ingress svc/istio-ingressgateway 8443:443

curl -k -i https://api.segwarez.com:8443/server
curl -k -i https://api.segwarez.com:8443/error
curl -k -i https://api.segwarez.com:8443/client

curl --resolve perf-https.passthrough.segwarez.com:8443:127.0.0.1 --cacert perf-https.crt -i https://perf-https.passthrough.segwarez.com:8443/

./client-ingress-test.sh /server 1000 2
./client-ingress-test.sh /server 1000 1.1