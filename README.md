# Xuanwu

## Overview

The Xuanwu Project is a collection of services and tools designed to facilitate various aspects of application development and deployment. It includes modules for code generation, mall services, and more.

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker

### Building the Project

To build the project, run the following command:

```sh
make mvn.build
```

### Building Docker Images

To build Docker images for the services, run:

```sh
make images.build
```

### Deploying to Kubernetes

To deploy the services to a Kubernetes cluster, run:

```sh
make k8s.install
```

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Contact

For any questions or feedback, please contact us at [wecoding@yeah.net](mailto:wecoding@yeah.net).
