# mirrord-srv-connections-broken
Description: This repo shows a reproducible example of SRV lookups failing when using MirrorD.

## MirrorD issue

When intercepting a remote pod using MirrorD and rerouting traffic to your locally running service, 
the local service fails to connect to MongoDB when the service is configured to perform an SRV lookup on the Mongo host, i.e
when the Mongo host is prefixed with `mongodb+srv://`.

This repo aims to create a reproducible example of this issue.

## Insights

The issue appears for MirrorD binary 3.97.0 and later.

The issue also only appears when a `http_filter` is set in the `.mirrord.yaml` file.

## Pre-requisites

* You will need a Kubernetes cluster to deploy the resources into.
* You should have the MirrorD application installed, either as a binary or as an IDE plugin.
* You will need the `kubectl` command installed on your machine
* *Optional*: `Java 17` and `Maven 3.9.1` is required if you wish to rebuild the JAR locally
* *Optional*: `docker` installed along with your own Dockerhub account and repo. This is only if you wish to fork the java example and deploy the fork in your cluster.
* CoreDNS installed in your Kubernetes cluster
* CoreDNS ConfigMap should be updated to include an SRV lookup for the mongo hosts, add the below `template` block into the configmap

`kubectl edit cm -n kube-system coredns`

```
.:53 {
        ...
        template IN SRV {
          match "^_mongodb._tcp.my-mongodb-host.default.svc.cluster.local.$"
          answer "{{.Name}} 60 IN SRV 0 5 27017 my-mongodb1.default.svc.cluster.local"
          answer "{{.Name}} 60 IN SRV 0 5 27017 my-mongodb2.default.svc.cluster.local"
          fallthrough
        }
        ...
      }
```

## Deploy example into your cluster

The following resources must be deployed in your Kubernetes cluster in the following order:

```bash
kubectl apply -f kubernetes/mongo/pods.yml
kubectl apply -f kubernetes/mongo/services.yml
kubectl apply -f kubernetes/main-app/pod.yml
```

## Updating Java app

These steps are optional if you wish to update the existing docker image stored in Dockerhub.

### Rebuild JAR

Run the following while checked out in this repository: 

`mvn -B -U package`

The build JAR should be generated in `./target/`

### Rebuild and Push Docker image

You will need to have your own Dockerhub account and Dockerhub repo.
Run the following while checked out in this repository:

`docker build -t YOUR_DOCKER_USER/YOUR_DOCKER_REPO:0.1.0 .`

`docker push YOUR_DOCKER_USER/YOUR_DOCKER_REPO:0.1.0`
