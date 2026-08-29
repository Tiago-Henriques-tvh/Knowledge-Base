# Networking Fundamentals — Complete Study Guide

---

## Table of Contents

1. [Fundamental Concepts](#1-fundamental-concepts)
2. [Reference Communication Protocols](#2-reference-communication-protocols)
3. [Essential Network Tools and Commands (Linux)](#3-essential-network-tools-and-commands-linux)
4. [Practical Networking Scenarios](#4-practical-networking-scenarios)
5. [Troubleshooting Network Issues](#5-troubleshooting-network-issues)

---

## 1. Fundamental Concepts

### 1.1 LAN vs WAN

- **LAN (Local Area Network):** The network inside your home. Your Raspberry Pi, smartphone, laptop, and smart TV all sit on the same LAN, connected via your router's Wi-Fi or Ethernet ports. Devices communicate directly with each other at high speed using **private IP addresses** (like `192.168.1.x` or `10.0.0.x`).
- **WAN (Wide Area Network):** Everything _outside_ your home network — the internet at large. Your router acts as the boundary between your LAN and the WAN.

> **Practical Analogy:** Think of your LAN as the interior of your house and the WAN as the entire city outside. Your router is the front door.

### 1.2 Routers & NAT

Your router performs two critical functions:

1. **Routing:** Directs data traffic between your LAN and the internet (WAN).
2. **NAT (Network Address Translation):** Your ISP gives your router only **one** public IP address. For all your internal devices to reach the internet simultaneously, NAT maps outgoing connections and ensures responses return to the correct device.

**How NAT Works:**

```
Your Home Network (LAN)
┌─────────────────────────────────┐
│ Laptop          (192.168.1.10)  │
│  Request to google.com          │
│         ↓                        │
│   NAT Table Entry:              │
│   192.168.1.10:12345 ← → ISP:56789
│         ↓                        │
│ ROUTER (NAT Engine)             │
│   Public IP: 203.45.67.89       │
└─────────────────────────────────┘
         ↓
    INTERNET
   Google sees request from 203.45.67.89
   (not 192.168.1.10)
```

> **Why This Matters:** Because your Raspberry Pi sits behind NAT without its own public IP, the internet cannot reach it directly. This is why you need solutions like Port Forwarding or Tailscale.

### 1.3 IP Addresses: Private vs Public vs Tailscale

| Type             | Example          | Where Used                     | Access From                  | Purpose                        |
| ---------------- | ---------------- | ------------------------------ | ---------------------------- | ------------------------------ |
| **Private IP**   | `192.168.1.50`   | Inside your LAN only           | Only local devices           | Internal device identification |
| **Public IP**    | `203.45.67.89`   | Assigned by ISP to your router | Entire internet              | External identification        |
| **Tailscale IP** | `100.75.141.127` | Only within your tailnet       | Only other Tailscale devices | Secure overlay network         |

**The Three Layers Explained:**

```
Layer 3 (WAN - Public Internet):
203.45.67.89 (your home's public face)

Layer 2 (NAT/Router):
192.168.1.x range
    ├─ 192.168.1.10 (Laptop)
    ├─ 192.168.1.50 (Raspberry Pi)
    └─ 192.168.1.100 (Phone on Wi-Fi)

Layer 1 (Tailscale Mesh - Overlay):
100.x.x.x range
    ├─ 100.75.141.127 (Pi-hole on Tailscale)
    ├─ 100.92.241.35 (Phone on Tailscale)
    └─ 100.95.87.105 (Windows PC on Tailscale)
```

### 1.4 Ports (Ports)

An IP address locates the **device**. A **port** locates the specific **service** running on that device.

- Ports 0–1,023: Well-known, reserved for standard services
- Ports 1,024–49,151: Registered ports (for specific applications)
- Ports 49,152–65,535: Ephemeral/dynamic (temporary, assigned by the OS)

**Common Ports You'll Encounter:**

| Port | Protocol   | Use Case               | Example                                      |
| ---- | ---------- | ---------------------- | -------------------------------------------- |
| 22   | SSH        | Remote terminal access | `ssh user@192.168.1.50`                      |
| 53   | DNS        | Domain name resolution | Pi-hole listens here to block ads            |
| 80   | HTTP       | Unencrypted web        | Admin panel (dangerous on internet)          |
| 443  | HTTPS      | Encrypted web          | Secure admin panel                           |
| 3389 | RDP        | Windows Remote Desktop | Remote desktop control                       |
| 5432 | PostgreSQL | Database access        | Docker containers communicate here           |
| 6379 | Redis      | In-memory cache        | Docker containers communicate here           |
| 8080 | HTTP (alt) | Development web apps   | Docker container web services often use this |

> **Practical Analogy:** IP is the street address of a building; the port is the apartment number inside.

### 1.5 Port Forwarding

By default, NAT blocks external connections to devices on your LAN — which is actually good for security (your Pi isn't exposed to the entire internet).

**Port Forwarding:** A rule you configure on your router that says, _"Any traffic arriving from the internet on port X, send it to device Y on my LAN."_

**Example:** Forward port 8080 (external) → Raspberry Pi's port 80 (internal)

```
Internet Traffic:
203.45.67.89:8080 (from outside world)
        ↓ (Router sees this)
192.168.1.50:80 (Raspberry Pi)
```

**The Trade-off:**

- ✓ Accessible from anywhere
- ✗ Opens a permanent hole in your home network
- ✗ Exposes your Raspberry Pi directly to the internet
- ✗ Needs manual configuration; breaks if your ISP changes your public IP

**The Tailscale Alternative:**

- ✓ Accessible from anywhere
- ✓ No open ports on your router
- ✓ Encrypted, private tunnel
- ✓ Works even if your ISP changes your public IP
- ✓ No complex configuration needed

### 1.6 Traditional VPNs vs Mesh VPNs (Tailscale)

**Traditional VPN (hub-and-spoke):**

```
        ┌─────────────┐
        │ VPN Server  │
        │ (Central)   │
        └─────────────┘
         ↑     ↑     ↑
         │     │     │
    Laptop  Phone  Tablet

All traffic funnels through one central point
```

**Mesh VPN (Tailscale/WireGuard - peer-to-peer):**

```
    Laptop ←─────→ Phone
      ↑             ↑
      └─── Pi ──────┘

Every device connects directly to every other device
(encrypted end-to-end, no central point)
```

**Comparison:**

| Aspect          | Traditional VPN                          | Mesh VPN (Tailscale)                         |
| --------------- | ---------------------------------------- | -------------------------------------------- |
| **Routing**     | Everything through central server        | Direct peer-to-peer connections              |
| **Latency**     | Higher (all traffic goes through server) | Lower (direct connections)                   |
| **Scalability** | Server becomes bottleneck                | Scales easily with more devices              |
| **Setup**       | Complex, expensive servers               | Simple, coordinates through SaaS             |
| **Privacy**     | Server operator sees all traffic         | Only WireGuard layer can see encrypted data  |
| **Speed**       | Limited by server bandwidth              | Limited by internet connection between peers |

### 1.7 Exit Nodes (How Tailscale Makes You Appear "at Home")

An **exit node** is a Tailscale feature where one device on your tailnet volunteers to route _all_ internet traffic from another device.

**Normal Tailscale Usage:**

```
Phone on 4G:
┌──────────────────────────────────┐
│ Tailscale traffic → Pi (100.x.x) │ (secure)
│ Regular internet → ISP directly   │ (normal internet)
└──────────────────────────────────┘
Phone's ISP sees: Google searches, YouTube, etc.
```

**With Exit Node Enabled:**

```
Phone on 4G:
┌──────────────────────────────────────────────┐
│ ALL traffic (Tailscale + regular internet)   │
│         ↓ encrypted tunnel ↓                  │
│    Raspberry Pi (exit node)                  │
│         ↓                                     │
│  Home Router → ISP → Internet                │
└──────────────────────────────────────────────┘
Google sees: Request from home's public IP
(not from mobile carrier IP)
```

**Why You'd Use This:**

- Access home-only services while traveling
- Bypass geo-restrictions (site thinks you're home)
- Better privacy (traffic exits through your home, not ISP)
- Single point of security filtering (Pi-hole blocks ads for all outgoing traffic)

### 1.8 Practical Networking Diagram

```
┌────────────────────────────────────────────────────────────────┐
│                     ENTIRE SETUP VISUALIZATION                 │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│  INTERNET (WAN)                                                │
│  ┌──────────────────┐                                          │
│  │ Public IP:       │                                          │
│  │ 203.45.67.89     │                                          │
│  └────────┬─────────┘                                          │
│           │                                                    │
│  ┌────────▼──────────────────┐                                 │
│  │   HOME ROUTER (NAT)       │                                 │
│  │ Private IP: 192.168.1.1   │                                 │
│  └─┬──────────────────────┬──┘                                 │
│    │                      │                                    │
│  ┌─▼──────────────────┐ ┌─▼──────────────────────┐             │
│  │  LAN (192.168.1.x) │ │                        │             │
│  │                    │ │  DOCKER + TAILSCALE    │             │
│  │ - Laptop           │ │  (Raspberry Pi)        │             │
│  │ - Phone (Wi-Fi)    │ │                        │             │
│  │ - Smart TV         │ │ ┌────────────────────┐ │             │
│  │                    │ │ │  Tailscale         │ │             │
│  └────────────────────┘ │ │  100.75.141.127    │ │             │
│                         │ ├────────────────────┤ │             │
│                         │ │  Pi-hole DNS       │ │             │
│                         │ │  Port 53           │ │             │
│                         │ └────────────────────┘ │             │
│                         │                        │             │
│                         │  Services accessible:  │             │
│                         │  - http://100.75...    │             │
│                         │  - DNS @ 100.75...     │             │
│                         │  - Exit node enabled   │             │
│                         └────────────────────────┘             │
│                                                                │
│  TAILSCALE MESH (100.x.x.x)                                    │
│  ┌───────────────────────────────────────────────┐             │
│  │                                               │             │
│  │  100.75.141.127 (Pi)    100.92.241.35 (Phone) │             │
│  │         ↔ ──────────────── ↔                  │             │
│  │      (direct encrypted tunnel)                │             │
│  │                                               │             │
│  │  100.95.87.105 (Windows PC)                   │             │
│  │         ↔ ─────────────────────────────── ↔   │             │
│  │                                               │             │
│  └───────────────────────────────────────────────┘             │
│     (Only Tailscale members can see these IPs)                 │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

---

## 2. Reference Communication Protocols

This table organizes the main protocols you'll encounter while managing Linux systems and networking:

### 2.1 Protocol Reference Table

| Category                    | Protocol          | Default Port | OSI Layer              | What It Does (Practical Summary)                                                                                          | Key Characteristic                |
| :-------------------------- | :---------------- | :----------- | :--------------------- | :------------------------------------------------------------------------------------------------------------------------ | :-------------------------------- |
| **Infrastructure**          | **IP**            | N/A          | Layer 3 (Network)      | Handles addressing and routing packets from source to destination                                                         | Fundamental transport layer       |
|                             | **TCP**           | N/A          | Layer 4 (Transport)    | **Reliable, ordered delivery** — guarantees all data arrives in order (connection-oriented)                               | "Guaranteed delivery"             |
|                             | **UDP**           | N/A          | Layer 4 (Transport)    | Focused on **speed and low latency** — no confirmation (connectionless). Used by VPNs and streaming                       | "Fire and forget"                 |
| **Access & Web**            | **HTTP**          | 80 (TCP)     | Layer 7 (Application)  | Transmits web pages **without encryption**                                                                                | Insecure, human-readable          |
|                             | **HTTPS**         | 443 (TCP)    | Layer 7 (Application)  | Transmits web pages **securely** (HTTP over TLS encryption)                                                               | Secure, encrypted                 |
|                             | **SSH**           | 22 (TCP)     | Layer 7 (Application)  | **Secure remote terminal access** to servers via CLI                                                                      | Standard remote administration    |
| **VPN & Tunneling**         | **WireGuard**     | 51820 (UDP)  | Layer 3/4              | Ultra-fast, minimal VPN protocol that powers Tailscale                                                                    | Modern, efficient                 |
|                             | **TLS/SSL**       | Varies       | Layer 6 (Presentation) | **Encryption protocol** used by HTTPS, SSH, and many other services                                                       | Security foundation               |
| **File Sharing**            | **SMB (Samba)**   | 445 (TCP)    | Layer 7 (Application)  | **Share files and disks** on the network between Linux, Windows, and macOS                                                | Windows file sharing on Linux     |
|                             | **SFTP**          | 22 (TCP)     | Layer 7 (Application)  | **Secure file transfer** using SSH tunnel                                                                                 | Secure, username/password or keys |
| **Service Discovery**       | **DNS**           | 53 (UDP/TCP) | Layer 7 (Application)  | The "phonebook" of the network — **translates domain names** (`google.com`) into IPs. Foundation of Pi-hole's ad blocking | Everything depends on this        |
|                             | **mDNS**          | 5353 (UDP)   | Layer 7 (Application)  | **Local network discovery** — devices announce themselves on LAN (`.local` names)                                         | Linux/Mac standard                |
| **Synchronization**         | **NTP**           | 123 (UDP)    | Layer 7 (Application)  | **Synchronizes system clock** with high-precision servers to the millisecond                                              | Keeps time accurate               |
| **Remote Access**           | **RDP**           | 3389 (TCP)   | Layer 7 (Application)  | **Windows Remote Desktop** — full GUI control over Windows machines                                                       | Windows standard                  |
|                             | **VNC**           | 5900 (TCP)   | Layer 7 (Application)  | **Virtual Network Computing** — generic remote desktop (cross-platform)                                                   | Linux/Mac alternative             |
| **Database & Applications** | **PostgreSQL**    | 5432 (TCP)   | Layer 7 (Application)  | Powerful relational database (used in many Docker containers)                                                             | Production database               |
|                             | **MySQL/MariaDB** | 3306 (TCP)   | Layer 7 (Application)  | Popular relational database (lighter than PostgreSQL)                                                                     | Web app standard                  |
|                             | **Redis**         | 6379 (TCP)   | Layer 7 (Application)  | In-memory cache/data store (extremely fast, used by Docker containers)                                                    | Caching layer                     |
|                             | **MongoDB**       | 27017 (TCP)  | Layer 7 (Application)  | NoSQL database (document-based, JSON-like)                                                                                | Modern web apps                   |

### 2.2 OSI Model Quick Reference

```
Layer 7 - APPLICATION (DNS, HTTP/HTTPS, SSH, FTP, SMTP)
                ↑
Layer 6 - PRESENTATION (Encryption: TLS/SSL)
                ↑
Layer 5 - SESSION (Manages connection)
                ↑
Layer 4 - TRANSPORT (TCP, UDP - reliability and delivery)
                ↑
Layer 3 - NETWORK (IP, Routing, NAT)
                ↑
Layer 2 - DATA LINK (Ethernet, Wi-Fi, MAC addresses)
                ↑
Layer 1 - PHYSICAL (Cables, Radio waves)
```

**Practical Examples:**

- **Your web browser:** Works at Layer 7 (HTTP/HTTPS), uses Layer 4 (TCP), Layer 3 (IP), Layer 2 (Wi-Fi)
- **Ping command:** Tests Layer 3 (IP connectivity) using ICMP
- **Tailscale:** Operates at Layer 3/4 (UDP WireGuard tunnel)
- **Pi-hole:** Sits at Layer 7 (DNS) and Layer 4 (UDP port 53)

---

## 3. Essential Network Tools and Commands (Linux)

When managing Debian servers or services in containers (like Docker), these commands save your day for diagnosing network issues:

### 3.1 `ip a` (or `ip address`)

**What it does:** Shows all active network interfaces (Ethernet, Wi-Fi, Docker virtual interfaces, Tailscale tunnel) and their assigned private IPs.

**Practical use:** Discover your Raspberry Pi's current local IP on your LAN (usually `eth0` for Ethernet or `wlan0` for Wi-Fi), or check if the `tailscale0` tunnel is active.

```bash
ip a

# Output example:
# 1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536
#     inet 127.0.0.1/8 scope host lo
# 2: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500
#     inet 192.168.1.50/24 brd 192.168.1.255 scope global eth0
# 3: docker0: <UP,BROADCAST,MULTICAST> mtu 1500
#     inet 172.17.0.1/16 scope global docker0
# 4: tailscale0: <POINTOPOINT,UP,LOWER_UP> mtu 1280
#     inet 100.75.141.127/32 scope global tailscale0
```

**Key info:**

- `lo` = Loopback (localhost, always 127.0.0.1)
- `eth0` = Ethernet (wired connection)
- `wlan0` = Wi-Fi
- `docker0` = Docker's internal bridge network
- `tailscale0` = Your Tailscale VPN tunnel

### 3.2 `ip route` (or `route`)

**What it does:** Shows the routing table — how your system decides where to send packets based on destination IP.

**Practical use:** Confirm that traffic destined for `100.x.x.x` (Tailscale IPs) routes through the `tailscale0` interface, or that your default gateway points to the router.

```bash
ip route

# Output example:
# default via 192.168.1.1 dev eth0 proto dhcp metric 100
#   (default route: send everything to router 192.168.1.1)
# 100.64.0.0/10 dev tailscale0 proto kernel scope link src 100.75.141.127
#   (Tailscale IPs route through tailscale0)
# 172.17.0.0/16 dev docker0 proto kernel scope link src 172.17.0.1
#   (Docker IPs route through docker0)
```

### 3.3 `ping`

**What it does:** Sends test packets (ICMP) to an IP or domain to check if it's online and measure latency.

**Practical use:** Verify your Tailscale phone can reach the Pi's Tailscale IP, or check internet connectivity.

```bash
# Test reachability
ping 100.75.141.127
ping google.com
ping 8.8.8.8

# Limit to 4 packets
ping -c 4 192.168.1.50

# Output example:
# PING 100.75.141.127 (100.75.141.127) 56(84) bytes of data.
# 64 bytes from 100.75.141.127: icmp_seq=1 ttl=64 time=8.52 ms
# 64 bytes from 100.75.141.127: icmp_seq=2 ttl=64 time=7.89 ms
#
# --- 100.75.141.127 statistics ---
# 4 packets transmitted, 4 received, 0% packet loss, time 3004ms
# rtt min/avg/max/stddev = 7.89/8.12/8.52/0.25 ms
```

**What to look for:**

- `0% packet loss` = Connection is solid
- `X% packet loss` = Unreliable connection
- `time = Xms` = Latency (lower is better)

### 3.4 `dig` / `nslookup`

**What it does:** Queries DNS servers directly to see how a domain is being resolved.

**Practical use:** Test if Pi-hole is working and correctly blocking ads. Force a query through the Pi's DNS.

```bash
# Query your ISP's default DNS
dig google.com

# Query specifically through Pi-hole (192.168.1.50)
dig @192.168.1.50 google.com

# Query for ads that Pi-hole should block
dig @192.168.1.50 doubleclick.net

# Detailed output
dig +short google.com  # Just the IP
dig -x 8.8.8.8        # Reverse lookup (IP to domain)

# Example output (Pi-hole blocking):
# ; <<>> DiG 9.16.1-Ubuntu <>> @192.168.1.50 doubleclick.net
# ; (1 server found)
# ;; global options: +cmd
# ;; Got answer:
# ;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 12345
# ;; flags: qr aa rd ra; QUERY: 1, ANSWER: 1, AUTHORITY: 0, ADDITIONAL: 0
#
# ;; QUESTION SECTION:
# ;doubleclick.net.         IN      A
#
# ;; ANSWER SECTION:
# doubleclick.net.  3600    IN      A       0.0.0.0
#  ↑ (0.0.0.0 = blocked by Pi-hole)
```

### 3.5 `ss -tulpn` (improved `netstat`)

**What it does:** Shows all network ports currently open and "listening" (_listening_) on your system, plus which process is using them.

**Practical use:** Verify Pi-hole is listening on port 53 for DNS, check if Samba is on port 445, or find which process is using a specific port.

```bash
# Show all listening ports
ss -tulpn

# Filter to a specific port
sudo ss -tulpn | grep :53
sudo ss -tulpn | grep :3389

# Output example:
# Netid  State  Recv-Q Send-Q  Local Address:Port    Peer Address:Port  Process
# tcp    LISTEN 0      128         0.0.0.0:22             0.0.0.0:*      users:(("sshd",pid=1234,fd=3))
# tcp    LISTEN 0      128    127.0.0.1:5432            0.0.0.0:*      users:(("postgres",pid=5678,fd=5))
# udp    NONE   0      0      0.0.0.0:53             0.0.0.0:*      users:(("dnsmasq",pid=9999,fd=3))
```

**Flags explained:**

- `t` = TCP
- `u` = UDP
- `l` = LISTEN (show only listening sockets)
- `p` = Processes (show which process owns the port)
- `n` = Numeric (show IPs, not hostnames)

### 3.6 `curl` / `wget`

**What it does:** Downloads files or makes HTTP requests from the command line.

**Practical use:** Test if a web service is responding, fetch a file, or check HTTP headers.

```bash
# Fetch headers only (fastest check)
curl -I http://192.168.1.50:80/admin
curl -I https://100.75.141.127/admin

# Check response code only
curl -o /dev/null -s -w "%{http_code}" http://localhost:8080

# Download a file
curl -O https://example.com/file.tar.gz

# Piping output
curl -s http://example.com/api | jq .

# Example output:
# HTTP/1.1 200 OK
# Content-Type: text/html
# Content-Length: 1234
# Server: nginx/1.18.0
```

### 3.7 `nc` (netcat) — The "network Swiss Army knife"

**What it does:** Opens raw network connections, tests port connectivity, or transfers data between machines.

**Practical use:** Test if a specific port is reachable from your machine (before trying complex applications like Remmina).

```bash
# Test if port 3389 is open on Windows machine
nc -zv 100.95.87.105 3389
# z = scan (don't send data, just check connection)
# v = verbose (show status)

# Output examples:
# Connection to 100.95.87.105 3389 port [tcp/*] succeeded!
# Connection refused
# Connection timed out

# Alternative (if nc not installed): timeout + bash
timeout 3 bash -c "</dev/tcp/100.95.87.105/3389" && echo "PORT OPEN" || echo "PORT CLOSED/UNREACHABLE"
```

### 3.8 `systemctl status`

**What it does:** Shows the status of system services (running, stopped, failed, etc.).

**Practical use:** Check if Docker, Tailscale, or other important services are actually running.

```bash
# Check Docker
systemctl status docker

# Check Tailscale (if installed natively)
systemctl status tailscaled

# Enable service on boot
sudo systemctl enable docker

# Start/stop/restart
sudo systemctl start docker
sudo systemctl stop docker
sudo systemctl restart docker

# View recent logs
journalctl -u docker -n 50  # Last 50 lines
journalctl -u docker -f      # Follow (live tail)
```

### 3.9 `lsof` (list open files)

**What it does:** Shows all open files and network sockets, and which process owns them.

**Practical use:** Find which process is using a specific port, or see all network connections for a process.

```bash
# Find what's using port 8080
sudo lsof -i :8080

# Find all network connections for a process
sudo lsof -p 1234  # PID 1234

# Find all listening sockets
sudo lsof -i -P -n | grep LISTEN

# Output example:
# COMMAND   PID     USER   FD   TYPE       DEVICE SIZE/OFF NODE NAME
# docker   1234    root   12u  IPv4  0x12345abcde      0t0  TCP *:8080 (LISTEN)
```

### 3.10 `iptables` / `ufw` — Firewall

**What it does:** Manages firewall rules to allow/block traffic on specific ports.

**Practical use:** Allow SSH from outside, block a specific IP, or verify existing firewall rules.

```bash
# Check firewall status (ufw = simple interface)
sudo ufw status
sudo ufw status verbose

# Allow SSH
sudo ufw allow 22/tcp

# Allow from specific IP
sudo ufw allow from 192.168.1.50 to any port 22

# Deny port
sudo ufw deny 3389/tcp

# Enable/disable firewall
sudo ufw enable
sudo ufw disable

# Check detailed rules (low-level iptables)
sudo iptables -L -n
```

### 3.11 Quick Diagnostic Checklist

Use this checklist when something isn't working:

```bash
# 1. Check your local IP
ip a | grep "inet "

# 2. Check if router is reachable
ping 192.168.1.1

# 3. Check internet connectivity
ping 8.8.8.8

# 4. Check if Tailscale is running
systemctl status tailscaled
# or in Docker:
docker exec pihole-tailscale tailscale status

# 5. Test Tailscale connectivity
docker exec pihole-tailscale tailscale ping 100.95.87.105

# 6. Check if target service is listening
sudo ss -tulpn | grep :3389

# 7. Test port connectivity
nc -zv 100.95.87.105 3389

# 8. Check for firewall blocking
sudo ufw status

# 9. Check routing
ip route | grep "100\|tailscale"

# 10. View recent errors
journalctl -xe
```

---

## 4. Practical Networking Scenarios

### Scenario 1: "My Raspberry Pi is offline, how do I find its IP?"

**Situation:** You just turned on the Pi but don't remember its IP address.

**Solution:**

```bash
# Method 1: From another machine on your LAN
arp-scan --localnet  # Scans entire subnet, slow
ping -c 1 192.168.1.255  # Broadcast ping (some systems respond)

# Method 2: Check your router's DHCP client list
# Log into 192.168.1.1 (your router) → check connected devices

# Method 3: Once you find it
ping 192.168.1.50
ssh user@192.168.1.50
```

### Scenario 2: "Tailscale connection works from phone but not from PC"

**Situation:** Your phone can `ping 100.75.141.127` but your Windows PC can't.

**Troubleshooting:**

```bash
# On Windows, test TCP connectivity (not just ICMP ping)
# Ping uses ICMP, but RDP uses TCP — Windows may block ICMP while allowing TCP

nc -zv 100.75.141.127 3389  # Test port 3389 specifically
```

### Scenario 3: "Pi-hole isn't blocking ads"

**Situation:** Devices are using Pi-hole (port 53) but ads still appear.

**Troubleshooting:**

```bash
# Verify Pi-hole is listening on port 53
sudo ss -tulpn | grep :53

# Query it directly
dig @192.168.1.50 google.com    # Should work
dig @192.168.1.50 doubleclick.net  # Should return 0.0.0.0

# Check if it's being bypassed
# Devices might be using different DNS
nslookup google.com   # Might use ISP DNS, not Pi-hole
# Set device DNS to 192.168.1.50 or 100.75.141.127 (Tailscale)
```

### Scenario 4: "Docker containers can't reach each other"

**Situation:** Your web app container can't connect to the database container.

**Troubleshooting:**

```bash
# Check they're on same network
docker network ls
docker inspect app-network | grep -A 10 Containers

# From within app container, test connectivity
docker exec app-container ping db-container
docker exec app-container nc -zv db-container 5432

# Check DNS inside container
docker exec app-container nslookup db-container

# Common fixes:
# 1. Both services must be on same docker-compose.yml network
# 2. Use service name (from compose file), not container name
# 3. Check depends_on: [db] for startup ordering
```

---

## 5. Troubleshooting Network Issues

### Issue: "Connection timed out"

**What it means:** TCP connection couldn't be established within the timeout period.

**Common causes and fixes:**

```bash
# 1. Service not running
sudo ss -tulpn | grep :PORT_NUMBER
systemctl status SERVICE_NAME

# 2. Firewall blocking
sudo ufw status
sudo ufw allow PORT/tcp

# 3. Wrong IP/port
# Double-check target IP and port
ping TARGET_IP
nc -zv TARGET_IP PORT

# 4. Network path broken
# Check routing
ip route
traceroute TARGET_IP
```

### Issue: "Connection refused"

**What it means:** The connection reached the machine, but no service is listening on that port.

**Fixes:**

```bash
# Check if service is actually running
systemctl status SERVICE
docker ps | grep SERVICE

# Check if it's listening on the correct port
sudo ss -tulpn | grep SERVICE

# Check if it's listening on wrong interface (localhost only)
netstat -an | grep LISTEN
# If you see "127.0.0.1:PORT" instead of "0.0.0.0:PORT",
# the service is only accessible from that machine
```

### Issue: "DNS resolution fails"

**What it means:** Domain names aren't resolving to IPs.

**Fixes:**

```bash
# Check your DNS configuration
cat /etc/resolv.conf

# Test DNS directly
nslookup google.com
dig google.com

# Switch to Google's public DNS temporarily
echo "nameserver 8.8.8.8" | sudo tee /etc/resolv.conf

# Check if Pi-hole is resolving correctly
dig @192.168.1.50 google.com
```

### Issue: "Packet loss or high latency"

**What it means:** Some packets aren't arriving, or they're taking a long time.

**Diagnosis and fixes:**

```bash
# Continuous ping to see pattern
ping -c 100 TARGET_IP

# Check for network interface errors
ethtool -S eth0 | grep -i error

# Check Wi-Fi signal strength (if using WLAN)
iwconfig wlan0 | grep Signal

# Test different route
traceroute TARGET_IP

# Bandwidth test
iperf3 -c TARGET_IP  # Requires iperf3 server running on target
```

### Issue: "Can't SSH into Raspberry Pi"

**What it means:** SSH connection fails or times out.

**Fixes:**

```bash
# 1. Check SSH service is running
systemctl status ssh

# 2. Check SSH port (default 22)
sudo ss -tulpn | grep :22

# 3. Test connectivity to port 22
nc -zv 192.168.1.50 22

# 4. Try with verbose output
ssh -vvv user@192.168.1.50
# Look for where it fails

# 5. Check firewall
sudo ufw status
sudo ufw allow 22/tcp

# 6. Check SSH config
cat /etc/ssh/sshd_config | grep -E "^Port|^PermitRootLogin|^PasswordAuthentication"

# 7. Restart SSH service
sudo systemctl restart ssh
```

### Issue: "Docker container can't access internet"

**What it means:** Container can't resolve DNS or reach external hosts.

**Fixes:**

```bash
# Test from inside container
docker exec mycontainer ping 8.8.8.8
docker exec mycontainer curl -I https://google.com

# Check container DNS
docker exec mycontainer cat /etc/resolv.conf
# Should show host's DNS servers

# Restart Docker daemon
sudo systemctl restart docker

# Check Docker network
docker network inspect bridge
docker inspect mycontainer | grep -A 10 NetworkSettings

# Rebuild without caching
docker-compose down
docker-compose up --build
```

---

## Reference: Common Ports to Remember

```
22   - SSH (Remote terminal)
53   - DNS (Domain resolution)
80   - HTTP (Web)
443  - HTTPS (Secure web)
3306 - MySQL
3389 - Windows RDP
5432 - PostgreSQL
5900 - VNC (Remote desktop)
6379 - Redis
8080 - HTTP (alternate, often Docker)
27017 - MongoDB
```

---

## Final Checklist: Know Before Troubleshooting

- What's the **source IP** trying to connect?
- What's the **destination IP and port**?
- What **service** is supposed to be listening?
- Is the service actually **running** (`systemctl status` / `docker ps`)?
- Is the port **open** (`ss -tulpn`, `nc -zv`)?
- Is the **firewall** blocking it (`ufw status`)?
- Is there a **routing issue** (`ip route`, `traceroute`)?
- Are they on the **same network** (Docker, Tailscale, LAN)?

**Always start with `ping` (Layer 3) before testing application-level protocols (Layer 7).**
