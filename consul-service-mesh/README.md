# Consul Service Mesh and Kong Gateway

## Steps:

brew install kubectl

brew install minikube

minikube version

minikube start --driver=docker

eval $(minikube docker-env)

docker build -t order:1.0 .
docker build -t warehouse:1.0 .
docker build -t billing:1.0 .
docker build -t delivery:1.0 .

minikube image list

helm repo add hashicorp https://helm.releases.hashicorp.com
helm repo add kong https://charts.konghq.com
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana-community https://grafana-community.github.io/helm-charts
helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts

helm repo update

helm search repo hashicorp/consul
helm search repo kong/kong
helm search repo grafana-community/tempo
helm search repo prometheus-community/kube-prometheus-stack
helm search repo open-telemetry/opentelemetry-collector

helm install prometheus prometheus-community/kube-prometheus-stack --version 81.5.0 --namespace observability --create-namespace --values ./k8s/helm/prometheus.yaml
helm install tempo grafana-community/tempo --version 1.26.1 --namespace observability --values ./k8s/helm/tempo.yaml
helm install otel-collector open-telemetry/opentelemetry-collector --version 0.145.0 --namespace observability --values ./k8s/helm/otel-collector.yaml
helm install consul hashicorp/consul --version 1.9.3 --namespace consul --create-namespace --values ./k8s/helm/consul.yaml
helm install kong kong/kong --version 3.0.2 --namespace kong --create-namespace --values ./k8s/helm/kong.yaml

kubectl port-forward -n kong svc/kong-kong-admin 8444:8444
curl -k https://localhost:8444

kubectl port-forward -n consul svc/consul-ui 8500:443