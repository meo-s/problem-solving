# https://www.acmicpc.net/problem/8980

import sys
from heapq import heappop
from heapq import heappush


def readline():
    return sys.stdin.readline().rstrip()


N, C = map(int, readline().split())

freights = [[] for _ in range(N + 1)]
for _ in range(int(readline())):
    u, v, w = map(int, readline().split())
    heappush(freights[v], (-u, w))

n_remains = [C] * (N + 1)
n_deliverd = 0
for i in range(N + 1):
    while 0 < n_remains[i] and 0 < len(freights[i]):
        u, w = heappop(freights[i])
        w = min(w, min(n_remains[-u:i + 1]))
        for j in range(-u, i):
            n_remains[j] -= w

        n_deliverd += w

print(n_deliverd)
