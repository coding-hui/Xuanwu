VERSION=0.9-SNAPSHOT

images.build:
	mvn clean install -DskipTests
	for name in xuanwu-codegen; do\
		mvn package docker:build -Pcodegen-frontend -DskipTests -pl $$name; \
	done

images.push: images.build
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

