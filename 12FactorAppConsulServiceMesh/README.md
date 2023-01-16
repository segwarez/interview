# Consul ServiceMesh on Kubernetes

Content:

- Identity provider - Keycloak Authorization Code Flow with PKCE
- API Gateways - Kong on Kubernetes, Spring API Gateway for local development
- Observability - Zipkin, Prometheus and Grafana

## Cleanup in progress!


minikube start
minikube docker-env | Invoke-Expression
minikube dashboard

helm search repo kong/kong
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo add hashicorp https://helm.releases.hashicorp.com
helm repo add kong https://charts.konghq.com
helm repo update

helm install -f consul.yaml consul hashicorp/consul --version "0.38.0"
helm install -f prometheus.yaml prometheus prometheus-community/prometheus --version "15.0.1"
helm install -f grafana.yaml grafana grafana/grafana --version "6.19.2"

kubectl apply -f ./consul-k8s.yaml
helm install gapa kong/kong --version 2.6.3 --values kong.yaml
helm install gapa kong/kong --version 2.3.0 --values kong.yaml
kubectl apply -f ./kong-proxy.yaml
kubectl get pods -l='app.kubernetes.io/name=kong'


kubectl apply -f ./zipkin.yaml
kubectl create -f zipkin-k8s.yml

docker build -t segware/gapa-service:1.0 .
docker build -t segware/gapa-second:1.0 .

minikube service gapa-consul-ui

minikube tunnel

kubectl port-forward deploy/gapa-service 8081:8081
kubectl port-forward svc/zipkin 9411:9411
kubectl port-forward svc/grafana 3000:3000
kubectl port-forward deploy/prometheus-server 9090:9090

sum by(__name__)({app="gapa-service"})
sum by(__name__)({app="gapa-second"})

--audit-policy-file=/etc/kubernetes/audit-policy.yaml --audit-log-path=/var/log/audit.log