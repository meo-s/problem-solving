# https://www.acmicpc.net/problem/13511

import math
import sys
from collections import deque


def lca_cache(edges, u=1):
    L = math.ceil(math.log2(len(edges) - 1)) + 1
    jumps = [[-1] * L for _ in range(len(edges))]
    costs = [[-1] * L for _ in range(len(edges))]
    depths = [-1] * len(edges)

    jumps[u][0] = u
    costs[u][0] = 0
    depths[u] = 0

    vertices = deque([u])
    while 0 < len(vertices):
        for _ in range(len(vertices)):
            u = vertices.popleft()
            for v, w in edges[u]:
                if depths[v] == -1:
                    jumps[v][0] = u
                    costs[v][0] = w
                    depths[v] = depths[u] + 1
                    vertices.append(v)

    for i in range(1, L):
        for u in range(1, len(edges)):
            jumps[u][i] = jumps[jumps[u][i - 1]][i - 1]
            costs[u][i] = costs[jumps[u][i - 1]][i - 1] + costs[u][i - 1]

    return jumps, costs, depths


def lca_of(u, v, cache):
    jumps, costs, depths = cache
    L = len(jumps[0])

    cost = 0
    if depths[v] < depths[u]:
        for i in reversed(range(L)):
            if depths[v] == depths[u]:
                break
            if depths[v] <= depths[jumps[u][i]]:
                cost += costs[u][i]
                u = jumps[u][i]
    if depths[u] < depths[v]:
        for i in reversed(range(L)):
            if depths[u] == depths[v]:
                break
            if depths[u] <= depths[jumps[v][i]]:
                cost += costs[v][i]
                v = jumps[v][i]

    ancestor = u
    if u != v:
        for i in reversed(range(L)):
            if jumps[u][i] != jumps[v][i]:
                cost += costs[u][i] + costs[v][i]
                u = jumps[u][i]
                v = jumps[v][i]

        ancestor = jumps[u][0]
        cost += costs[u][0] + costs[v][0]

    return ancestor, cost


def nth_parent_of(u, n, jumps):
    for i in reversed(range(len(jumps[u]))):
        if n == 0:
            break
        if 2**i <= n:
            n -= 2**i
            u = jumps[u][i]
    return u


readline = lambda: sys.stdin.readline().rstrip()  # noqa

V = int(readline())
edges = [[] for _ in range(V + 1)]
for _ in range(V - 1):
    u, v, w = map(int, readline().split())
    edges[u].append((v, w))
    edges[v].append((u, w))

cache = lca_cache(edges)
jumps, _, depths = cache
for _ in range(int(readline())):
    q, *args = map(int, readline().split())
    if q == 1:
        _, cost = lca_of(*args, cache)
        print(cost)
    else:
        u, v, k = args
        k -= 1
        lca, _ = lca_of(u, v, cache)

        u2lca = depths[u] - depths[lca]
        if k <= u2lca:
            print(nth_parent_of(u, k, jumps))
        else:
            v2lca = depths[v] - depths[lca]
            print(nth_parent_of(v, v2lca - (k - u2lca), jumps))
