# Consul Service Mesh and Kong Gateway

## Steps:

brew install kubectl

brew install minikube

brew install skaffold

minikube start --driver=docker

eval $(minikube docker-env)

skaffold run

sudo minikube tunnel

http://localhost/api/v1/order

kubectl port-forward -n observability svc/prometheus-grafana 3000:80
kubectl port-forward -n observability svc/prometheus-kube-prometheus-prometheus 9090:9090
kubectl port-forward -n observability svc/jaeger 16686:16686
kubectl port-forward -n consul svc/consul-ui 8500:443
kubectl port-forward -n kong svc/kong-gateway-admin 8444:8444