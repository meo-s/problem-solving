# https://www.acmicpc.net/problem/1967

import sys

sys.setrecursionlimit(10000)
readline = lambda: sys.stdin.readline().strip()

N = int(readline())
edges = [[] for _ in range(N + 1)]
for _ in range(N - 1):
    v, u, w = map(int, readline().split())
    edges[v].append((u, w))


def dfs(v):
    max_d, top2_r = 0, [0, 0]
    for u, w in edges[v]:
        d, r = dfs(u)
        max_d = max(max_d, d)
        if top2_r[0] < r + w:
            top2_r[0] = r + w
            top2_r.sort()

    return max(max_d, sum(top2_r)), top2_r[-1]


print(dfs(1)[0])
