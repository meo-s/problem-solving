# https://www.acmicpc.net/problem/2820

import sys


def update(tree, index, value):
    while index < len(tree):
        tree[index] += value
        index += index & -index


def query(tree, index):
    value = 0
    while 0 < index:
        value += tree[index]
        index -= index & -index
    return value


def dfs(graph, u, a, beg, end, depth=1):
    beg[u] = depth
    for v, w in graph[u]:
        a[depth + 1] = w
        depth = dfs(graph, v, a, beg, end, depth + 1)
    end[u] = depth
    return end[u]


sys.setrecursionlimit(5*(10**5))
readline = lambda: sys.stdin.readline().rstrip()  # noqa
N, Q = map(int, readline().split())

a = [None] * (N + 1)
a[1] = int(readline())

graph = [[] for _ in range(N + 1)]
for i in range(2, N + 1):
    w, p = map(int, readline().split())
    graph[p].append((i, w))

beg, end = [0] * (N + 1), [0] * (N + 1)
dfs(graph, 1, a, beg, end)

fenwick_tree = [0] * (N + 1)
update(fenwick_tree, 1, a[1])
for i in range(2, N + 1):
    update(fenwick_tree, i, a[i] - a[i - 1])

for _ in range(Q):
    q, *args = readline().split()
    if q == 'u':
        print(query(fenwick_tree, beg[int(args[0])]))
    else:
        u, w = map(int, args)
        update(fenwick_tree, beg[u] + 1, w)
        update(fenwick_tree, end[u] + 1, -w) if end[u] < N else None
