# 🐙 Git, GitHub & CI/CD Workflows

---

## 📑 Table of Contents

1. [Git Terminology & Core Concepts](#1-git-terminology--core-concepts)
2. [Levels of Git Architecture](#2-levels-of-git-architecture)
3. [Git Approaches: Local vs Remote](#3-git-approaches-local-vs-remote)
4. [GitHub Fundamentals & Core Workflow](#4-github-fundamentals--core-workflow)
5. [Types of Branching Strategies](#5-types-of-branching-strategies)
6. [Working with Branches: Commands & Conventions](#6-working-with-branches-commands--conventions)
7. [Commits and Commit Messages](#7-commits-and-commit-messages)
8. [Pull Requests (PRs) & Templates](#8-pull-requests-prs--templates)
9. [Git Merge Models & Conflict Resolution](#9-git-merge-models--conflict-resolution)
10. [GitHub Actions & Automation Models](#10-github-actions--automation-models)
11. [GitHub Packages](#11-github-packages)
12. [Advanced GitHub Actions: Exam Focus](#12-advanced-github-actions-exam-focus)

---

## 1. Git Terminology & Core Concepts 🧠

### 1.1 What is Git, Really?

**Git** is a **distributed version control system (VCS)** that tracks changes to your code over time. It allows teams to record history, revert to previous states if something breaks, work on features in parallel, and collaborate safely.

```text
Without Git: 📁                           With Git: 🌿
project_v1.py                              project.py (full history accessible
project_v2.py                                          via `git log`)
project_final_FINAL_ACTUALLY_WORKS.py
```

### 1.2 Core Components 📦

- **Repository (Repo):** A storage folder tracked by Git containing your project files, metadata, and the internal `.git/` database.
- **Commit:** An immutable snapshot of your project at a specific point in time, identified by a SHA-1 hash, author details, and a parent commit pointer.
- **Branch:** An isolated, separate line of code development running in parallel to the main codebase.
- **HEAD:** A pointer referencing your active branch or current location/commit in the Git tree.

```text
┌───────────────────────────────────────────────┐
│  Git (Distributed Version Control System) 🛠️  │
│  ┌──────────────────────────────────────────┐ │
│  │  Repository (.git database & metadata)   │ │
│  │  ┌───────────────────────────────────┐   │ │
│  │  │  Commits & Branches (HEAD)        │   │ │
│  │  │  (Chain of historical snapshots)  │   │ │
│  │  └───────────────────────────────────┘   │ │
│  └──────────────────────────────────────────┘ │
└───────────────────────────────────────────────┘

```

---

## 2. Levels of Git Architecture 🏗️

Git organizes file changes across distinct stages locally before syncing with remote servers:

```text
┌──────────────────────────────────────────────────────┐
│  REMOTE REPOSITORY (e.g., GitHub) ☁️                 │
│  "The cloud host"                                    │
│  Central server storing shared history, PRs, and     │
│  triggering CI/CD pipelines                          │
├──────────────────────────────────────────────────────┤
│  LOCAL REPOSITORY (.git/ folder) 💾                  │
│  "The database"                                      │
│  Stores committed snapshots, local branches, and     │
│  historical metadata on disk                         │
├──────────────────────────────────────────────────────┤
│  STAGING AREA (Index) & WORKING DIRECTORY 🛠️          │
│  "The active workspace"                              │
│  Working dir holds active edits; Staging area        │
│  prepares specific changes for the next commit       │
└──────────────────────────────────────────────────────┘

```

| Operational Area            | Description                                                        | Command / Action           |
| --------------------------- | ------------------------------------------------------------------ | -------------------------- |
| **Working Directory** 💻    | Your active workspace files with uncommitted edits.                | Edit files in IDE / editor |
| **Staging Area (Index)** ⏳ | Holds prepared file edits staged for the upcoming commit snapshot. | `git add <file>`           |
| **Local Repository** 💾     | Local `.git/` database containing committed historical snapshots.  | `git commit -m "msg"`      |
| **Remote Repository** ☁️    | Cloud server (GitHub) storing shared code history.                 | `git push` / `git pull`    |

> 💡 **Key insight:** Git commands like `git add` and `git commit` operate purely on local disk structures. No network traffic occurs until you explicitly run `git push` or `git pull`.

---

## 3. Git Approaches: Local vs Remote 🔄

```text
              Git Operations ⚙️
             (both track history)
                    │
        ┌───────────┴───────────┐
        │                       │
     Local Git 💻           Remote Git ☁️
        │                       │
  Tracks workspace        Synchronizes local
  edits in a local        commits with cloud
  `.git` database         platforms (GitHub)
        │                       │
  Trained to:             Trained to:
  RECORD & STAGE          SHARE & AUTOMATE
  changes locally         PRs, CI/CD, and builds

```

| Aspect           | Local Git 💻                                       | Remote Git (GitHub) ☁️                             |
| ---------------- | -------------------------------------------------- | -------------------------------------------------- |
| **Goal**         | Track local edits and stage commits.               | Host shared code, facilitate reviews, run CI/CD.   |
| **Scope**        | Single computer / machine.                         | Centralized cloud server.                          |
| **Commands**     | `git add`, `git commit`, `git status`, `git diff`. | `git push`, `git pull`, `git clone`, `git fetch`.  |
| **Key Features** | Fast offline operations, instant branching.        | PRs, Code Reviews, Issue Trackers, GitHub Actions. |

---

## 4. GitHub Fundamentals & Core Workflow ⚡

### 4.1 GitHub vs Git

- 🛠️ **Git:** Version control command-line software running on your computer.
- 🌐 **GitHub:** Cloud-based hosting service providing visual collaboration tools on top of Git repositories.

### 4.2 Standard 9-Step GitHub Workflow 🚀

```text
1. Clone 📥     ──► git clone [https://github.com/user/project.git](https://github.com/user/project.git)
2. Branch 🌿    ──► git checkout -b feature/add-login
3. Edit ✏️      ──► Edit code files in your editor
4. Commit 💾    ──► git add . && git commit -m "feat: add login"
5. Push 📤      ──► git push origin feature/add-login
6. Open PR 🔀   ──► Open Pull Request on GitHub for team review
7. Review 👀    ──► Discuss changes and address review comments
8. Merge 🔀     ──► Merge approved PR into main branch
9. Cleanup 🧹   ──► Delete feature branch locally and remotely

```

---

## 5. Types of Branching Strategies 🌲

```text
┌───────────────────────────────────────────────────────────┐
│              Types of Branching Strategies 🌿             │
├───────────────────────────────────────────────────────────┤
│                                                           │
│  GIT FLOW 🌊          GITHUB FLOW 🚀      TRUNK-BASED ⚡  │
│  ──────────           ───────────         ───────────     │
│  Complex structure    Simple model        Short-lived     │
│  with main, develop,  with main and       branches merged │
│  release, hotfix      feature branches    rapidly (1-2    │
│  branches             deploying directly  days max)       │
│                                                           │
└───────────────────────────────────────────────────────────┘

```

### 5.1 Strategy Deep Dive & Commands

#### Git Flow 🌊 (Complex, versioned releases)

Branching model using distinct long-lived `develop` and `main` branches alongside temporary `feature/*`, `release/*`, and `hotfix/*` branches.

```bash
# Start feature off develop
git checkout develop && git pull origin develop
git checkout -b feature/add-payment

# Merge feature to develop
git checkout develop && git merge --no-ff feature/add-payment

# Release to production
git checkout -b release/1.2.0 develop
git checkout main && git merge --no-ff release/1.2.0 && git tag 1.2.0

```

#### GitHub Flow 🚀 (Simple, continuous deployment)

Simple branching strategy centered around a deployable `main` branch with short-lived feature branches that merge directly via PRs.

```bash
git checkout main && git pull origin main
git checkout -b feature/add-notifications
# Commit and push
git push origin feature/add-notifications
# Merge via PR on GitHub and deploy immediately

```

#### Trunk-Based Development ⚡ (High velocity)

High-velocity strategy where developers work on very short-lived branches (hours to 1-2 days max) that merge frequently into `main`, or commit directly using feature flags.

### 5.2 Branching Comparison 📊

| Aspect               | Git Flow 🌊                                               | GitHub Flow 🚀                       | Trunk-Based ⚡                           |
| -------------------- | --------------------------------------------------------- | ------------------------------------ | ---------------------------------------- |
| **Branches**         | Many (`main`, `develop`, `release`, `hotfix`, `feature`). | Few (`main`, `feature/*`).           | Minimal (`main`, short-lived features).  |
| **Branch Lifespan**  | Long (weeks/months).                                      | Medium (days).                       | Short (hours/1-2 days).                  |
| **Release Strategy** | Scheduled version releases (v1.0, v2.0).                  | Continuous deployment.               | Continuous integration / fast iteration. |
| **Best For**         | Large enterprise apps with releases.                      | Web applications, SaaS, agile teams. | Startups, DevOps, rapid development.     |

---

## 6. Working with Branches: Commands & Conventions 🌿

### 6.1 Essential Branching Commands 💻

```bash
# Branch Inspection & Switching 🔍
git branch                          # List local branches
git branch -a                       # List local and remote branches
git checkout -b feature/add-docker  # Create and switch to new branch
git switch -c feature/add-docker    # Modern syntax (Git 2.23+)

# Renaming & Deleting 🧹
git branch -m feature/old feature/new           # Rename current branch
git branch -d feature/add-docker               # Safe delete (requires merge)
git branch -D feature/add-docker               # Force delete unmerged branch
git push origin --delete feature/add-docker    # Delete remote branch

# Diffing & Status Inspection 📊
git status                          # Unstaged / staged status
git diff                            # View unstaged changes
git diff main..feature              # Compare branches
git log --oneline --graph --all     # Visual branch history tree

```

### 6.2 Standardized Naming Conventions 🏷️

- `feature/add-payment-system` ✨ — New functionality.
- `bugfix/fix-login-timeout` 🐛 — Non-critical bug fixes.
- `hotfix/critical-security-patch` 🚑 — Urgent production patches.
- `docs/update-installation-guide` 📝 — Documentation improvements.
- `refactor/simplify-auth-logic` 🛠️ — Code restructuring without feature changes.
- `infra/setup-github-actions` ⚙️ — CI/CD or infrastructure tasks.

---

## 7. Commits and Commit Messages 💬

### 7.1 Conventional Commits Specification 📜

Standardized specification for formatted commit messages using the layout: `<type>(<scope>): <subject>`.

```text
Types:
- feat: ✨ New feature                - refactor: 🛠️ Code restructuring
- fix: 🐛 Bug fix                     - perf: ⚡ Performance boost
- docs: 📝 Documentation changes      - test: 🧪 Adding/updating tests
- style: 🎨 Formatting changes        - ci: ⚙️ CI/CD configuration updates

```

**Practical Commit Examples:**

```bash
# Concise commits 🎯
git commit -m "feat(auth): add OAuth 2.0 Google login"
git commit -m "fix(api): resolve 500 error on empty payload"

# Multi-line detailed commit 📝
git commit -m "feat(auth): implement OAuth 2.0 integration

- Add Google and GitHub OAuth providers
- Update user session management and scopes

Closes #123"

```

---

## 8. Pull Requests (PRs) & Templates 🔀

### 8.1 What is a Pull Request? 🤔

A **Pull Request (PR)** is a GitHub platform tool for reviewing, discussing, and merging branch changes before target integration.

### 8.2 Pull Request Standard Template 📋

Place this inside `.github/pull_request_template.md` in your repository:

```markdown
## Description 📝

Brief summary of changes.

## Related Issue 🔗

Closes #123

## Type of Change 🏷️

- [ ] 🐛 Bug fix
- [ ] ✨ New feature
- [ ] 💥 Breaking change
- [ ] 📝 Documentation update

## Checklist ✅

- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Tests added/passing
```

---

## 9. Git Merge Models & Conflict Resolution 🔀

### 9.1 Merge Strategies 🛤️

```text
Git Merge Models
        │
        ├── 🌿 Explicit Merge Commit
        │   `git merge feature` — preserves complete branch graph
        │
        ├── 🎯 Rebase & Merge
        │   `git rebase main` — rewrites commits onto main for linear history
        │
        └── 📦 Squash Merge
            `git merge --squash` — condenses feature commits into one snapshot

```

- **Explicit Merge:** Preserves complete branch history by creating a dedicated merge commit.
- **Rebase:** Re-applies commits on top of another base tip to create a linear history.
- **Squash Merge:** Condenses multiple branch commits into a single unified commit snapshot on target.

### 9.2 Resolving Merge Conflicts Step-by-Step ⚠️

A **Merge Conflict** is an issue that occurs when concurrent branch edits overlap on the exact same lines of code.

```bash
# 1. Trigger merge 🔀
git merge feature/add-docker

# 2. Inspect conflict markers inside affected files 🔍
<<<<<<< HEAD
x = 1  # Your current version
=======
x = 2  # Incoming branch version
>>>>>>> feature/add-docker

# 3. Edit file to resolve desired state, then stage ✏️
git add app.py

# 4. Finalize merge 🎉
git commit -m "fix: resolve merge conflict in app.py"

```

---

## 10. GitHub Actions & Automation Models 🤖

**GitHub Actions** is a built-in GitHub platform for creating automated CI/CD workflows using `.yml` files stored in `.github/workflows/`.

```text
Workflow Trigger ⚡ (push / pull_request)
               │
               ▼
     [ Workflow (.yml) ] 📜
               │
         ┌─────┴─────┐
         ▼           ▼
      [Job 1]     [Job 2]   ──► Executed on Runners (Ubuntu / Self-Hosted) 🖥️
         │
      [Steps] ──► Runs Actions or Shell Commands (`run: npm test`) 🛠️

```

```yaml
name: PR Build & Package APKs # <--- Freeform display name for the workflow

on: # <--- Fixed keyword (Trigger configuration)
  pull_request: # <--- Fixed keyword (Event type)
    branches: ["main"]

jobs: # <--- Fixed keyword (Start of jobs container)
  build-apk: # <--- YOUR CUSTOM JOB ID (Used in dependency chains like `needs: build-apk`)
    name: Build Android APK # <--- Freeform display name shown in the GitHub UI
    runs-on: ubuntu-latest # <--- Fixed keyword + runner image target

    steps: # <--- Fixed keyword (Start of steps list)
      - name: Checkout Code # <--- Freeform display name for Step 1
        uses: actions/checkout@v4

      - name: Compile App # <--- Freeform display name for Step 2
        run: ./gradlew assembleDebug
```

### 10.1 Concepts 🧩

- **Workflow:** A configured, automated process defined in `.github/workflows/*.yml`.
- **Job:** A set of pipeline steps executed together on a specified runner machine.
- **Runner:** A host server (ephemeral GitHub-hosted VM or custom self-hosted server) running workflow jobs.

#### Action Types & Runner Architecture 🏛️

| Component           | Description                             | Options / Types                                                                            |
| ------------------- | --------------------------------------- | ------------------------------------------------------------------------------------------ |
| **Runners** 🖥️      | Host servers executing workflow jobs.   | **GitHub-hosted** (ephemeral VMs) or **Self-hosted** (custom servers).                     |
| **Action Types** ⚙️ | Packaged executable tasks.              | **JavaScript** (host-native), **Container** (Linux Docker), **Composite** (bundled steps). |
| **Triggers** ⚡     | Repository events activating workflows. | `push`, `pull_request`, `schedule`, `workflow_dispatch`.                                   |

#### Reserved Keywords 🔑

| Keyword           | Level / Scope     | Primary Purpose & Use Case                                                                                   |
| ----------------- | ----------------- | ------------------------------------------------------------------------------------------------------------ |
| **`name`**        | Top-level or Step | Assigns a human-readable display name for the overall workflow or an individual step.                        |
| **`run-name`**    | Top-level         | Sets a dynamic title for a specific workflow execution run (can use context variables like commit message).  |
| **`on`**          | Top-level         | **Required**. Defines the events that trigger the workflow (e.g., `push`, `pull_request`, `schedule`).       |
| **`jobs`**        | Top-level         | **Required**. Encloses all individual jobs defined within the workflow.                                      |
| **`<job_id>`**    | Under `jobs`      | Custom unique key chosen by you to identify a specific job (e.g., `build-apk`, `unit-tests`).                |
| **`runs-on`**     | Job level         | **Required per job**. Defines the host operating system/runner VM (e.g., `ubuntu-latest`, `windows-latest`). |
| **`needs`**       | Job level         | Establishes job dependencies to control execution order (e.g., `needs: build-apk`).                          |
| **`steps`**       | Job level         | **Required per job**. An ordered list of sequential tasks executed on the runner.                            |
| **`uses`**        | Step level        | Specifies a pre-packaged action or reusable workflow to execute (e.g., `actions/checkout@v4`).               |
| **`run`**         | Step level        | Executes shell command-line scripts directly on the runner (e.g., `./gradlew test`).                         |
| **`with`**        | Step level        | Supplies input variables/parameters to an action referenced by `uses`.                                       |
| **`env`**         | Top, Job, or Step | Defines custom environment variables available to shell scripts or actions.                                  |
| **`id`**          | Step level        | Assigns a unique identifier to a step so its outputs can be referenced elsewhere.                            |
| **`if`**          | Job or Step       | Adds conditional logic to execute a job or step only when an expression evaluates to `true`.                 |
| **`strategy`**    | Job level         | Sets up build configurations, such as matrix combinations (`matrix`) or execution limits (`fail-fast`).      |
| **`concurrency`** | Top or Job level  | Manages concurrent runs to cancel in-progress builds or restrict simultaneous runs on a branch.              |
| **`outputs`**     | Job level         | Declares output parameters generated by a job to be passed downstream to other jobs.                         |
| **`permissions`** | Top or Job level  | Controls the access permissions granted to the default `GITHUB_TOKEN`.                                       |
| **`defaults`**    | Top or Job level  | Sets default configurations (like default `shell` or `working-directory`) across steps.                      |

---

### 10.2 Creating Workflows for CI 🧪

Continuous Integration (CI) in GitHub Actions automates building and testing code on every push or pull request.

#### Basic CI Pipeline Structure 🏗️

```yaml
name: Node.js CI Pipeline

on:
  push:
    branches: ["main", "develop"]
  pull_request:
    branches: ["main"]

jobs:
  build-and-test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Set up Node.js Runtime
        uses: actions/setup-node@v4
        with:
          node-version: "20"
          cache: "npm"

      - name: Install Dependencies
        run: npm ci

      - name: Run Linter
        run: npm run lint

      - name: Run Unit Tests
        run: npm test
```

#### Matrix Builds for Multi-Platform/Version Testing 🌐

A **matrix strategy** lets you run a single job across multiple OS environments and language versions simultaneously.

```yaml
jobs:
  test-matrix:
    runs-on: ${{ matrix.os }}
    strategy:
      fail-fast: false # Continues other matrix jobs even if one fails
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        node-version: [18.x, 20.x]
        exclude:
          - os: windows-latest
            node-version: 18.x # Excludes specific combination

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: ${{ matrix.node-version }}
      - run: npm test
```

#### GitHub Actions Trigger Categories ⚡

| Event Keyword             | Category          | Description                                                                                 |
| ------------------------- | ----------------- | ------------------------------------------------------------------------------------------- |
| **`push`**                | Code & Branch     | Triggers when commits are pushed to a repository branch or tag.                             |
| **`pull_request`**        | Code & Branch     | Triggers when a pull request is opened, updated (`synchronize`), merged, or closed.         |
| **`pull_request_target`** | Code & Branch     | Triggers on pull request activity, but runs securely in the context of the base repository. |
| **`create`**              | Code & Branch     | Triggers when a Git branch or tag is created.                                               |
| **`delete`**              | Code & Branch     | Triggers when a Git branch or tag is deleted.                                               |
| **`workflow_dispatch`**   | Manual & Dispatch | Allows manual execution from the GitHub UI, CLI, or API with optional custom parameters.    |
| **`repository_dispatch`** | Manual & Dispatch | Allows external third-party systems or webhooks to trigger a workflow via the GitHub API.   |
| **`schedule`**            | Scheduled         | Runs the workflow automatically at specific times or periodic intervals using cron syntax.  |
| **`issues`**              | Collaboration     | Triggers when an issue is opened, edited, labeled, assigned, or closed.                     |
| **`issue_comment`**       | Collaboration     | Triggers when a comment is created, edited, or deleted on an issue or pull request.         |
| **`discussion`**          | Collaboration     | Triggers when a GitHub Discussion is created, edited, answered, or deleted.                 |
| **`release`**             | Collaboration     | Triggers when a GitHub Release is created, published, edited, or deleted.                   |
| **`workflow_run`**        | Workflow Chaining | Triggers a downstream workflow automatically after an upstream workflow completes.          |
| **`workflow_call`**       | Workflow Chaining | Exposes a workflow as a modular, reusable component callable by other workflows.            |

#### Full Trigger Syntax Checklist ✅

```yaml
name: Master Trigger Checklist Workflow

on:
  # 1. CODE & BRANCH EVENTS 🌿
  push:
    branches:
      - main
      - "releases/**"
    branches-ignore:
      - "experimental/**"
    tags:
      - "v*.*.*"
    paths:
      - "src/**"
      - "package.json"
    paths-ignore:
      - "**.md"

  pull_request:
    types: [opened, synchronize, reopened, closed]
    branches:
      - main
    paths:
      - "src/**"

  pull_request_target:
    types: [opened, labeled]
    branches:
      - main

  create:
  delete:

  # 2. MANUAL & DISPATCH EVENTS 🎛️
  workflow_dispatch:
    inputs:
      target_env:
        description: "Environment to run against"
        required: true
        default: "staging"
        type: choice
        options:
          - dev
          - staging
          - production
      enable_debug:
        description: "Enable verbose logging"
        required: false
        type: boolean
        default: false

  repository_dispatch:
    types: [external-webhook-trigger, build-event]

  # 3. SCHEDULED EVENTS (CRON) ⏰
  schedule:
    - cron: "0 0 * * *" # Runs at 00:00 UTC every day
    - cron: "30 8 * * 1" # Runs every Monday at 08:30 UTC

  # 4. GITHUB COLLABORATION EVENTS 💬
  issues:
    types: [opened, edited, labeled]

  issue_comment:
    types: [created]

  discussion:
    types: [created, answered]

  release:
    types: [published, created]

  # 5. WORKFLOW CHAINING & REUSABILITY 🔗
  workflow_run:
    workflows: ["Build & Test CI"]
    types:
      - completed
    branches:
      - main

  workflow_call:
    inputs:
      config_path:
        required: true
        type: string
    secrets:
      AUTH_TOKEN:
        required: true

jobs:
  inspect-trigger:
    runs-on: ubuntu-latest
    steps:
      - name: Print Event Trigger Information
        run: |
          echo "Workflow triggered by event: ${{ github.event_name }}"
          echo "Triggered on branch/ref: ${{ github.ref }}"
          echo "Triggered by user: ${{ github.actor }}"

      - name: Handle Manual Dispatch Inputs
        if: github.event_name == 'workflow_dispatch'
        run: |
          echo "Target Environment: ${{ inputs.target_env }}"
          echo "Debug Enabled: ${{ inputs.enable_debug }}"
```

---

### 10.3 Managing & Debugging Workflows 🛠️

#### Workflow Management Features 🚦

- **Concurrency Control:** Prevents simultaneous workflow runs on the same branch or pull request to save build minutes and prevent race conditions.

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

- **Disabling / Enabling Workflows:** Managed directly in the **Actions** tab of the repository UI.
- **Re-running Workflows:** Re-run failed jobs only or re-run all jobs from the GitHub UI or CLI.

#### Debugging Techniques 🐞

1. **Enable Runner Diagnostic / Step Debug Logging:**
   Set repository secrets to enable detailed step-by-step diagnostic logs:

- `ACTIONS_RUNNER_DEBUG`: `true` (enables runner operational logs)
- `ACTIONS_STEP_DEBUG`: `true` (enables detailed execution logs for each step)

2. **Debugging Commands in Step Logs:**

```yaml
steps:
  - name: Debug Information
    run: |
      echo "::error::This is a formatted error message"
      echo "::warning::This is a custom warning"
      echo "::notice::This is a log notice"
      echo "::group::Expandable Log Group"
      echo "Env: ${{ runner.os }}"
      echo "::endgroup::"
```

3. **Job Failure Handlers:**
   Use conditional execution functions (`always()`, `failure()`, `cancelled()`) to capture diagnostic data when jobs fail.

```yaml
- name: Upload Error Logs
  if: failure()
  uses: actions/upload-artifact@v4
  with:
    name: failure-logs
    path: ./logs/
```

---

### 10.4 Customizing Workflows with Environment Variables & Secrets 🔐

GitHub Actions provides default contexts, custom environment variables, and encrypted secrets.

| Scope                                | Definition Method           | Usage Syntax                       | Notes                                 |
| ------------------------------------ | --------------------------- | ---------------------------------- | ------------------------------------- |
| **Default Environment Variables** 🌐 | Set by GitHub automatically | `$GITHUB_SHA`, `$GITHUB_REF`       | Available in shell scripts natively   |
| **Workflow / Job Level `env**` 📝    | Defined via `env:` block    | `${{ env.VARIABLE_NAME }}`         | Static configuration per job/workflow |
| **Step-Level Dynamic Variables** ⚡  | Written to `$GITHUB_ENV`    | `echo "KEY=VAL" >> $GITHUB_OUTPUT` | Accessible in subsequent steps        |
| **Encrypted Secrets** 🔑             | Configured in Repo Settings | `${{ secrets.MY_SECRET }}`         | Automatically masked in workflow logs |

#### Example: Environment Variables & Secrets Configuration 💡

```yaml
env:
  GLOBAL_STAGE: "staging"

jobs:
  deploy:
    runs-on: ubuntu-latest
    env:
      JOB_REGION: "us-east-1"

    steps:
      - name: Set Dynamic Environment Variable
        run: echo "BUILD_DATE=$(date +'%Y-%m-%d')" >> $GITHUB_ENV

      - name: Consume Environment Variables & Secrets
        env:
          API_KEY: ${{ secrets.PROD_API_KEY }}
        run: |
          echo "Stage: $GLOBAL_STAGE"
          echo "Region: $JOB_REGION"
          echo "Build Date: $BUILD_DATE"
          # $API_KEY is masked in output logs as ***
```

---

### 10.5 Caching, Sharing Artifacts & Reusable Workflows 📦

#### Caching Dependencies (`actions/cache`) 🚀

Reusing dependencies across runs significantly reduces build duration and network traffic.

```yaml
- name: Cache Node Modules
  uses: actions/cache@v4
  with:
    path: ~/.npm
    key: ${{ runner.os }}-node-${{ hashFiles('**/package-lock.json') }}
    restore-keys: |
      ${{ runner.os }}-node-
```

> 💡 **Note:** Common setup actions like `actions/setup-node`, `actions/setup-python`, and `actions/setup-java` have built-in `cache:` parameters that wrap `actions/cache` automatically.

#### Sharing Data Between Jobs 🔄

Because jobs execute on isolated runners, data must be shared using **Artifacts** or **Job Outputs**.

- **Artifacts (`upload-artifact` / `download-artifact`):** Best for files, binaries, build folders, and test reports.
- **Job Outputs (`$GITHUB_OUTPUT` + `needs`):** Best for short strings, tags, dynamic versions, and flags.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    outputs:
      build_version: ${{ steps.gen_ver.outputs.version }}
    steps:
      - uses: actions/checkout@v4
      - id: gen_ver
        run: echo "version=v1.2.3" >> $GITHUB_OUTPUT
      - run: npm run build
      - uses: actions/upload-artifact@v4
        with:
          name: dist-folder
          path: dist/

  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/download-artifact@v4
        with:
          name: dist-folder
          path: dist/
      - run: echo "Deploying version ${{ needs.build.outputs.build_version }}"
```

#### Reusable Workflows (`workflow_call`) ♻️

Promotes DRY (Don't Repeat Yourself) architecture across multiple repositories or workflows.

```yaml
jobs:
  call-security-scan:
    uses: my-org/shared-workflows/.github/workflows/security-scan.yml@v1
    with:
      target-environment: "production"
    secrets:
      token: ${{ secrets.ORG_SCAN_TOKEN }}
```

---

### 10.6 Automating GitHub with GitHub Script 📜

**GitHub Script** (`actions/github-script`) is an official Action that enables inline Node.js execution directly inside workflow files. It allows seamless programmatic interaction with the **GitHub API** and **workflow context** without the overhead of building, packaging, or publishing a dedicated custom Action.

#### Key Architecture & Built-in Globals 🏗️

- **`actions/github-script`**: Action wrapper providing an isolated JavaScript execution scope pre-loaded with official SDK tools.
- **`github`**: Pre-authenticated [Octokit REST/GraphQL client](https://www.google.com/search?q=https://octokit.github.io/rest.js/), giving full API access to repositories, issues, pull requests, runs, and security alerts.
- **`context`**: Execution payload metadata (e.g., `context.repo.owner`, `context.repo.repo`, commit SHAs, branch references, issue numbers).
- **`core`**: `@actions/core` SDK methods for managing pipeline state—setting step outputs (`core.setOutput`), setting process failure (`core.setFailed`), appending step summaries (`core.summary`), and masking sensitive values.
- **`exec`**: `@actions/exec` utility to run shell commands asynchronously (`await exec.exec('git status')`) inside the runner environment.

#### Automation Scenarios 🎯

| Scenario                     | Practical Implementation                                                                                  |
| ---------------------------- | --------------------------------------------------------------------------------------------------------- |
| **Issue & PR Triage** 🏷️     | Apply contextual labels, assign code owners, or post welcome messages on creation.                        |
| **Release Orchestration** 🚀 | Dynamically generate release notes, calculate semantic version tags, or publish GitHub Releases.          |
| **Pipeline Gatekeeping** 🚧  | Verify review constraints or commit message formats prior to allowing deployment steps.                   |
| **Cross-Repo Dispatch** 🔗   | Trigger workflows in external repositories using `repository_dispatch` and a Personal Access Token (PAT). |
| **Workflow Diagnostics** 📊  | Emit inline commit annotations or generate custom Markdown step summaries via `core.summary`.             |

#### Complete Implementation: Auto-Triage & Dynamic Output ✨

The workflow below automatically responds to newly opened issues, assigns a `triage` label concurrently via Octokit, enforces least-privilege security permissions, and passes computed metadata to downstream workflow steps.

```yaml
name: Triage New Issues

on:
  issues:
    types: [opened]

permissions:
  issues: write

jobs:
  triage:
    name: Auto-Triage & Metadata Generation
    runs-on: ubuntu-latest

    steps:
      - name: Process Issue and Produce Outputs
        id: triage-step
        uses: actions/github-script@v7
        with:
          script: |
            const { owner, repo } = context.repo;
            const issue_number = context.issue.number;

            try {
              await Promise.all([
                github.rest.issues.createComment({
                  owner,
                  repo,
                  issue_number,
                  body: 'Thanks for opening this issue! Our team will review it shortly.'
                }),
                github.rest.issues.addLabels({
                  owner,
                  repo,
                  issue_number,
                  labels: ['triage']
                })
              ]);
            } catch (error) {
              core.setFailed(`Failed to triage issue #${issue_number}: ${error.message}`);
            }

            const today = new Date().toISOString().split('T')[0];
            core.setOutput('date', today);

      - name: Consume Generated Step Output
        run: |
          echo "Processing Date: ${{ steps.triage-step.outputs.date }}"
```

---

## 11. GitHub Packages 📦

### 11.1 Architecture & Core Concepts

GitHub Packages is an integrated software package hosting service that allows you to store, manage, and distribute software packages (dependencies, libraries, and container images) privately or publicly alongside your source code.

- **Key Use Cases:**
- Centralizing code and package management within a single platform.
- Sharing proprietary libraries securely across internal enterprise teams without setting up third-party package registries.
- Linking software packages directly to commits, pull requests, and GitHub Actions CI/CD workflows for full supply-chain end-to-end traceability.
- Consolidating access control, billing, and team permissions under your existing GitHub organization.

- **Supported Ecosystems & Registry Endpoints:**

| Package Type                         | Registry Endpoint URL                           | Package Naming / Scoping Requirement |
| ------------------------------------ | ----------------------------------------------- | ------------------------------------ |
| **Container Images (OCI/Docker)** 🐳 | `ghcr.io`                                       | `ghcr.io/OWNER/PACKAGE-NAME`         |
| **npm (JavaScript)** 🟡              | `https://npm.pkg.github.com`                    | `@OWNER/PACKAGE-NAME`                |
| **NuGet (.NET)** 🔷                  | `https://nuget.pkg.github.com/OWNER/index.json` | `OWNER.PACKAGE-NAME`                 |
| **Maven (Java)** ☕                  | `https://maven.pkg.github.com/OWNER/REPOSITORY` | `com.OWNER.app`                      |
| **RubyGems (Ruby)** 💎               | `https://rubygems.pkg.github.com/OWNER`         | `PACKAGE-NAME`                       |

- **Scoping & Package Visibility Rules:**
- Package names **must** be explicitly scoped to the repository owner or organization name (e.g., `@my-org/my-package`).
- By default, a package published from a private repository inherits **private** visibility, while a package published from a public repository is **public**.
- Packages can have their permissions configured independently of the underlying source code repository.

---

### 11.2 Publishing Packages via GitHub Actions 🚀

Automating package publication in a CI pipeline removes manual publishing steps and ensures packages are only pushed when code passes verification or when a release is created.

#### Mandatory Workflow Permissions (`permissions`) 🔒

Every job that publishes a package **must** declare the `packages: write` permission scope. Without this explicit scope, the build step will fail with an HTTP 403 Forbidden error.

```yaml
permissions:
  contents: read # To read repo files
  packages: write # MANDATORY to publish/delete packages in GitHub Packages
```

#### Authentication Options 🔑

- **`GITHUB_TOKEN`:** Workflows automatically inherit `${{ secrets.GITHUB_TOKEN }}`. This short-lived token has sufficient rights to publish packages to the **same repository** where the workflow runs.
- **Personal Access Token (PAT):** If your workflow needs to publish a package to a **different repository** or an **organization-level registry** outside the workflow's immediate scope, you must use a PAT with the `write:packages` scope stored in GitHub Secrets.

---

### 11.3 Publishing Examples 💡

#### Example 1: Publishing an NPM Package 🟡

To publish an npm package, point npm to `https://npm.pkg.github.com`, configure the `@scope`, and pass `NODE_AUTH_TOKEN`.

```yaml
name: Publish NPM Package

on:
  release:
    types: [published]

jobs:
  publish-npm:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Setup Node.js Environment
        uses: actions/setup-node@v4
        with:
          node-version: "20"
          registry-url: "[https://npm.pkg.github.com](https://npm.pkg.github.com)"
          scope: "@${{ github.repository_owner }}"

      - name: Install Dependencies
        run: npm ci

      - name: Publish Package
        run: npm publish
        env:
          NODE_AUTH_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

#### Example 2: Publishing a Container Image to `ghcr.io` 🐳

Publishing OCI/Docker images to the GitHub Container Registry (`ghcr.io`) using `docker/login-action`.

```yaml
name: Build and Push Container Image

on:
  push:
    branches: ["main"]

jobs:
  publish-container:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Log in to GitHub Container Registry
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Build and Push Docker Image
        run: |
          IMAGE_NAME=$(echo "ghcr.io/${{ github.repository }}" | tr '[A-Z]' '[a-z]')
          docker build -t $IMAGE_NAME:latest -t $IMAGE_NAME:${{ github.sha }} .
          docker push $IMAGE_NAME --all-tags
```

---

## 12. Advanced GitHub Actions: Exam Focus 🎓

Key hands-on concepts for advanced implementation:

- Custom composite actions 🛠️
- Matrix builds & caching mechanisms ⚡
- OIDC authentication to cloud providers (AWS/Azure/GCP) ☁️

---

### 12.1 Custom Composite Actions 🛠️

A **Composite Action** bundles multiple workflow steps into a single reusable unit using standard YAML syntax. Unlike JavaScript actions or Docker container actions, composite actions execute directly on the runner host machine without requiring Node.js runtimes or container builds.

#### Structure & Placement 📁

- **Location:** Placed in the repository root inside `action.yml` (or `action.yaml`), or inside subdirectories (e.g., `.github/actions/my-action/action.yml`).
- **Invocation:**
- From relative path: `uses: ./.github/actions/my-action`
- From external repo: `uses: owner/repo/path/to/action@v1`

#### Anatomy of `action.yml` 📄

```yaml
name: "Build and Scan App"
description: "Builds application and executes security scanning"

inputs:
  build-env:
    description: "Target build environment"
    required: true
    default: "production"
  node-version:
    description: "Node JS runtime version"
    required: false
    default: "20"

outputs:
  artifact-id:
    description: "Generated build artifact identifier"
    value: ${{ steps.build-step.outputs.artifact-id }}

runs:
  using: "composite" # MUST be explicitly set to 'composite'
  steps:
    - name: Set up Node.js
      uses: actions/setup-node@v4
      with:
        node-version: ${{ inputs.node-version }}
      shell: bash # CRITICAL: shell is mandatory for EVERY run step

    - name: Execute Build
      id: build-step
      run: |
        npm ci
        BUILD_ID="build-$(date +%s)"
        echo "artifact-id=$BUILD_ID" >> $GITHUB_OUTPUT
      shell: bash

    - name: Run Inline Script
      run: python ./scripts/verify.py
      shell: python
```

#### Composite Actions vs. Reusable Workflows ⚔️

| Feature                 | Composite Action                                    | Reusable Workflow                                 |
| ----------------------- | --------------------------------------------------- | ------------------------------------------------- |
| **Primary Unit**        | Encapsulates **steps**                              | Encapsulates entire **jobs**                      |
| **Syntax File**         | `action.yml`                                        | `.github/workflows/filename.yml`                  |
| **Engine Keyword**      | `runs.using: 'composite'`                           | `on: workflow_call:`                              |
| **Execution Context**   | Runs within the parent job context                  | Spawns separate job execution blocks              |
| **Secrets Access**      | Must pass secrets explicitly via `inputs`           | Accepts `secrets: inherit` or explicit `secrets:` |
| **Runner Requirements** | Mandatory `shell:` declaration on every `run:` step | Standard workflow defaults apply                  |

---

### 12.2 Matrix Builds & Caching Mechanisms ⚡

#### Part A: Matrix Strategies 🌐

Matrix strategies allow you to automatically generate job configurations based on variable combinations, enabling parallel testing across operating systems, language runtimes, or database versions.

##### Cartesian Generation ✖️

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [18, 20]
# Result: 4 jobs generated (ubuntu-18, ubuntu-20, windows-18, windows-20)
```

##### Expanding the Matrix (`include`) ➕

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, macos-latest]
    node: [18, 20]
    include:
      # Adds 'experimental: true' ONLY to ubuntu + node 20
      - os: ubuntu-latest
        node: 20
        experimental: true
      # Adds an entirely new job combination not in the base matrix
      - os: windows-latest
        node: 20
        experimental: false
```

##### Pruning the Matrix (`exclude`) ➖

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
    node: [16, 18, 20]
    exclude:
      - os: macos-latest
        node: 16 # Disables macOS + Node 16 combination only
```

##### Execution Control Keywords 🎛️

- `fail-fast: true` _(Default)_: Cancels all active/queued matrix jobs as soon as **any** matrix job fails.
- `fail-fast: false`: Ensures all matrix jobs run to completion even if one fails.
- `max-parallel: <number>`: Caps how many matrix jobs run concurrently.
- `continue-on-error: true`: Set at the job level to allow the pipeline to pass even if an experimental matrix leg fails.

---

#### Part B: Caching Mechanisms 💾

Caching reduces workflow run times by preserving unchanged dependencies (like `node_modules`, Maven local repos, or Pip packages) across workflow runs.

##### 1. Explicit Caching with `actions/cache@v4` 📦

```yaml
- name: Cache Gradle packages
  uses: actions/cache@v4
  with:
    path: ~/.gradle/caches
    key: gradle-${{ runner.os }}-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
    restore-keys: |
      gradle-${{ runner.os }}-
      gradle-
```

- **`path`:** The directory or files to save and restore.
- **`key`:** Explicit identifier evaluated when saving/restoring. Uses `hashFiles()` to regenerate when lockfiles change.
- **`restore-keys`:** Sequential fallback prefixes evaluated top-to-bottom if an exact `key` match misses.

##### 2. Ecosystem Setup Actions (Built-in Caching) ⚡

```yaml
- uses: actions/setup-node@v4
  with:
    node-version: "20"
    cache: "npm" # Supports npm, yarn, pnpm

- uses: actions/setup-python@v5
  with:
    python-version: "3.11"
    cache: "pip" # Supports pip, pipenv, poetry
```

##### 3. Caching Rules & Constraints 🚨

- **Storage Limit:** **10 GB total per repository**.
- **Eviction Policy:** Oldest caches are automatically deleted once the 10 GB limit is exceeded. Caches unused for **7 days** are automatically evicted.
- **Cache Scope Restrictions:**
- Workflows can access caches created in the current branch, target branch (for pull requests), or default branch (`main`/`master`).
- Feature branches **cannot** access caches created in sibling feature branches.

---

### 12.3 OIDC Authentication to Cloud Providers ☁️

OpenID Connect (OIDC) replaces long-lived cloud access keys (`AWS_SECRET_ACCESS_KEY`, Azure Client Secrets) stored in GitHub secrets with **short-lived, auto-expiring JWT tokens**.

```text
[ GitHub Actions Runner ] --- (1) Requests OIDC Token ---> [ GitHub OIDC Issuer ]
                                                                     |
[ GitHub Actions Runner ] <-- (2) Returns Signed JWT Token <---------+
            |
            +--- (3) Exchanging JWT Token for Cloud STS Credentials ---> [ Cloud Provider (AWS/Azure/GCP) ]
                                                                                   |
[ GitHub Actions Runner ] <-- (4) Returns Short-lived Session Token <--------------+

```

#### Essential Requirements 🔑

1. **Mandatory Permission:** The job or root workflow MUST include explicit `id-token` write permissions:

```yaml
permissions:
  id-token: write # Generates the JWT token
  contents: read # Required to check out repository code
```

2. **Issuer Endpoint:** `https://token.actions.githubusercontent.com`
3. **Subject Claim (`sub`):** Identifies the exact repository context requesting authentication.

- Example: `repo:my-org/my-repo:ref:refs/heads/main`
- Example: `repo:my-org/my-repo:environment:production`

---

#### Cloud Implementation Examples 🌐

##### AWS Authentication (`aws-actions/configure-aws-credentials`) 🟠

```yaml
jobs:
  aws-deploy:
    runs-on: ubuntu-latest
    permissions:
      id-token: write
      contents: read
    steps:
      - name: Configure AWS Credentials via OIDC
        uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::123456789012:role/GitHubActionsCloudDeploy
          aws-region: us-east-1
```

> 💡 **AWS IAM Trust Policy:** Requires an Identity Provider (IdP) for `token.actions.githubusercontent.com` matching `token.actions.githubusercontent.com:sub` against `repo:org/repo:ref:refs/heads/main`.

---

##### Azure Authentication (`azure/login`) 🔵

```yaml
jobs:
  azure-deploy:
    runs-on: ubuntu-latest
    permissions:
      id-token: write
      contents: read
    steps:
      - name: Azure Login via OIDC
        uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
```

> 💡 **Azure Configuration:** Set up a **Federated Identity Credential** on the Azure AD App Registration linked to the repository name and branch/environment.

---

##### GCP Authentication (`google-github-actions/auth`) 🔴

```yaml
jobs:
  gcp-deploy:
    runs-on: ubuntu-latest
    permissions:
      id-token: write
      contents: read
    steps:
      - name: Authenticate to Google Cloud
        uses: google-github-actions/auth@v2
        with:
          workload_identity_provider: "projects/123456789/locations/global/workloadIdentityPools/my-pool/providers/my-provider"
          service_account: "my-service-account@my-project.iam.gserviceaccount.com"
```

> 💡 **GCP Configuration:** Requires a **Workload Identity Pool** and Provider mapping `attribute.repository` to `assertion.repository`.
