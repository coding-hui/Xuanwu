VERSION ?= 0.9-SNAPSHOT
IMAGES ?= "xuanwu-mall xuanwu-codegen"

.PHONY: mvn.build
mvn.build:
	mvn clean install -DskipTests

.PHONY: images.build
images.build: mvn.build $(addprefix images.build., $(IMAGES))

.PHONY: images.build.%
images.build.%:
	$(eval IMAGE := $*)
	@echo "===========> Building docker image $(IMAGE) $(VERSION)"
	mvn package docker:build \
		$(if $(filter xuanwu-mall,$(IMAGE)),-Pmall-frontend) \
		$(if $(filter xuanwu-codegen,$(IMAGE)),-Pcodegen-frontend) \
		-DskipTests -pl $(IMAGE)

.PHONY: images.push
images.push: $(addprefix images.push., $(IMAGES))

.PHONY: images.push.%
images.push.%: images.build.%
	$(eval IMAGE := $*)
	@echo "===========> Pushing docker image $(IMAGE) $(VERSION)"
	mvn docker:push -pl $(IMAGE)

.PHONY: k8s.install
k8s.install:
	for name in xuanwu-codegen; do\
		kubectl apply -k ./deploy/kubernetes/$$name; \
	done

.PHONY: k8s.uninstall
k8s.uninstall:
	for name in xuanwu-codegen; do\
		kubectl delete -k ./deploy/kubernetes/$$name; \
	done

.PHONY: k8s.update-images
k8s.update-images:
	DEPLOYS=xuanwu-codegen ./scripts/update_pod_images.sh

