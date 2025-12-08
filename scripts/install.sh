#!/bin/bash

set -e

# Script directory
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")
DEPLOY_DIR="$PROJECT_ROOT/deploy/kubernetes"

# Default configuration
NAMESPACE="xuanwu-system"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO] $1${NC}"
}

log_warn() {
    echo -e "${YELLOW}[WARN] $1${NC}"
}

log_error() {
    echo -e "${RED}[ERROR] $1${NC}"
}

usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  -s, --service <service>      Specify the service to install (e.g., xuanwu-exam)"
    echo "  -a, --all                    Install all available services"
    echo "  -h, --help                   Show this help message"
}

# Parse arguments
SERVICES=()
INSTALL_ALL=false

while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        -s|--service)
            SERVICES+=("$2")
            shift
            shift
            ;;
        -a|--all)
            INSTALL_ALL=true
            shift
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

# Get all available services
AVAILABLE_SERVICES=()
for dir in "$DEPLOY_DIR"/*/; do
    service_name=$(basename "$dir")
    if [ -f "$dir/kustomization.yml" ] || [ -f "$dir/kustomization.yaml" ]; then
        AVAILABLE_SERVICES+=("$service_name")
    fi
done

# If --all is specified, add all services
if [ "$INSTALL_ALL" = true ]; then
    SERVICES=("${AVAILABLE_SERVICES[@]}")
fi

# If no service is specified, show error and available services
if [ ${#SERVICES[@]} -eq 0 ]; then
    log_error "No services specified."
    echo "Available services:"
    for svc in "${AVAILABLE_SERVICES[@]}"; do
        echo "  - $svc"
    done
    usage
    exit 1
fi

# Check and create namespace
if ! kubectl get namespace "$NAMESPACE" > /dev/null 2>&1; then
    log_info "Creating namespace: $NAMESPACE"
    kubectl create namespace "$NAMESPACE"
else
    log_info "Namespace $NAMESPACE already exists."
fi

# Deploy services
for service in "${SERVICES[@]}"; do
    SERVICE_DIR="$DEPLOY_DIR/$service"
    
    # Check if service exists
    if [ ! -d "$SERVICE_DIR" ]; then
        log_warn "Service directory not found: $SERVICE_DIR"
        continue
    fi

    # Check for kustomization file
    if [ ! -f "$SERVICE_DIR/kustomization.yml" ] && [ ! -f "$SERVICE_DIR/kustomization.yaml" ]; then
        log_warn "No kustomization.yml found in $SERVICE_DIR, skipping..."
        continue
    fi

    log_info "Deploying $service..."
    
    # Deploy using kustomize
    if kubectl apply -k "$SERVICE_DIR"; then
        log_info "Successfully deployed $service"
    else
        log_error "Failed to deploy $service"
        exit 1
    fi
done

log_info "All tasks completed."
