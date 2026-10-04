<div align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset=".github/logo-light.webp">
    <img src=".github/logo-dark.webp" alt="Dulno logo" width="128">
  </picture>

  <h1><b>Dulno - Core</b><br><br></h1>

</div>

Core of the backend of Dulno. Each module relies on the core. It bundles central functionalities and forms the framework of the entire application.

## Status

|      | Pipeline status                                                      |
|------|----------------------------------------------------------------------|
| main | ![](https://github.com/dulno/dulno-core/actions/workflows/ci.yml/badge.svg?branch=main) |

## Architecture

Each server in the cluster runs an ingress and an Nginx instance that balance requests across several core replicas. All core instances share a Cassandra database cluster and register with the [operator](https://github.com/dulno/dulno-operator), which distributes work between the nodes.

```mermaid
flowchart TB
    request(["Request"]) --> loadBalancer["Load Balancer"]

    subgraph cluster["Cluster"]
        direction LR
        subgraph server1["Server 1"]
            ingress1["Ingress"] --> nginx1["Nginx"]
            nginx1 --> core11["Core"]
            nginx1 --> core12["Core"]
            nginx1 --> core13["Core"]
        end
        subgraph server2["Server 2"]
            ingress2["Ingress"] --> nginx2["Nginx"]
            nginx2 --> core21["Core"]
            nginx2 --> core22["Core"]
            nginx2 --> core23["Core"]
        end
        subgraph server3["Server 3"]
            ingress3["Ingress"] --> nginx3["Nginx"]
            nginx3 --> core31["Core"]
            nginx3 --> core32["Core"]
            nginx3 --> core33["Core"]
        end
    end

    loadBalancer --> ingress1 & ingress2 & ingress3

    subgraph shared["Shared services"]
        direction LR
        database[("Cassandra cluster<br>one node per server")]
        operator["Operator"]
    end

    cluster -- "data" --> database
    cluster <-- "distribution" --> operator
```

At startup, the core loads all module JARs (for example [access](https://github.com/dulno/dulno-access)) from the `/modules` directory.

## Integration
This module can be integrated into a submodule.

To do this, publish the core to your local Maven repository first:
```bash
./gradlew publishToMavenLocal
```

Then add `mavenLocal()` to the repositories in *build.gradle.kts*:
```kotlin
repositories {
  mavenCentral()
  mavenLocal()
}
```

The repository can then be added and used like a regular dependency. This is done in the following way:
```kotlin
dependencies {
  compileOnly("com.dulno:core:1.0.0-SNAPSHOT")
}
```

## License

© Dulno. Licensed under [CC BY-NC-SA 4.0](LICENSE): free for non-commercial use with attribution to Dulno, modifications must be shared under the same license. Third-party code (e.g. under `static/dependency/`) keeps its own license.
