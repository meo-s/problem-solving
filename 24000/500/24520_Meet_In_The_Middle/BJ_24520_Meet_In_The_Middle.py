# https://www.acmicpc.net/problem/24520

import math
import sys
from collections import deque


def lca_cache(g, u=1):
    V = len(g)
    L = math.ceil(math.log2(V)) + 1

    jumps = [[-1] * L for _ in range(V)]
    costs = [[-1] * L for _ in range(V)]
    depths = [-1] * V

    jumps[u][0] = u
    costs[u][0] = 0
    depths[u] = 0

    vertices = deque([u])
    while 0 < len(vertices):
        u = vertices.popleft()
        for v, w in g[u]:
            if depths[v] == -1:
                jumps[v][0] = u
                costs[v][0] = w
                depths[v] = depths[u] + 1
                vertices.append(v)

    for i in range(1, L):
        for u in range(1, V):
            jumps[u][i] = jumps[jumps[u][i - 1]][i - 1]
            costs[u][i] = costs[jumps[u][i - 1]][i - 1] + costs[u][i - 1]

    return jumps, costs, depths


def lca_of(u, v, cache):
    jumps, costs, depths = cache
    L = len(jumps[0])
    u_cost, v_cost = 0, 0

    for i in reversed(range(L)):
        if depths[u] <= depths[v]:
            break
        if depths[v] <= depths[jumps[u][i]]:
            u_cost += costs[u][i]
            u = jumps[u][i]
    for i in reversed(range(L)):
        if depths[v] <= depths[u]:
            break
        if depths[u] <= depths[jumps[v][i]]:
            v_cost += costs[v][i]
            v = jumps[v][i]

    lca = u
    if u != v:
        for i in reversed(range(L)):
            if jumps[u][i] != jumps[v][i]:
                u_cost += costs[u][i]
                v_cost += costs[v][i]
                u = jumps[u][i]
                v = jumps[v][i]

        u_cost += costs[u][0]
        v_cost += costs[v][0]
        lca = jumps[u][0]

    return lca, u_cost, v_cost


def meet_in_the_middle(u, v, lca, uc, vc, cache):
    if uc == vc:
        return lca

    jumps, costs, depths = cache
    u = [v, u][vc < uc]
    uc, vc = 0, uc + vc
    for i in reversed(range(len(jumps[u]))):
        if uc == vc:
            break
        dc = costs[u][i]
        if depths[lca] <= depths[jumps[u][i]] and uc + dc <= vc - dc:
            uc += dc
            vc -= dc
            u = jumps[u][i]

    return [-1, u][uc == vc]


readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, Q = map(int, readline().split())
g = [[] for _ in range(N + 1)]
for _ in range(N - 1):
    u, v, w = map(int, readline().split())
    g[u].append((v, w))
    g[v].append((u, w))

cache = lca_cache(g)
for _ in range(Q):
    u, v = map(int, readline().split())
    print(meet_in_the_middle(u, v, *lca_of(u, v, cache), cache))
