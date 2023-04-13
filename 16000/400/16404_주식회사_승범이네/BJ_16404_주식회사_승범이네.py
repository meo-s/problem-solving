# https://www.acmicpc.net/problem/16404

import sys


def fenwick_tree_update(tree, index, diff):
    while index < len(tree):
        tree[index] += diff
        index += index & -index


def fenwick_tree_query(tree, index):
    value = 0
    while 0 < index:
        value += tree[index]
        index -= index & -index

    return value


def dfs(edges, u, beg, end, depth=1):
    beg[u] = depth
    for v in edges[u]:
        depth = dfs(edges, v, beg, end, depth + 1)

    end[u] = depth
    return end[u]


sys.setrecursionlimit(10**6)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, M = map(int, readline().split())

edges = [[] for _ in range(N + 1)]
for v, p in enumerate(map(int, readline().split()), start=1):
    edges[p].append(v) if p != -1 else None

beg, end = [0]*(N+1), [0]*(N+1)
dfs(edges, 1, beg, end)

tree = [0] * (N + 1)
for _ in range(M):
    q, *args = map(int, readline().split())
    if q == 1:
        u, diff = args
        fenwick_tree_update(tree, beg[u], diff)
        fenwick_tree_update(tree, end[u] + 1, -diff)
    else:
        print(fenwick_tree_query(tree, beg[args[0]]))

