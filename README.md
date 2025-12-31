# Distributed API Rate Limiter

A system built with **Java** and **Redis** to stop users from spamming your API.

This project implements a **token bucket rate limiter**. It ensures that no matter how many servers you have, a user cannot exceed their allowed limit. It uses Lua Scripting to guarantee the counting is accurate.

## What it does

* **Stops Spammers:** Limits how many requests a user can make in a specific time window.
* **Works Everywhere:** Because it uses Redis (a shared database), multiple Java servers can work together and share the same limit.
* **No Counting Errors:** Uses a custom script to prevent "Race Conditions."

## Tech Stack

* **Java 17:** The logic.
* **Redis:** The shared database to store user counts.
* **Lua Script:** A script that runs inside Redis to make counting safe and fast.
* **Docker:** To package everything up and run it easily.

## The Engineering Problem & Solution

### The Problem: "Race Conditions"
If two users hit your server at the exact same nanosecond, a normal Java program might get confused and let both of them in, even if the limit was reached. 

### The Solution: Lua Scripting
We wrote a small script in **Lua**. instead of Java doing the math, we send the math to Redis. Redis promises to do **one thing at a time**. This guarantees that our counter is always 100% accurate, no matter how much traffic we get.

## How to Run

### Prerequisites
* Docker Desktop installed.

### Quick Start
1.  **Clone the repo:**
    ```bash
    git clone [https://github.com/beauuks/rate-limiter.git](https://github.com/beauuks/rate-limiter.git)
    cd rate-limiter
    ```

2.  **Run with Docker:**
    ```bash
    docker-compose up --build
    ```

### What you will see
The app will simulate a "Hacker" trying to send 10 requests instantly. You will see the first 5 succeed, and the rest get blocked.

```text
Request 1: ALLOWED
Request 2: ALLOWED
Request 3: ALLOWED
Request 4: ALLOWED
Request 5: ALLOWED
Request 6: BLOCKED (Too Many Requests)
Request 7: BLOCKED (Too Many Requests)
...