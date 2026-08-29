# AI & Machine Learning Fundamentals

---

## Table of Contents

1. [AI Terminology](#1-ai-terminology)
2. [Levels of AI Capability](#2-levels-of-ai-capability)
3. [AI Approaches: Traditional vs Generative](#3-ai-approaches-traditional-vs-generative)
4. [Types of Machine Learning](#4-types-of-machine-learning)
5. [Neural Network Models](#5-neural-network-models)
6. [Generative AI Models](#6-generative-ai-models)
7. [How It All Connects](#7-how-it-all-connects)
8. [Building GenAI-Powered Apps: Tools & Frameworks](#8-building-genai-powered-apps-tools--frameworks)
9. [Types of AI Agents](#9-types-of-ai-agents)
10. [Glossary Quick Reference](#glossary)

---

## 1. AI Terminology

### 1.1 What is AI, Really?

**Artificial Intelligence (AI)** is the broad field of building systems that perform tasks normally requiring human intelligence — reasoning, perception, decision-making, language.

**Machine Learning (ML)** is a _subset_ of AI — the ability of a system to **learn from data** and identify patterns to build a model that can autonomously **predict**, **classify**, or **generate content**, rather than being explicitly programmed with fixed rules.

**Deep Learning (DL)** is a _subset_ of ML that uses multi-layered neural networks to learn increasingly abstract representations of data.

```
┌───────────────────────────────────────────────┐
│  Artificial Intelligence (the broad field)    │
│  ┌──────────────────────────────────────────┐ │
│  │  Machine Learning                        │ │
│  │  (learns patterns from data)             │ │
│  │  ┌───────────────────────────────────┐   │ │
│  │  │  Deep Learning                    │   │ │
│  │  │  (neural networks, many layers)   │   │ │
│  │  └───────────────────────────────────┘   │ │
│  └──────────────────────────────────────────┘ │
└───────────────────────────────────────────────┘
```

### 1.2 Augmented AI

**Augmented AI** is a collaboration model between humans and artificial intelligence, rather than AI replacing humans entirely. The idea rests on complementary strengths:

- **AI excels at:** processing huge datasets, speed, consistency, pattern recognition at scale
- **Humans excel at:** emotional intelligence, creativity, ethical judgment, contextual nuance

Rather than "AI vs humans," Augmented AI treats it as "AI + humans" — the AI handles data-heavy pattern work, while humans provide judgment, creativity, and oversight.

---

## 2. Levels of AI Capability

There are three widely-discussed tiers of AI capability, based on how broad and human-like the intelligence is:

```
┌──────────────────────────────────────────────────────┐
│  SUPER AI                                            │
│  "The unfathomable mind"                             │
│  Hypothetical AI that vastly exceeds human           │
│  intelligence across all domains                     │
├──────────────────────────────────────────────────────┤
│  STRONG / GENERAL AI (AGI)                           │
│  "The human equal"                                   │
│  Can think, learn, and reason across ANY domain,     │
│  the way a human can — does not yet exist            │
├──────────────────────────────────────────────────────┤
│  WEAK / NARROW AI                                    │
│  "The specialist"                                    │
│  Excels at ONE specific task (e.g. chess, image      │
│  recognition, language). This is ALL AI that exists  │
│  today, including LLMs like Claude/ChatGPT           │
└──────────────────────────────────────────────────────┘
```

| Tier                        | Also Known As                        | Capability                                                          | Real-World Status                                                                                                                     |
| --------------------------- | ------------------------------------ | ------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **Weak/Narrow AI**          | ANI (Artificial Narrow Intelligence) | Excels at one specific, well-defined task                           | **Exists today** — every AI system in production (Pi-hole-style filters, recommendation engines, LLMs, self-driving perception, etc.) |
| **Strong/General AI (AGI)** | AGI                                  | Can think, learn, and reason across any domain like a human         | **Does not yet exist** — an active research goal                                                                                      |
| **Super AI**                | ASI (Artificial Superintelligence)   | Intelligence vastly exceeding all human capability, in every domain | **Purely hypothetical/theoretical**                                                                                                   |

> **Key insight:** Even the most advanced LLMs today (GPT, Claude, Gemini) are technically Narrow AI — extremely broad and impressive within the "predict the next token" task, but not truly general reasoning across every domain the way AGI is defined.

---

## 3. AI Approaches: Traditional vs Generative

Both approaches use Machine Learning techniques underneath, but they're trained for fundamentally different goals.

```
              AI Approaches
             (both use ML techniques)
                    │
        ┌───────────┴───────────┐
        │                       │
  Traditional AI          Generative AI
        │                       │
  Takes large datasets    Feeds massive amounts
  and uses ML to find     of unstructured data
  patterns                to deep learning
        │                 neural networks
  Trained to:                    │
  PREDICT & CLASSIFY       Trained to:
                          CREATE/GENERATE new data
                          (that looks like training data)
```

| Aspect             | Traditional AI                                               | Generative AI                             |
| ------------------ | ------------------------------------------------------------ | ----------------------------------------- |
| **Goal**           | Predict or classify existing data                            | Create new data resembling training data  |
| **Input Data**     | Structured, often labeled                                    | Massive, often unstructured               |
| **Output**         | A number, category, or label                                 | New text, images, audio, or other content |
| **Example Task**   | "Is this email spam?"                                        | "Write me an email"                       |
| **Example Models** | Logistic Regression, Random Forest, standard Neural Networks | GANs, VAEs, Transformers (LLMs)           |

---

## 4. Types of Machine Learning

There are three fundamental paradigms for how a model learns:

```
┌───────────────────────────────────────────────────────────┐
│                 Types of Machine Learning                 │
├───────────────────────────────────────────────────────────┤
│                                                           │
│  SUPERVISED           UNSUPERVISED        REINFORCEMENT   │
│  ───────────          ─────────────       ────────────────│
│  Trained on           Find patterns in    Trial-and-error │
│  LABELED data         UNLABELED data      loop training   │
│                                           maximizing      │
│  ┌─────────────┐      ┌───────────────┐   rewards,        │
│  │ Regression  │      │ Clustering    │   minimizing      │
│  │ (continuous │      │ (group similar│   penalties       │
│  │  numbers)   │      │  data)        │                   │
│  ├─────────────┤      ├───────────────┤                   │
│  │Classification│     │ Anomaly       │                   │
│  │ (discrete   │      │ Detection     │                   │
│  │ categories) │      │               │                   │
│  └─────────────┘      └───────────────┘                   │
│                                                           │
└───────────────────────────────────────────────────────────┘
```

### 4.1 Supervised Learning

**Trained on labeled data** — every training example comes with the "correct answer" already attached, and the model learns to map inputs to outputs.

**Two main sub-tasks:**

- **Regression** — predicts **continuous numbers**.
  - Example: predict house prices based on square footage, location, etc.
  - Output: a real number (e.g., $342,500)

- **Classification** — predicts **discrete values, categories, or groups**.
  - Example: is this email spam or not spam?
  - Output: a category/label (e.g., "spam", "not spam")

**Common algorithms:** Linear/Logistic Regression, Decision Trees, Random Forest, Support Vector Machines (SVM), Neural Networks

### 4.2 Unsupervised Learning

**Finds patterns in unlabeled data** — there's no "correct answer" given; the model discovers structure on its own.

**Common tasks:**

- **Clustering** — grouping similar data points together (e.g., customer segmentation)
- **Anomaly Detection** — identifying unusual data points that don't fit expected patterns (e.g., fraud detection, network intrusion detection)

**Common algorithms:** K-Means Clustering, Hierarchical Clustering, DBSCAN, Isolation Forest, Principal Component Analysis (PCA, for dimensionality reduction)

### 4.3 Reinforcement Learning

**Trial-and-error loop training** — an "agent" takes actions in an environment, receiving **rewards** for good outcomes and **penalties** for bad ones. Over many iterations, it learns a strategy (policy) that **maximizes cumulative reward**.

```
┌──────────┐   action    ┌─────────────┐
│  Agent   │ ──────────→ │ Environment │
│          │ ←────────── │             │
└──────────┘  reward/    └─────────────┘
              state

Loop repeats thousands/millions of times,
agent gradually learns the best actions
```

**Real-world examples:** Game-playing AI (AlphaGo, chess engines), robotics control, resource allocation, recommendation systems that adapt to feedback

### 4.4 Quick Comparison Table

| Type              | Data                                       | Goal                       | Example                                 |
| ----------------- | ------------------------------------------ | -------------------------- | --------------------------------------- |
| **Supervised**    | Labeled                                    | Predict output from input  | Predicting house prices, spam detection |
| **Unsupervised**  | Unlabeled                                  | Discover hidden structure  | Customer segmentation, fraud detection  |
| **Reinforcement** | No fixed dataset — learns from interaction | Maximize cumulative reward | Game-playing AI, robotics               |

---

## 5. Neural Network Models

Neural Networks are the foundation of **Deep Learning**. They're loosely inspired by how neurons in the brain connect and fire.

### 5.1 Traditional Neural Networks

Used to **analyze, classify, and predict**.

```
Traditional Neural Network Types
        │
        ├── Feed-Forward
        │   Standard data classification and
        │   predicting numerical values
        │   (data flows in ONE direction: input → output)
        │
        ├── Recurrent (RNN)
        │   Sequential tasks (e.g. Speech-to-Text)
        │   (has "memory" of previous inputs in the sequence)
        │
        └── Convolutional (CNN)
            Vision tasks
            (specializes in detecting spatial patterns —
             edges, shapes, textures in images)
```

| Architecture            | Best For                                                | How It Works (Intuition)                                                                    |
| ----------------------- | ------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| **Feed-Forward**        | Standard classification, predicting numerical values    | Data flows straight through layers, one direction, no memory of previous inputs             |
| **Recurrent (RNN)**     | Sequential data — speech-to-text, time series, language | Loops information back to itself, giving it a form of "memory" across a sequence            |
| **Convolutional (CNN)** | Vision tasks — image classification, object detection   | Uses filters that slide across an image to detect patterns like edges, shapes, and textures |

### 5.2 Deep Learning

**Deep Learning** simply means using neural networks with **many layers** ("deep" = many hidden layers stacked), enabling the model to learn increasingly abstract, hierarchical representations of the data (e.g., in image recognition: edges → shapes → objects → scenes).

---

## 6. Generative AI Models

Unlike traditional models that predict/classify existing data, **generative models are trained to create new data** that resembles the training data.

```
Generative Models
        │
        ├── VAEs (Variational Autoencoders)
        │   Encode → Decode
        │   (shrink data down → build it back out)
        │
        ├── GANs (Generative Adversarial Networks)
        │   Generator → Discriminator
        │   (produce fake data → evaluate if it's real or fake)
        │
        └── Transformers
            LLMs, self-attention mechanism
            (foundation of ChatGPT, Claude, etc.)
```

### 6.1 VAEs (Variational Autoencoders)

**Encode → Decode.** The model compresses (shrinks) input data down into a compact internal representation, then learns to reconstruct (build back out) the original from that compressed form. By learning this compressed "latent space," the model can generate new data by sampling from it.

```
Input Image → [Encoder] → Compressed representation → [Decoder] → Reconstructed/New Image
              (shrink)                                  (build back out)
```

**Use cases:** Image generation, anomaly detection, data compression

### 6.2 GANs (Generative Adversarial Networks)

**Generator → Discriminator.** Two neural networks compete against each other:

- The **Generator** tries to **produce** fake data realistic enough to fool the discriminator
- The **Discriminator** tries to **evaluate** whether the data is real or fake

```
Generator → produces fake image
                ↓
         Discriminator → "Real or Fake?"
                ↓
    Both networks improve through this
    adversarial competition over time
```

Over many training iterations, the Generator gets progressively better at producing convincing fakes, and the Discriminator gets better at spotting them — pushing both to improve.

**Use cases:** Deepfakes, synthetic image generation, art generation, data augmentation

### 6.3 Transformers

The architecture behind **LLMs** (Large Language Models) like GPT, Claude, and Gemini. The key innovation is the **self-attention mechanism** — the model weighs the relevance of every other word/token in the input when processing each word, allowing it to capture long-range context and relationships in a sequence far better than older RNN-based approaches.

**Use cases:** Language models (chatbots, translation, summarization), and increasingly vision and multi-modal tasks too

---

## 7. How It All Connects

```
┌────────────────────────────────────────────────────────────────┐
│                                                                │
│   AI (broad field)                                             │
│     │                                                          │
│     ├── Levels of Capability: Narrow (today) → AGI → ASI       │
│     │                                                          │
│     └── Machine Learning                                       │
│           │                                                    │
│           ├── Types (HOW it learns):                           │
│           │     ├── Supervised (labeled data)                  │
│           │     ├── Unsupervised (unlabeled data)              │
│           │     └── Reinforcement (trial and error)            │
│           │                                                    │
│           └── Deep Learning (neural networks, many layers)     │
│                 │                                              │
│                 ├── Traditional NN (predict/classify):         │
│                 │     ├── Feed-Forward                         │
│                 │     ├── Recurrent (RNN)                      │
│                 │     └── Convolutional (CNN)                  │
│                 │                                              │
│                 └── Generative (create new data):              │
│                       ├── VAEs (encode → decode)               │
│                       ├── GANs (generate → discriminate)       │
│                       └── Transformers (LLMs, self-attention)  │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

**Reading it top-down:** AI is the umbrella field. Within it, we can categorize by **capability level** (narrow vs general vs super) or by **subfield** (Machine Learning). Within ML, we categorize by **how the model learns** (supervised / unsupervised / reinforcement) and separately by **architecture** (traditional neural nets for predicting, vs generative models for creating).

---

## 8. Building GenAI-Powered Apps: Tools & Frameworks

Knowing the theory (ML types, neural nets, generative models) is one thing — actually **building** a GenAI app pulls in a whole extra layer of tools: ways to ground models in real facts, frameworks to wire everything together, and platforms to host/share the result.

### 8.1 Retrieval-Augmented Generation (RAG)

**RAG** is a framework that grounds an LLM's answers in facts pulled from an **external knowledge base**, rather than relying only on what the model memorized during training.

```
User Query
    │
    ▼
[Retriever] ──→ searches external knowledge base (docs, DB, web)
    │                 (latest research, stats, news, private data)
    ▼
Relevant chunks/context
    │
    ▼
[LLM] ──→ generates an answer GROUNDED in the retrieved context
    │
    ▼
Final Response (more accurate, up-to-date, less "hallucinated")
```

**Why it matters:** An LLM's training data has a cutoff and can't know about your private documents. RAG solves this by retrieving relevant text at query time and feeding it to the model as context, rather than requiring an expensive retrain/fine-tune.

### 8.2 Orchestration Frameworks: LangChain & LlamaIndex

Once you're combining an LLM + a retriever + a data source + maybe some tools, you need a framework to glue it together:

| Framework      | Primary Role                                                                                                                                                        |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **LangChain**  | Simplifies building full LLM-powered applications — document analysis, summarization, chatbot building, code analysis, chaining multiple steps/prompts together     |
| **LlamaIndex** | A flexible **data framework** whose specialty is connecting custom/external data sources to an LLM through a central interface (the "index" the LLM retrieves from) |

**Rule of thumb:** LlamaIndex is often used as the _retrieval/indexing layer_ feeding data in, while LangChain is used as the _orchestration layer_ chaining prompts, tools, and logic — the two are frequently used together in a RAG pipeline.

### 8.3 Model & App Hubs: Hugging Face, Gradio, Streamlit

| Tool             | What It's For                                                                                                                                      |
| ---------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Hugging Face** | An AI platform/community hub where open-source scientists, developers, and companies share and collaborate on machine learning models and datasets |
| **Gradio**       | An open-source Python package for quickly building a **demo/web UI** around a model — good for lightweight, shareable ML demos                     |
| **Streamlit**    | An open-source framework that turns a plain data/Python script into a shareable **web app** in minutes — popular for data apps and dashboards      |

**In practice:** You might pull a model from Hugging Face, then wrap it in a Gradio or Streamlit interface so non-technical users can interact with it through a browser.

### 8.4 Notable Foundation Models & LLMs

**Foundation models** are broad, general-purpose AI models trained on huge datasets that can then be **adapted** (fine-tuned or prompted) for many specific downstream tasks — they're the base layer most GenAI apps are built on top of.

| Model / System                               | Notes                                                                                                                         |
| -------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **GPT (Generative Pre-trained Transformer)** | OpenAI's LLM family; combines large-scale training with the transformer architecture                                          |
| **ChatGPT**                                  | OpenAI's chatbot product built on GPT models                                                                                  |
| **Llama**                                    | Meta AI's family of LLMs                                                                                                      |
| **Falcon**                                   | Developed by the Technology Innovation Institute (TII); the `falcon-7b-instruct` variant is a 7B-parameter decoder-only model |
| **Google Flan**                              | An encoder-decoder foundation model based on the T5 architecture                                                              |
| **BLIP**                                     | A vision-language pre-training framework for multi-modal tasks (visual Q&A, image captioning, image-text retrieval)           |
| **BlenderBot**                               | Meta's conversational chatbot, designed to take direct feedback to improve its responses                                      |
| **CodeT5**                                   | Google AI's text-to-code model — the first code-aware, encoder-decoder pretrained programming-language model                  |
| **OpenAI Whisper**                           | Automatic speech recognition system trained on 680,000 hours of supervised audio; transcribes speech across many languages    |

### 8.5 NLP Building Blocks

Underneath most GenAI apps sit some core NLP mechanics:

- **Tokenizer** — breaks text into smaller units ("tokens": words, sub-words, or characters) so a model can process and understand it. Every LLM has a paired tokenizer.
- **Named-Entity Recognition (NER)** — locates and classifies named entities in text (names, locations, ages, addresses, phone numbers) — useful for information extraction from unstructured text.
- **Sentiment Analysis** — analyzes text to determine its emotional tone (positive/negative/neutral); commonly used by businesses to monitor brand perception from customer feedback.

### 8.6 Putting a RAG App Together (Typical Stack)

```
 Data Source (docs, PDFs, DB)
        │
        ▼
 LlamaIndex (indexes/connects the data)
        │
        ▼
 LangChain (orchestrates retrieval + prompt + LLM call)
        │
        ▼
 LLM (GPT / Llama / Falcon / watsonx.ai model, etc.)
        │
        ▼
 Gradio / Streamlit (wraps it all in a usable web UI)
        │
        ▼
 End User Chat/App
```

---

## 9. Types of AI Agents

An **agent** is a system that perceives its environment and acts on it to achieve some objective. Agents range from very simple rule-followers to complex, learning, multi-agent systems.

### 9.1 Simple Reflex & Model-Based Reflex Agents

| Agent Type                   | How It Works                                                                                | Strengths / Limits                                                                                                                |
| ---------------------------- | ------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| **Simple Reflex Agent**      | Operates on predefined **condition-action rules** ("if X, do Y") with no memory of the past | Simple and fast, but only suitable for fully predictable environments — struggles once things get dynamic or partially observable |
| **Model-Based Reflex Agent** | Enhances the simple reflex agent by keeping an **internal state model** of the world        | Can remember past information and infer unseen parts of the environment, making it more robust than a simple reflex agent         |

### 9.2 Goal-Oriented and Utility-Based Agents

| Agent Type              | How It Works                                                                                                                             | Strengths / Limits                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| **Goal-Based Agent**    | Uses explicit **goals** to guide decision-making, simulating possible future outcomes to pick actions that achieve the desired objective | More flexible than reflex agents since it reasons about the future, not just the present state                                   |
| **Utility-Based Agent** | Evaluates and ranks possible outcomes using a **utility function**, then selects the most _desirable_ outcome                            | Goes a step further than goal-based agents — instead of any goal-achieving action, it picks the _best_ one among several options |

### 9.3 Learning Agents and Multi-Agent Systems

- **Learning Agent** — Improves its performance over time by learning from feedback and experience. Typically built from four components: a **performance element** (acts), a **critic** (evaluates outcomes), a **learning element** (adapts behavior), and a **problem generator** (suggests exploratory actions).
- **Multi-Agent Systems (MAS)** — Multiple agents (often of different types above) work cooperatively within a shared environment to achieve common goals, useful for complex tasks that a single agent can't handle alone.

### 9.4 From Monolithic Models to Compound AI Systems

```
Monolithic Model               Compound AI System
      │                                │
Limited by its own            Combines a model with
training data — hard           external components
to adapt for specific         (databases, tools,
tasks                          verifiers, search)
                               to solve problems
                                more effectively
```

- **Monolithic models** are constrained by what's baked into their training data and are hard to adapt for specific, specialized tasks.
- **Compound AI systems** integrate models with external components — databases, tools, verifiers, search engines — to tackle complex problems more effectively than a model alone.

**Compound AI Systems and Control Logic**

- Compound systems are **modular**, combining models with programmatic components such as verifiers and search tools.
- **Control logic** defines the path taken to answer a query — traditionally hand-programmed by humans, specifying exactly which steps to run and when.

### 9.5 Agentic AI and LLMs

**Agentic AI** flips the control-logic model: instead of humans hard-coding the path, an **LLM controls the system's logic** — reasoning, planning, and iterating toward a solution.

- LLM agents can **act** by calling external tools (search engines, calculators, APIs, etc.).
- LLM agents can **access memory** for context and personalization across steps or sessions.

### 9.6 The ReAct Framework

**ReAct (Reason + Act)** agents interleave reasoning with acting: the agent reasons about what to do, takes an action (usually a tool call), observes the result, and **iterates** on its plan based on that output — repeating until it reaches a satisfactory answer.

```
  Reason ──► Act ──► Observe ──► Reason ──► Act ──► ... ──► Answer
```

**Practical Example — Planning vacation sunscreen needs:**

1. Retrieve how many vacation days are available
2. Check the weather forecast for the destination
3. Consult sun-exposure/health guidelines
4. Perform the calculation to determine how much sunscreen is needed

Each step's output feeds back into the agent's reasoning before it decides the next action — the hallmark of the ReAct loop.

### 9.7 The Future of Compound and Agentic AI Systems

- Compound AI systems will increasingly adopt **agentic features** for greater autonomy.
- **Programmatic approaches** remain well-suited to narrow, well-defined problems, while **agentic systems** are better suited to complex, varied tasks that need more flexibility.

---

## Glossary

| Term                                     | Definition                                                                                |
| ---------------------------------------- | ----------------------------------------------------------------------------------------- |
| **ML**                                   | Subset of AI — systems that learn patterns from data to predict, classify, or generate    |
| **Deep Learning**                        | Subset of ML using multi-layered neural networks                                          |
| **Narrow AI (ANI)**                      | AI specialized in one task — all AI that exists today                                     |
| **AGI**                                  | Hypothetical AI with human-equivalent general reasoning across any domain                 |
| **ASI**                                  | Hypothetical AI vastly exceeding human intelligence in all domains                        |
| **Augmented AI**                         | Collaborative human + AI model, leveraging each other's strengths                         |
| **Supervised Learning**                  | Learning from labeled data (input + correct output pairs)                                 |
| **Unsupervised Learning**                | Finding patterns in unlabeled data                                                        |
| **Reinforcement Learning**               | Learning via trial-and-error, maximizing rewards                                          |
| **Regression**                           | Predicting continuous numerical values                                                    |
| **Classification**                       | Predicting discrete categories/labels                                                     |
| **Clustering**                           | Grouping similar data points (unsupervised)                                               |
| **Anomaly Detection**                    | Identifying unusual/outlier data points                                                   |
| **Feed-Forward NN**                      | Data flows one direction, no memory — standard prediction/classification                  |
| **RNN**                                  | Recurrent Neural Network — has memory, good for sequences                                 |
| **CNN**                                  | Convolutional Neural Network — specializes in spatial/image data                          |
| **VAE**                                  | Variational Autoencoder — encode then decode to generate data                             |
| **GAN**                                  | Generative Adversarial Network — generator vs discriminator competition                   |
| **Transformer**                          | Self-attention based architecture, foundation of modern LLMs                              |
| **Self-Attention**                       | Mechanism where a model weighs relevance of all other tokens when processing each token   |
| **API**                                  | Interface that lets applications communicate and exchange data via shared protocols       |
| **RAG (Retrieval-Augmented Generation)** | Framework that retrieves facts from an external knowledge base to ground an LLM's answers |
| **LangChain**                            | Framework for building LLM apps — chains prompts, tools, retrieval, and logic together    |
| **LlamaIndex**                           | Data framework that connects custom data sources to LLMs via a central index/interface    |
| **Hugging Face**                         | Community/platform hub for sharing and building open-source ML models and datasets        |
| **Gradio**                               | Python package for quickly building a shareable demo/web UI around a model                |
| **Streamlit**                            | Framework that turns a data/Python script into a shareable web app                        |
| **Foundation Model**                     | Broad, general-purpose model adaptable to many specific downstream tasks                  |
| **Tokenizer**                            | Breaks text into tokens (words/sub-words) so a model can process it                       |
| **NER (Named-Entity Recognition)**       | Locates and classifies named entities (names, places, etc.) in unstructured text          |
| **Sentiment Analysis**                   | Analyzes text to determine emotional tone (positive/negative/neutral)                     |
| **PIL (Python Imaging Library)**         | Python library for image processing (reading, rescaling, saving images)                   |
| **Simple Reflex Agent**                  | Acts on condition-action rules only, with no memory of the past                           |
| **Model-Based Reflex Agent**             | Reflex agent that maintains an internal state model of the world                          |
| **Goal-Based Agent**                     | Chooses actions by simulating future outcomes to reach a defined goal                     |
| **Utility-Based Agent**                  | Ranks outcomes via a utility function and picks the most desirable one                    |
| **Learning Agent**                       | Improves over time via critic, learning element, and problem generator components         |
| **Multi-Agent System (MAS)**             | Multiple agents cooperating in a shared environment toward common goals                   |
| **Compound AI System**                   | Modular system combining a model with external tools, databases, and verifiers            |
| **Control Logic**                        | The defined path a system follows to answer a query                                       |
| **Agentic AI**                           | System where an LLM itself controls reasoning, planning, and control logic                |
| **ReAct (Reason + Act)**                 | Agent framework that interleaves reasoning, tool-calling, and observing results in a loop |

- PROJECT: make a chatboot api that uses langchain, python, rag, hugging face models for an agenetic RAG agent, that have tools and then build mcp servers that my mcp host/client (in a UI chatboot type) can call. Everything should be free and local built. Please make use of docker also.

---
