VERSION ?= 0.9-SNAPSHOT
IMAGES ?= "xuanwu-mall xuanwu-codegen xuanwu-exam"

MAVEN_ARGS ?=
MAVEN_ARGS += $(if $(DOCKER_USER),-Ddocker.username=$(DOCKER_USER))
MAVEN_ARGS += $(if $(DOCKER_PWD),-Ddocker.password=$(DOCKER_PWD))
MAVEN_ARGS += $(if $(REGISTRY),-Ddocker.registry=$(REGISTRY))
MAVEN_ARGS += $(if $(REGISTRY_PREFIX),-Ddocker.namespace=$(REGISTRY_PREFIX))

.PHONY: mvn.build
mvn.build:
	mvn clean install -DskipTests

.PHONY: images.build
images.build: mvn.build images.build.mall images.build.codegen images.build.exam

.PHONY: images.native.build
images.native.build: mvn.build images.native.build.exam

.PHONY: images.build.mall
images.build.mall:
	@echo "===========> Building docker image xuanwu-mall $(VERSION)"
	mvn package docker:build -Pmall-frontend -DskipTests -pl services/xuanwu-mall $(MAVEN_ARGS)

.PHONY: images.build.codegen
images.build.codegen:
	@echo "===========> Building docker image xuanwu-codegen $(VERSION)"
	mvn package docker:build -Pcodegen-frontend -DskipTests -pl services/xuanwu-codegen $(MAVEN_ARGS)

.PHONY: images.build.exam
images.build.exam:
	@echo "===========> Building docker image xuanwu-exam $(VERSION)"
	mvn package docker:build -Pexam-frontend -DskipTests -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.native.build.exam
images.native.build.exam: native.build.exam
	@echo "===========> Building docker native image xuanwu-exam $(VERSION)"
	docker build -f services/xuanwu-exam/Dockerfile.native -t xuanwu-exam:native --build-arg VERSION=$(VERSION) --build-arg BUILD_DATE=$(shell date -u +%Y-%m-%dT%H:%M:%SZ) .

.PHONY: native.build.exam
native.build.exam:
	mvn -Pnative clean package -DskipTests -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.push
images.push: mvn.build images.push.mall images.push.codegen images.push.exam

.PHONY: images.push.mall
images.push.mall: images.build.mall
	@echo "===========> Pushing docker image xuanwu-mall $(VERSION)"
	mvn docker:push -pl services/xuanwu-mall $(MAVEN_ARGS)

.PHONY: images.push.codegen
images.push.codegen: images.build.codegen
	@echo "===========> Pushing docker image xuanwu-codegen $(VERSION)"
	mvn docker:push -pl services/xuanwu-codegen $(MAVEN_ARGS)

.PHONY: images.push.exam
images.push.exam: images.build.exam
	@echo "===========> Pushing docker image xuanwu-exam $(VERSION)"
	mvn docker:push -pl services/xuanwu-exam $(MAVEN_ARGS)

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
