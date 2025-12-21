VERSION ?= 0.9-SNAPSHOT
IMAGES ?= "xuanwu-mall xuanwu-codegen xuanwu-exam"

.DEFAULT_GOAL := help
SHELL := /usr/bin/env bash
.SHELLFLAGS := -eu -o pipefail -c

# Registry settings (compose final image tag parts)
REGISTRY_URL := $(if $(REGISTRY),$(REGISTRY)/,)
REGISTRY_NS := $(if $(REGISTRY_PREFIX),$(REGISTRY_PREFIX)/,)

# Image name shortcuts
EXAM_IMAGE := xuanwu-exam
EXAM_IMAGE_TAG := $(REGISTRY_URL)$(REGISTRY_NS)$(EXAM_IMAGE):$(VERSION)

# Docker build args
DOCKER_BUILD_ARGS := --build-arg VERSION=$(VERSION) --build-arg BUILD_DATE=$(shell date -u +%Y-%m-%dT%H:%M:%SZ)

MAVEN_ARGS ?=
MAVEN_ARGS += $(if $(DOCKER_USER),-Ddocker.username=$(DOCKER_USER))
MAVEN_ARGS += $(if $(DOCKER_PWD),-Ddocker.password=$(DOCKER_PWD))
MAVEN_ARGS += $(if $(REGISTRY),-Ddocker.registry=$(REGISTRY))
MAVEN_ARGS += $(if $(REGISTRY_PREFIX),-Ddocker.namespace=$(REGISTRY_PREFIX))

.PHONY: mvn.build
mvn.build: ## Build all modules with Maven (skip tests)
	mvn clean install -DskipTests

.PHONY: native.build.exam
native.build.exam: ## Build xuanwu-exam native binary with Maven
	mvn -Pnative clean package -DskipTests -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.build
images.build: mvn.build images.build.mall images.build.codegen images.build.exam ## Build docker images (mall, codegen, exam)

.PHONY: images.native.build
images.native.build: mvn.build images.native.build.exam ## Build native docker images (exam)

.PHONY: images.build.mall
images.build.mall: ## Build docker image xuanwu-mall
	@echo "===========> Building docker image xuanwu-mall $(VERSION)"
	mvn package docker:build -Pmall-frontend -DskipTests -pl services/xuanwu-mall $(MAVEN_ARGS)

.PHONY: images.build.codegen
images.build.codegen: ## Build docker image xuanwu-codegen
	@echo "===========> Building docker image xuanwu-codegen $(VERSION)"
	mvn package docker:build -Pcodegen-frontend -DskipTests -pl services/xuanwu-codegen $(MAVEN_ARGS)

.PHONY: images.build.exam
images.build.exam: ## Build docker image xuanwu-exam
	@echo "===========> Building docker image xuanwu-exam $(VERSION)"
	mvn package docker:build -Pexam-frontend -DskipTests -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.native.build.exam
images.native.build.exam: native.build.exam ## Build xuanwu-exam native image
	@echo "===========> Building docker native image $(EXAM_IMAGE) $(VERSION)"
	mvn package docker:build -Pnative -DskipTests -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.push
images.push: mvn.build images.push.mall images.push.codegen images.push.exam ## Push docker images (mall, codegen, exam)

.PHONY: images.push.mall
images.push.mall: images.build.mall ## Push xuanwu-mall image
	@echo "===========> Pushing docker image xuanwu-mall $(VERSION)"
	mvn docker:push -pl services/xuanwu-mall $(MAVEN_ARGS)

.PHONY: images.push.codegen
images.push.codegen: images.build.codegen ## Push xuanwu-codegen image
	@echo "===========> Pushing docker image xuanwu-codegen $(VERSION)"
	mvn docker:push -pl services/xuanwu-codegen $(MAVEN_ARGS)

.PHONY: images.push.exam
images.push.exam: images.build.exam ## Push xuanwu-exam image
	@echo "===========> Pushing docker image xuanwu-exam $(VERSION)"
	mvn docker:push -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: images.native.push.exam
images.native.push.exam: images.native.build.exam ## Push native xuanwu-exam image
	@echo "===========> Pushing native image $(EXAM_IMAGE_TAG)"
	mvn docker:push -Pnative -pl services/xuanwu-exam $(MAVEN_ARGS)

.PHONY: k8s.install
k8s.install: ## Apply Kubernetes manifests
	for name in xuanwu-codegen xuanwu-mall xuanwu-exam; do \
		echo "===========> Install $$name"; \
		kubectl apply -k ./deploy/kubernetes/$$name; \
	done

.PHONY: k8s.uninstall
k8s.uninstall: ## Delete Kubernetes manifests
	for name in xuanwu-codegen xuanwu-mall xuanwu-exam; do \
		echo "===========> Uninstall $$name"; \
		kubectl delete -k ./deploy/kubernetes/$$name; \
	done

.PHONY: k8s.update-images
k8s.update-images: ## Update running pod images
	./scripts/update_pod_images.sh

.PHONY: help
help: ## Show available targets
	@echo "Available targets:"
	@grep -hE '^[a-zA-Z0-9_.-]+:.*## ' $(MAKEFILE_LIST) | \
	awk -F':|##' '{printf "  %-28s %s\n", $$1, $$3}'
