# https://www.acmicpc.net/problem/10775

import sys


def find(parents, u):
    if parents[u] != u:
        parents[u] = find(parents, parents[u])
    return parents[u]


def union(parents, v, u):
    vp = find(parents, v)
    up = find(parents, u)
    parents[vp] = up


sys.setrecursionlimit(10**5)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

G = int(readline())
gates = [*range(G + 1)]

P = [int(readline()) for _ in range(int(readline()))]
n_dockings = 0
for g in P:
    if find(gates, g) == 0:
        break

    gates[find(gates, g)] -= 1
    n_dockings += 1

print(n_dockings)
