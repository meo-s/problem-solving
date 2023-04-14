# https://www.acmicpc.net/problem/14268

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
    return depth


sys.setrecursionlimit(10**5 + 1)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, Q = map(int, readline().split())
edges = [[] for _ in range(N + 1)]
for v, u in enumerate(map(int, readline().split()), start=1):
    edges[u].append(v) if 0 < u else None

beg, end = [0] * (N + 1), [0] * (N + 1)
dfs(edges, 1, beg, end)

tree = [0] * (N + 1)
for _ in range(Q):
    q, *args = map(int, readline().split())
    if q == 1:
        i, w = args
        fenwick_tree_update(tree, beg[i], w)
        fenwick_tree_update(tree, end[i] + 1, -w)
    else:
        print(fenwick_tree_query(tree, beg[args[0]]))
