# https://www.acmicpc.net/problem/17352

import sys


def find(parents, u):
    if parents[u] != u:
        parents[u] = find(parents, parents[u])
    return parents[u]


def union(parents, u, v):
    parents[find(parents, u)] = find(parents, v)


sys.setrecursionlimit(3 * 10**5)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
parents = [*range(N + 1)]
for _ in range(N - 2):
    u, v = map(int, readline().split())
    union(parents, u, v)

for i in range(2, N + 1):
    if find(parents, 1) != find(parents, i):
        print(1, i, sep=' ')
        break
