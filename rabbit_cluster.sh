export REGION=us-central1
export PROJECT_ID=$(gcloud config get project)
export MQ_CLUSTER=hello-world
gcloud container clusters create mq2ps --location ${REGION} \
  --enable-image-streaming --enable-shielded-nodes \
  --shielded-secure-boot --shielded-integrity-monitoring \
  --enable-ip-alias \
  --node-locations=$REGION-b \
  --machine-type n2d-standard-4 \
  --num-nodes 2 --min-nodes 1 --max-nodes 3 \
  --scopes="gke-default,storage-rw"


gcloud container node-pools create mq2ps-pool --cluster mq2ps  \
 --machine-type n2d-standard-4 --ephemeral-storage-local-ssd=count=1   --enable-autoscaling --enable-image-streaming \
   --num-nodes=0 --min-nodes=0 --max-nodes=3   --node-version=1.27.5-gke.200 --node-locations $REGION-a,$REGION-b --region $REGION 



kubectl apply -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml


kubectl apply -f rabbitmq_cluster.sh


kubectl get rabbitmqcluster

#Get user name:
kubectl get secret $MQ_CLUSTER-default-user -o jsonpath='{.data.username}' | base64 --decode
###Password
kubectl get secret $MQ_CLUSTER-default-user -o jsonpath='{.data.password}' | base64 --decode

### External IP for integration:
kubectl get svc $MQ_CLUSTER -o jsonpath='{.status.loadBalancer.ingress[0].ip}'






