# https://www.acmicpc.net/problem/13907

import sys


def bellman_ford(V, edges, u):
    dists = [[float('inf')] * (V + 1) for _ in range(V)]
    dists[u][0] = 0
    for depth in range(V):
        for u, v, w in edges:
            if dists[u][depth] + w < dists[v][depth + 1]:
                dists[v][depth + 1] = dists[u][depth] + w

    return dists


readline = lambda: sys.stdin.readline().rstrip()  # noqa
V, E, K = map(int, readline().split())
S, D = map(int, readline().split())

edges = []
for _ in range(E):
    u, v, w = map(int, readline().split())
    edges.append((u, v, w))
    edges.append((v, u, w))

dp = bellman_ford(V + 1, edges, S)[D]
print(min(dp))
for _ in range(K):
    p = int(readline())
    min_dist = float('inf')
    for i in range(len(dp)):
        dp[i] += i * p
        min_dist = min(min_dist, dp[i])
    print(min_dist)

