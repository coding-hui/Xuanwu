VERSION=0.9-SNAPSHOT

images.build:
	for name in xuanwu-codegen; do\
		mvn clean package docker:build -DskipTests -pl $$name; \
	done

images.push:
	for name in xuanwu-codegen; do\
		mvn docker:push -pl $$name; \
	done

k8s.install:
	for name in xuanwu-codegen; do\
		kubectl apply -k ./deploy/kubernetes/$$name; \
	done

k8s.uninstall:
	for name in xuanwu-codegen; do\
		kubectl delete -k ./deploy/kubernetes/$$name; \
	done

k8s.update-images:
	DEPLOYS=xuanwu-codegen ./scripts/update_pod_images.sh

