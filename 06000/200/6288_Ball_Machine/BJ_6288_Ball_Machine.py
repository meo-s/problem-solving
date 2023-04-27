# https://www.acmicpc.net/problem/6288

import math
import sys
from operator import itemgetter
from heapq import heappop
from heapq import heappush


def pull_up(minimums, edges, u):
    minimums[u] = u
    for v in edges[u]:
        minimums[u] = min(minimums[u], pull_up(minimums, edges, v))

    return minimums[u]


def prioritize(indices, priors, edges, u, index=1):
    for v, _ in sorted(((v, priors[v]) for v in edges[u]), key=itemgetter(1)):
        index = prioritize(indices, priors, edges, v, index) + 1

    indices[u] = index
    return index


sys.setrecursionlimit(2 * 10**5)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

V, Q = map(int, readline().split())
root = 0
edges = [[] for _ in range(V + 1)]
parents = [-1] * (V + 1)
for v in range(1, V + 1):
    u = int(readline())
    root = v if u == 0 else root
    edges[u].append(v) if u != 0 else None
    parents[v] = u if u != 0 else v

minimums = [-1] * (V + 1)
pull_up(minimums, edges, root)

priors = [-1] * (V + 1)
prioritize(priors, minimums, edges, root)

L = math.ceil(math.log2(V)) + 1
jumps = [[parents[i]] * L for i in range(V + 1)]
costs = [[1] * L for i in range(V + 1)]
costs[root][0] = 0
for i in range(1, L):
    for u in range(1, V + 1):
        jumps[u][i] = jumps[jumps[u][i - 1]][i - 1]
        costs[u][i] = costs[jumps[u][i - 1]][i - 1] + costs[u][i - 1]

empties = sorted(((priors[i], i) for i in range(1, V + 1)), key=itemgetter(0))
in_empties = [True] * (V + 1)
for _ in range(Q):
    q, x = map(int, readline().split())
    if q == 1:
        ans = -1
        while 0 < x:
            x -= 1
            _, ans = heappop(empties)
            in_empties[ans] = False

        print(ans)
    else:
        dist = 0
        for i in reversed(range(L)):
            if not in_empties[jumps[x][i]]:
                dist += costs[x][i]
                x = jumps[x][i]

        heappush(empties, (priors[x], x))
        in_empties[x] = True
        print(dist)
