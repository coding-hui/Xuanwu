#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

# The root of the build/dist directory
XUANWU_ROOT=$(dirname "${BASH_SOURCE[0]}")/..

TAG="${TAG:-latest}"
NAMESPACE="${NAMESPACE:-xuanwu-system}" # 设置默认的命名空间
REGISTRY="${REPO:-devops-wecoding-docker.pkg.coding.net/wecoding/images}"
DEPLOYS="${DEPLOYS:-xuanwu-codegen}" # 部署列表，使用逗号分隔

function wait_for_installation_finish() {
  echo "waiting for XUANWU pod ready..."
  kubectl -n "$NAMESPACE" wait --timeout=180s --for=condition=Ready pods -l app.kubernetes.io/instance=xuanwu

  start_time=$(date +%s)
  timeout_seconds=900 # 超时时间，单位为秒

  echo "waiting for XUANWU ready..."
  while :; do
    current_time=$(date +%s)
    elapsed_time=$((current_time - start_time))
    if [ "$elapsed_time" -ge "$timeout_seconds" ]; then
      iam::log::error "Timeout: XUANWU Pod did not become ready within $timeout_seconds seconds."
      exit 1
    fi

    pod_status=$(kubectl -n "$NAMESPACE" get pod -l app.kubernetes.io/instance=xuanwu -o jsonpath="{.items[0].status.containerStatuses[0].ready}")
    if [ "$pod_status" == "true" ]; then
      break
    fi

    sleep 1
  done

  echo "XUANWU is ready!"
}

if [[ "$DEPLOYS" == *xuanwu-codegen* ]]; then
  # Update xuanwu-codegen image
  kubectl -n "$NAMESPACE" set image deployment/xuanwu-codegen codegen="${REGISTRY}/xuanwu-codegen:${TAG}"
  # Restart xuanwu-codegen deployment
  kubectl -n "$NAMESPACE" rollout restart deployment/xuanwu-codegen
fi

wait_for_installation_finish
