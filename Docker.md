# Complete Docker Study Guide

---

## Table of Contents

1. [What is Docker?](#what-is-docker)
2. [Docker Architecture](#docker-architecture)
3. [Images & Containers](#images--containers)
4. [Working with Images](#working-with-images)
5. [Working with Containers](#working-with-containers)
6. [Docker Networking](#docker-networking)
7. [Docker Volumes](#docker-volumes)
8. [Docker Compose Basics](#docker-compose-basics)
9. [Docker Compose Advanced](#docker-compose-advanced)
10. [Best Practices](#best-practices)
11. [Real-World Examples](#real-world-examples)
12. [Troubleshooting](#troubleshooting)

---

## What is Docker?

### The Core Concept

Docker is a **containerization platform** that packages your application, its dependencies, libraries, and runtime into a self-contained unit called a **container**. This container runs consistently across any machine — your laptop, your Raspberry Pi, a cloud server — without the "works on my machine" problem.

### Docker vs Virtual Machines

| Aspect                  | Virtual Machine (VM)   | Docker Container               |
| ----------------------- | ---------------------- | ------------------------------ |
| **What it virtualizes** | Entire OS + kernel     | Only the application layer     |
| **Boot time**           | Minutes                | Seconds                        |
| **Disk space**          | Gigabytes              | Megabytes                      |
| **CPU overhead**        | High                   | Minimal                        |
| **Isolation level**     | Complete OS isolation  | Process-level isolation        |
| **Use case**            | Running different OSes | Running multiple app instances |

**Visual comparison:**

```
Traditional Server:
┌─────────────────┐
│   OS Kernel     │  ← Only one, shared by all
├─────────────────┤
│ App 1 | App 2   │
└─────────────────┘

Virtual Machines:
┌──────────────┐  ┌──────────────┐  ← Multiple full OSes
│ OS + Kernel  │  │ OS + Kernel  │     (heavy, slow)
├──────────────┤  ├──────────────┤
│   App 1      │  │   App 2      │
└──────────────┘  └──────────────┘

Docker Containers:
┌──────────────┐  ┌──────────────┐  ← Share one kernel
│   App 1      │  │   App 2      │    (lightweight, fast)
├──────────────┤  ├──────────────┤
│ Dependencies │  │ Dependencies │
└──────────────┴──┴──────────────┘
        ↓         ↓
    Shared Docker Engine
         ↓
    Shared OS Kernel
```

### Why Docker Matters

- **Consistency**: "Works on my machine" → works everywhere
- **Isolation**: One app's crash doesn't affect others
- **Efficiency**: Containers use far fewer resources than VMs
- **Portability**: Move containers between machines effortlessly
- **Scalability**: Spin up/down container instances instantly

---

## Docker Architecture

Docker follows a **client-server architecture**:

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Architecture                   │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  CLIENT (your terminal / CLI)                           │
│  ├─ docker run                                          │
│  ├─ docker ps                                           │
│  └─ docker build                                        │
│         ↓ REST API                                      │
│  DOCKER DAEMON (the server)                             │
│  ├─ Manages images                                      │
│  ├─ Manages containers                                  │
│  ├─ Handles networking                                  │
│  └─ Manages volumes                                     │
│         ↓                                               │
│  HOST OS KERNEL                                         │
│  ├─ cgroups (resource limits)                           │
│  ├─ namespaces (isolation)                              │
│  └─ union filesystems (layered storage)                 │
│                                                           │
└─────────────────────────────────────────────────────────┘
```

### Key Components

1. **Docker Client** — The CLI tool you interact with (`docker` command)
2. **Docker Daemon** — Background service that actually runs containers
3. **Docker Registry** — Central storage for images (Docker Hub, private registries)
4. **Image** — Blueprint for containers (immutable template)
5. **Container** — Running instance of an image (mutable, ephemeral)

---

## Images & Containers

### What's an Image?

An **image** is a **blueprint** — a read-only template that defines everything a container needs:

- Base OS (or minimal OS-like environment)
- Application code
- Dependencies & libraries
- Environment variables
- Entry point (what runs when the container starts)

**Images are immutable** — once built, they never change. You create containers _from_ images.

### What's a Container?

A **container** is a **running instance** created from an image. It's like the difference between:

- Image = Recipe
- Container = The actual meal you cooked from that recipe

You can create multiple containers from the same image, each with its own isolated filesystem, processes, and network stack.

### How They Relate

```
Docker Image (Immutable)
  ↓ docker run
Container 1 (Mutable)
  - Own filesystem
  - Own processes
  - Own network
  ↓ (can modify, create files, etc.)

Container 2 (Different instance)
  - Own filesystem
  - Own processes
  - Own network
  ↓ (changes don't affect Container 1 or the Image)
```

**Key insight:** You can delete and recreate a container instantly, and it's pristine again because it's recreated from the unchanged image.

---

## Working with Images

### Understanding Image Layers

Images are built in **layers**, like a stack of stickers. Each layer represents a build instruction:

```dockerfile
FROM ubuntu:22.04           # Layer 1: Base OS
RUN apt-get update          # Layer 2: Update packages
RUN apt-get install -y curl # Layer 3: Install curl
COPY app.py /app/           # Layer 4: Copy your app
RUN chmod +x /app/app.py    # Layer 5: Make executable
CMD ["python", "/app/app.py"] # Layer 6: Default command
```

Each layer only stores the _differences_ from the layer below (union filesystem). This makes images efficient and layers cacheable.

### Docker Hub Images

**Docker Hub** is the official registry where pre-built images live:

- `ubuntu:22.04` — Ubuntu Linux base image
- `python:3.11` — Python runtime
- `node:18` — Node.js runtime
- `pihole/pihole:latest` — Pi-hole DNS filtering
- `tailscale/tailscale:latest` — Tailscale VPN client

Naming: `[registry]/[repository]/[image]:[tag]`

- `docker.io/pihole/pihole:latest` (full path)
- `pihole/pihole:latest` (docker.io is default)
- `pihole/pihole` (latest tag is default)

### Common Image Commands

```bash
# Search Docker Hub for images
docker search ubuntu

# Pull (download) an image
docker pull python:3.11

# List all images on your machine
docker images
docker images -a  # Include intermediate layers

# Build an image from a Dockerfile
docker build -t myapp:1.0 .
# -t = tag (name and version)
# . = build context (current directory)

# Tag an image with a new name
docker tag myapp:1.0 myregistry/myapp:1.0

# Push to a registry
docker push myregistry/myapp:1.0

# Remove an image
docker rmi myapp:1.0
docker rmi $(docker images -q)  # Remove all images

# View image history (layers)
docker history myapp:1.0

# Inspect image details (JSON)
docker inspect pihole/pihole:latest
```

### Creating a Dockerfile

A **Dockerfile** is a script that describes how to build an image:

```dockerfile
# Use an official Python runtime as base
FROM python:3.11-slim

# Set working directory inside container
WORKDIR /app

# Copy files from host to container
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code
COPY . .

# Expose port (documentation, doesn't actually publish)
EXPOSE 5000

# Set environment variables
ENV FLASK_APP=app.py

# Run the app
CMD ["python", "app.py"]
```

### Dockerfile Instructions Explained

| Instruction  | What it does                          | Example                              |
| ------------ | ------------------------------------- | ------------------------------------ |
| `FROM`       | Sets base image                       | `FROM python:3.11`                   |
| `WORKDIR`    | Sets working directory in container   | `WORKDIR /app`                       |
| `COPY`       | Copies files from host to container   | `COPY app.py /app/`                  |
| `RUN`        | Executes command during build         | `RUN apt-get install curl`           |
| `EXPOSE`     | Documents which ports the app uses    | `EXPOSE 8080` (doesn't publish)      |
| `ENV`        | Sets environment variables            | `ENV DEBUG=true`                     |
| `ARG`        | Build-time variables                  | `ARG BUILD_DATE`                     |
| `CMD`        | Default command when container starts | `CMD ["python", "app.py"]`           |
| `ENTRYPOINT` | Configures container as executable    | `ENTRYPOINT ["python"]`              |
| `LABEL`      | Metadata                              | `LABEL maintainer="you@example.com"` |

### Building and Running a Container

```bash
# Build the image
docker build -t myapp:1.0 .

# Run a container from the image
docker run -d -p 5000:5000 --name mycontainer myapp:1.0
# -d = detached (background)
# -p 5000:5000 = map port 5000 (host) to 5000 (container)
# --name = give container a name
# myapp:1.0 = image to use

# Check if it's running
docker ps

# View logs
docker logs mycontainer
docker logs -f mycontainer  # Follow (live tail)
```

---

## Working with Containers

### Container Lifecycle

```
┌─────────────────────────────────────────┐
│        Container Lifecycle              │
├─────────────────────────────────────────┤
│                                         │
│  Created → Running → Stopped → Removed │
│    ↑         ↓         ↑                │
│    └─────────┴─────────┘                │
│       docker restart                    │
│                                         │
└─────────────────────────────────────────┘

States:
- Created: Image pulled, not yet started
- Running: Actively executing
- Paused: Frozen, can be resumed
- Stopped: Exited, can be restarted
- Removed: Deleted from the system
```

### Essential Container Commands

```bash
# Run a container
docker run [OPTIONS] IMAGE [COMMAND]

# List running containers
docker ps
docker ps -a  # Include stopped containers

# Start/stop/restart containers
docker start mycontainer
docker stop mycontainer      # Graceful shutdown
docker kill mycontainer      # Forceful shutdown
docker restart mycontainer

# Remove a container
docker rm mycontainer
docker rm $(docker ps -aq)   # Remove all stopped containers

# View container logs
docker logs mycontainer
docker logs -f mycontainer   # Follow
docker logs --tail 50 mycontainer  # Last 50 lines

# Access container shell
docker exec -it mycontainer bash
docker exec -it mycontainer sh

# View container resource usage
docker stats mycontainer

# View running processes inside container
docker top mycontainer

# Inspect container details
docker inspect mycontainer

# Copy files to/from container
docker cp mycontainer:/app/output.txt ./
docker cp ./input.txt mycontainer:/app/

# View container changes
docker diff mycontainer
```

### Running Commands in Containers

```bash
# Interactive terminal
docker run -it ubuntu:22.04 bash
# -i = interactive
# -t = allocate pseudo-terminal

# Run command and exit
docker run ubuntu:22.04 echo "Hello World"

# Run with environment variables
docker run -e DATABASE_URL="postgres://..." myapp:1.0

# Run with volumes
docker run -v /host/path:/container/path myapp:1.0

# Run with port mapping
docker run -p 8080:80 nginx:latest

# Run with resource limits
docker run -m 512m --cpus="0.5" myapp:1.0
# -m = max memory (512 MB)
# --cpus = CPU cores (0.5 = half of one core)

# Run in background (detached)
docker run -d myapp:1.0

# Run with automatic restart
docker run -d --restart always myapp:1.0
# Options: no, always, on-failure, unless-stopped
```

### Container Naming & IDs

```bash
# Every container has an ID and optional name
docker run -d --name web-server nginx:latest

# Reference by name or ID
docker stop web-server              # By name
docker stop a3f5c8d2e9b7            # By ID (or partial)

# Rename container
docker rename web-server production-web

# Give multiple names (aliases)
docker run -d --name db --network app-net postgres:13
docker network connect app-net redis-server
```

---

## Docker Networking

### Bridge Network (Default)

When you `docker run`, containers connect to a **bridge network** by default. They can communicate with each other by container name.

```
┌─────────────────────────────────────────┐
│      Bridge Network (docker0)           │
├─────────────────────────────────────────┤
│                                         │
│  Container 1          Container 2       │
│  (app)                (database)        │
│  IP: 172.17.0.2       IP: 172.17.0.3   │
│       ↓                     ↓            │
│       └─────→ docker0 ←────┘            │
│              (bridge)                   │
│       ↓ (port mapping)                  │
│    Host Network                         │
│    (can reach at localhost/127.0.0.1)   │
│                                         │
└─────────────────────────────────────────┘
```

### Network Types

```bash
# List networks
docker network ls

# Bridge (default) - containers on same machine communicate
docker network create my-bridge
docker run --network my-bridge myapp:1.0

# Host - container shares host's network stack
docker run --network host myapp:1.0
# Fastest, but less isolation

# None - no networking
docker run --network none myapp:1.0
# Completely isolated

# Overlay - cross-machine communication (Swarm/Kubernetes)
docker network create --driver overlay my-overlay
```

### Service Discovery

Containers can reach each other by **name** on the same network:

```dockerfile
FROM python:3.11

# In Python code:
# import requests
# response = requests.get("http://postgres-db:5432")

# Container name 'postgres-db' is automatically resolvable
# Docker's embedded DNS (127.0.0.11:53) handles this
```

### Port Mapping

```bash
# Map a single port
docker run -p 8080:80 nginx:latest
# Host port 8080 → Container port 80

# Map multiple ports
docker run -p 3000:3000 -p 5432:5432 myapp:1.0

# Map to random host port
docker run -p 80 nginx:latest
# Shows which random port in docker ps

# Map specific IP
docker run -p 127.0.0.1:8080:80 nginx:latest
# Only accessible from localhost

# Publish all EXPOSE ports
docker run -P myapp:1.0
# Maps all EXPOSE ports to random high ports
```

### Common Networking Setup

```bash
# Create a network
docker network create app-network

# Run services on that network
docker run -d --name app --network app-network myapp:1.0
docker run -d --name db --network app-network postgres:13

# Services can now reach each other by name
# In app, connect to: "postgres://db:5432/mydb"
```

---

## Docker Volumes

### The Problem Volumes Solve

By default, container data is **ephemeral** — when you delete the container, all files are gone. Volumes provide **persistent storage**.

```
Without volumes:
Container → Data lost
   ↓
Delete container → All data gone forever ❌

With volumes:
Container → Data saved to host
   ↓
Delete container → Data still exists ✓
   ↓
New container → Data restored
```

### Types of Mounts

| Type           | Use Case                             | Storage Location        |
| -------------- | ------------------------------------ | ----------------------- |
| **Bind Mount** | Mount a host directory               | Host filesystem         |
| **Volume**     | Persistent storage managed by Docker | Docker's data directory |
| **tmpfs**      | Temporary, in-memory storage         | RAM (lost on restart)   |

### Bind Mounts

Direct mapping between host and container directories:

```bash
# Syntax: -v /host/path:/container/path

# Mount a directory
docker run -v /home/user/app:/app myapp:1.0
# Host /home/user/app ← → Container /app (shared)

# Read-only mount
docker run -v /home/user/config:/app/config:ro myapp:1.0
# Container can read but not modify

# Common use: live code editing
docker run -v $(pwd):/app -it python:3.11 bash
# Your current directory synced into container in real-time
```

### Named Volumes

Docker-managed volumes with names:

```bash
# Create a volume
docker volume create my-data

# Use the volume
docker run -v my-data:/data myapp:1.0

# List volumes
docker volume ls

# Inspect volume details
docker volume inspect my-data

# Remove volume
docker volume rm my-data

# Remove all unused volumes
docker volume prune

# Backup a volume
docker run -v my-data:/data -v $(pwd):/backup \
  alpine tar czf /backup/data.tar.gz /data

# Restore a volume
docker run -v my-data:/data -v $(pwd):/backup \
  alpine tar xzf /backup/data.tar.gz -C /
```

### Multi-Container with Volumes

```bash
# Database container with persistent volume
docker run -d \
  --name postgres \
  -v postgres-data:/var/lib/postgresql/data \
  postgres:13

# App container that connects to database
docker run -d \
  --name app \
  --link postgres \
  myapp:1.0

# Even if both containers are deleted, data persists
docker rm postgres app

# New containers can access the same data
docker run -d \
  --name postgres-new \
  -v postgres-data:/var/lib/postgresql/data \
  postgres:13
```

### Volume Permissions

```bash
# Run with specific user ID
docker run -u 1000:1000 -v my-data:/data myapp:1.0

# Mount with specific permissions
docker run -v my-data:/data:z myapp:1.0
# z = shared, shared SELinux context
# Z = private, unique SELinux context
```

---

## Docker Compose Basics

### Why Docker Compose?

Running multiple containers with individual `docker run` commands gets messy:

```bash
# Without Compose (painful)
docker run -d -p 3000:3000 -v app-code:/app --name web myapp:1.0
docker run -d -p 5432:5432 -v postgres-data:/var/lib/postgresql/data \
  -e POSTGRES_PASSWORD=secret --name db postgres:13
docker run -d -p 6379:6379 -v redis-data:/data --name cache redis:7

# With Compose (clean, declarative)
docker-compose up -d
```

**Docker Compose** describes your entire stack in one YAML file.

### Basic docker-compose.yml

```yaml
version: "3.9" # File format version

services:
  web: # Service name
    image: myapp:1.0 # Image to use
    ports:
      - "3000:3000" # Port mapping
    volumes:
      - ./app:/app # Bind mount
    environment:
      - DEBUG=true
    depends_on:
      - db # Start db first

  db:
    image: postgres:13
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data # Named volume
    environment:
      - POSTGRES_PASSWORD=secret
    restart: always # Always restart if it stops

volumes:
  postgres-data: # Define named volumes

networks:
  default: # Services auto-connect to default network
    driver: bridge
```

### Running Docker Compose

```bash
# Start services in background
docker-compose up -d

# View status
docker-compose ps

# View logs
docker-compose logs
docker-compose logs -f web      # Follow web service logs

# Stop services
docker-compose stop

# Stop and remove containers (volumes persist)
docker-compose down

# Restart a service
docker-compose restart web

# Execute command in a service
docker-compose exec db psql -U postgres

# View resource usage
docker-compose stats
```

### Environment Variables

```yaml
# Method 1: In compose file
services:
  app:
    environment:
      - DATABASE_URL=postgres://db:5432/mydb
      - DEBUG=true

# Method 2: From .env file (auto-loaded)
# .env file:
# DATABASE_PASSWORD=secret
# API_KEY=abc123

services:
  app:
    environment:
      - DATABASE_PASSWORD=${DATABASE_PASSWORD}
      - API_KEY=${API_KEY}

# Method 3: Separate env file
services:
  app:
    env_file: .env.production
```

### Custom Networks

```yaml
version: "3.9"

services:
  web:
    image: myapp:1.0
    networks:
      - frontend # Connect to frontend network
      - backend # Also connect to backend network

  api:
    image: api:1.0
    networks:
      - backend # Only backend network

networks:
  frontend:
    driver: bridge
  backend:
    driver: bridge

# Result: web ←→ api (communicate)
#         web ←→ frontend (web is accessible here)
#         api ←→ backend (api is accessible here)
#         web cannot reach frontend from backend perspective
```

---

## Docker Compose Advanced

### Override Default Command

```yaml
services:
  app:
    image: python:3.11
    command: python manage.py runserver 0.0.0.0:8000
    # Overrides any CMD in the image

  worker:
    image: python:3.11
    entrypoint: python # Override ENTRYPOINT
    command: manage.py celery # Pass arguments

  # Run arbitrary command once
  migration:
    image: python:3.11
    command: python manage.py migrate
    profiles: ["setup"] # docker-compose --profile setup up
```

### Build from Dockerfile

```yaml
services:
  app:
    build:
      context: ./app # Directory with Dockerfile
      dockerfile: Dockerfile # (optional, defaults to 'Dockerfile')
      args:
        - BUILD_DATE=2026-07-15 # Pass build args
    image: myapp:latest # Tag the built image
    ports:
      - "8000:8000"

  # Shorthand
  web:
    build: ./web # Assumes Dockerfile in ./web
```

### Healthchecks

```yaml
services:
  app:
    image: myapp:1.0
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8000/health"]
      interval: 30s # Check every 30 seconds
      timeout: 5s # Wait 5 seconds for response
      retries: 3 # Mark unhealthy after 3 failures
      start_period: 40s # Wait before first check
    restart: on-failure # Restart if unhealthy

  db:
    image: postgres:13
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
```

### Resource Limits

```yaml
services:
  app:
    image: myapp:1.0
    deploy:
      resources:
        limits:
          cpus: "0.5" # Max 0.5 CPU cores
          memory: 512M # Max 512 MB RAM
        reservations:
          cpus: "0.25" # Guaranteed 0.25 cores
          memory: 256M # Guaranteed 256 MB RAM
    restart: on-failure:5 # Restart max 5 times
```

### Logging

```yaml
services:
  app:
    image: myapp:1.0
    logging:
      driver: "json-file" # Default
      options:
        max-size: "10m" # Max file size
        max-file: "3" # Keep 3 files


    # Alternative drivers
    # driver: "syslog"            # Send to syslog
    # driver: "awslogs"           # AWS CloudWatch
    # driver: "gelf"              # ELK stack
```

### Profiles (Run Specific Services)

```yaml
services:
  web:
    image: myapp:1.0
    # No profile = always runs

  db:
    image: postgres:13
    profiles: ["database"] # Only runs with --profile database

  backup:
    image: backup-tool:1.0
    profiles: ["maintenance"] # Only runs with --profile maintenance

  cache:
    image: redis:7
    profiles: ["cache", "optimize"] # Multiple profiles
```

```bash
# Run without profiles (only web)
docker-compose up

# Run with specific profile
docker-compose --profile database up -d

# Run with multiple profiles
docker-compose --profile database --profile maintenance up -d
```

### Extending Services (compose overrides)

```yaml
# docker-compose.yml
services:
  app:
    image: myapp:1.0
    environment:
      - DEBUG=false

# docker-compose.override.yml (auto-loaded in dev)
services:
  app:
    environment:
      - DEBUG=true              # Override DEBUG
    volumes:
      - ./src:/app/src          # Add dev volume
    ports:
      - "8000:8000"             # Expose port in dev

# With profile for production
# docker-compose.prod.yml
services:
  app:
    image: myapp:1.0
    environment:
      - DEBUG=false
      - LOG_LEVEL=error
    restart: always

# Use in production
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

---

## Best Practices

### 1. Image Building

```dockerfile
# ❌ Bad: Large image, many layers, cached layers invalidated
FROM ubuntu:22.04
RUN apt-get update && apt-get install -y curl wget git python3
COPY . /app
RUN cd /app && python3 setup.py install

# ✓ Good: Minimal, efficient, layers cached
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
CMD ["python", "app.py"]
```

### 2. Use .dockerignore

```
# .dockerignore - prevent large files from being copied
node_modules
__pycache__
.git
.env
*.log
dist/
build/
.pytest_cache
.coverage
```

### 3. Multi-stage Builds (reduce final image size)

```dockerfile
# Stage 1: Build
FROM golang:1.19 AS builder
WORKDIR /app
COPY . .
RUN go build -o app .

# Stage 2: Runtime (much smaller)
FROM alpine:3.17
COPY --from=builder /app/app /app
CMD ["/app"]

# Final image only contains the binary, not the Go compiler
```

### 4. Container Security

```dockerfile
# ❌ Bad: Runs as root
FROM ubuntu:22.04
RUN apt-get install -y myapp
CMD ["myapp"]

# ✓ Good: Creates non-root user
FROM ubuntu:22.04
RUN apt-get install -y myapp && \
    useradd -m -u 1000 appuser
USER appuser
CMD ["myapp"]
```

```bash
# Run container with security options
docker run --read-only myapp:1.0           # Read-only root
docker run --cap-drop ALL myapp:1.0        # Drop all capabilities
docker run --security-opt no-new-privileges myapp:1.0
```

### 5. Healthchecks

Always define what "healthy" means:

```dockerfile
FROM python:3.11

WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD python -c "import requests; requests.get('http://localhost:8000/health')"

CMD ["python", "app.py"]
```

### 6. Restart Policies

```bash
# No restart (container exits when it fails)
docker run --restart no myapp:1.0

# Always restart (unless explicitly stopped)
docker run --restart unless-stopped myapp:1.0

# Restart with max retries
docker run --restart on-failure:5 myapp:1.0

# Recommended for production services
docker-compose:
  services:
    app:
      restart: unless-stopped
```

### 7. Logging

```bash
# Check logs to debug issues
docker logs container-name              # View all logs
docker logs -f container-name           # Follow
docker logs --tail 100 container-name   # Last 100 lines
docker logs --since 2h container-name   # Last 2 hours

# Use structured logging in your app
# Instead of: print("User logged in")
# Use:        logger.info({"event": "login", "user_id": 123})
```

### 8. Resource Limits (prevent runaway containers)

```bash
# Limit memory
docker run -m 512m myapp:1.0

# Limit CPU
docker run --cpus="1.5" myapp:1.0

# Docker Compose
services:
  app:
    deploy:
      resources:
        limits:
          memory: 512M
          cpus: '1.5'
```

### 9. Use Specific Image Tags

```bash
# ❌ Bad: 'latest' can change unexpectedly
docker run myapp:latest

# ✓ Good: Pin to specific version
docker run myapp:1.2.3

# Docker Compose
services:
  app:
    image: myapp:1.2.3       # Not 'latest'
```

### 10. Clean Up Regularly

```bash
# Remove stopped containers
docker container prune

# Remove dangling images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything unused
docker system prune --volumes
```

---

## Real-World Examples

### Example 1: Simple Web App (Python Flask + PostgreSQL)

```yaml
version: "3.9"

services:
  web:
    build: ./app
    ports:
      - "5000:5000"
    volumes:
      - ./app:/app # Live editing in dev
    environment:
      - DATABASE_URL=postgresql://user:password@db:5432/mydb
      - DEBUG=true
    depends_on:
      - db
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:5000/"]
      interval: 30s
      timeout: 10s
      retries: 3

  db:
    image: postgres:15
    volumes:
      - postgres-data:/var/lib/postgresql/data
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=password
      - POSTGRES_DB=mydb
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U user"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres-data:

# Run it
# docker-compose up
# Visit http://localhost:5000
```

### Example 2: Full Stack (React + Node + MongoDB)

```yaml
version: "3.9"

services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    volumes:
      - ./frontend/src:/app/src
    environment:
      - REACT_APP_API_URL=http://localhost:5000
    depends_on:
      - api
    networks:
      - app-network

  api:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "5000:5000"
    volumes:
      - ./backend/src:/app/src
    environment:
      - MONGODB_URL=mongodb://db:27017/mydb
      - NODE_ENV=development
    depends_on:
      - db
    networks:
      - app-network
    restart: unless-stopped

  db:
    image: mongo:6.0
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db
    networks:
      - app-network
    restart: unless-stopped

volumes:
  mongo-data:

networks:
  app-network:
    driver: bridge
```

### Example 3: Your Pi-hole + Tailscale Setup

```yaml
version: "3.9"

services:
  tailscale:
    image: tailscale/tailscale:latest
    container_name: pihole-tailscale
    hostname: pihole
    environment:
      - TS_AUTHKEY=${TS_AUTHKEY}
      - TS_STATE_DIR=/var/lib/tailscale
      - TS_USERSPACE=false
      - TS_ACCEPT_DNS=false
      - TS_EXTRA_ARGS=--advertise-exit-node
      - TS_DEBUG_FIREWALL_MODE=nftables
    sysctls:
      - net.ipv4.ip_forward=1
      - net.ipv6.conf.all.forwarding=1
    ports:
      - "53:53/tcp"
      - "53:53/udp"
    volumes:
      - tailscale-data:/var/lib/tailscale
      - /dev/net/tun:/dev/net/tun
    devices:
      - /dev/net/tun:/dev/net/tun
    cap_add:
      - NET_ADMIN
      - NET_RAW
      - SYS_MODULE
    restart: unless-stopped

  pihole:
    image: pihole/pihole:latest
    container_name: pihole
    network_mode: service:tailscale
    depends_on:
      - tailscale
    environment:
      - TZ=Europe/Lisbon
      - FTLCONF_webserver_api_password=${PIHOLE_PASSWORD}
      - FTLCONF_dns_listeningMode=ALL
    volumes:
      - ./etc-pihole:/etc/pihole
      - ./etc-dnsmasq.d:/etc/dnsmasq.d
    cap_add:
      - NET_ADMIN
    restart: unless-stopped

volumes:
  tailscale-data:
```

---

## Troubleshooting

### Container Won't Start

```bash
# Check logs
docker logs mycontainer

# Inspect container config
docker inspect mycontainer

# Common causes:
# 1. Port already in use
sudo lsof -i :8080
docker port mycontainer

# 2. Out of disk space
docker system df

# 3. Resource limits exceeded
docker stats

# 4. Missing dependencies
docker run -it myimage:1.0 bash  # Debug interactively
```

### Networking Issues

```bash
# Check if containers can reach each other
docker exec app ping db        # From app, ping db

# Check DNS
docker exec app nslookup db    # Test DNS resolution

# Check network
docker network inspect app-network

# Common fixes:
# - Make sure services are on same network
# - Check service names (container names used as hostnames)
# - Ensure ports are exposed, not just mapped
```

### Volume Issues

```bash
# Container not seeing files
docker volume ls
docker volume inspect myvolume

# Check permissions
docker exec mycontainer ls -la /data

# Fix permissions
docker exec mycontainer chown -R 1000:1000 /data

# Backup before deleting
docker run -v myvolume:/data -v $(pwd):/backup \
  alpine tar czf /backup/backup.tar.gz /data
```

### Performance Issues

```bash
# Check resource usage
docker stats

# Check for runaway processes
docker top mycontainer

# Increase resource limits
docker update -m 2g --cpus="2" mycontainer

# Check disk usage
docker system df
docker image prune
docker volume prune
```

### Docker Compose Issues

```bash
# Check if compose file is valid
docker-compose config

# See what's actually running
docker-compose ps

# Check service logs
docker-compose logs service-name

# Check dependencies are running
docker-compose ps | grep Up

# Rebuild from scratch
docker-compose down -v
docker-compose up --build
```

---

## Quick Reference Cheat Sheet

### Most Used Commands

```bash
# Building
docker build -t myapp:1.0 .
docker build -t myapp:1.0 -f Dockerfile.prod .

# Running
docker run -d -p 8080:80 --name web nginx:latest
docker run -it myapp:1.0 bash
docker run -v /host:/container myapp:1.0

# Managing
docker ps
docker ps -a
docker stop mycontainer
docker rm mycontainer
docker logs -f mycontainer

# Images
docker pull ubuntu:22.04
docker images
docker rmi myapp:1.0
docker tag myapp:1.0 registry.com/myapp:1.0

# Compose
docker-compose up -d
docker-compose down
docker-compose logs -f
docker-compose exec service bash
```

---

## Next Steps

1. **Practice with small projects** — Start with simple Dockerfiles
2. **Learn Docker networking** — Understand bridge networks and service discovery
3. **Master Docker Compose** — Most practical for multi-container development
4. **Explore registries** — Push to Docker Hub or private registries
5. **Study orchestration** — Docker Swarm or Kubernetes for production

---

## Resources

- [Docker Official Docs](https://docs.docker.com/)
- [Dockerfile Reference](https://docs.docker.com/engine/reference/builder/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [Docker Hub](https://hub.docker.com/) — Pre-built images
- [Best Practices](https://docs.docker.com/develop/dev-best-practices/)
