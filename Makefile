VERSION ?= 0.9-SNAPSHOT
IMAGES ?= "xuanwu-mall xuanwu-codegen"

MAVEN_ARGS ?=
MAVEN_ARGS += $(if $(DOCKER_USER),-Ddocker.username=$(DOCKER_USER))
MAVEN_ARGS += $(if $(DOCKER_PWD),-Ddocker.password=$(DOCKER_PWD))
MAVEN_ARGS += $(if $(REGISTRY),-Ddocker.registry=$(REGISTRY))
MAVEN_ARGS += $(if $(REGISTRY_PREFIX),-Ddocker.namespace=$(REGISTRY_PREFIX))

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
		$(if $(filter xuanwu-exam,$(IMAGE)),-Pexam-frontend) \
		-DskipTests -pl services/$(IMAGE) $(MAVEN_ARGS)

.PHONY: images.native.build.%
images.native.build.%:
	$(eval IMAGE := $*)
	@echo "===========> Building docker native image $(IMAGE) $(VERSION)"
	mvn -Pnative clean package -DskipTests -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.push
images.push: mvn.build $(addprefix images.push., $(IMAGES))

.PHONY: images.push.%
images.push.%: images.build.%
	$(eval IMAGE := $*)
	@echo "===========> Pushing docker image $(IMAGE) $(VERSION)"
	mvn docker:push -pl services/$(IMAGE) $(MAVEN_ARGS)

.PHONY: k8s.install
k8s.install:
	for name in xuanwu-codegen xuanwu-mall xuanwu-exam; do\
		echo "===========> Install $$name"; \
		kubectl apply -k ./deploy/kubernetes/$$name; \
	done

.PHONY: k8s.uninstall
k8s.uninstall:
	for name in xuanwu-codegen xuanwu-mall xuanwu-exam; do\
		echo "===========> Uninstall $$name"; \
		kubectl delete -k ./deploy/kubernetes/$$name; \
	done

.PHONY: k8s.update-images
k8s.update-images:
	./scripts/update_pod_images.sh
