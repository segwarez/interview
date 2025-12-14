# Consul Service Mesh and Kong Gateway

## Steps:

brew install kubectl

brew install minikube

minikube version

minikube start --driver=docker

eval $(minikube docker-env)

./gradlew clean bootJar

docker build -t order:1.0 .

minikube image list

helm repo add hashicorp https://helm.releases.hashicorp.com
helm repo add kong https://charts.konghq.com
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts

helm repo update

helm search repo hashicorp/consul
helm search repo kong/kong
helm search repo prometheus-community/kube-prometheus-stack

helm install prometheus prometheus-community/kube-prometheus-stack --version 79.12.0 --namespace prometheus --create-namespace  --values ./k8s/helm/prometheus.yaml
helm install consul hashicorp/consul --version 1.9.1 --namespace consul --create-namespace --values ./k8s/helm/consul.yaml
helm install kong kong/kong --version 3.0.1 --namespace kong --create-namespace --values ./k8s/helm/kong.yaml

kubectl port-forward -n kong svc/kong-kong-admin 8444:8444
curl -k https://localhost:8444

kubectl port-forward -n consul svc/consul-ui 8500:443