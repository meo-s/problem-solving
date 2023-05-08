# https://www.acmicpc.net/problem/2098

from itertools import repeat

INF = float('inf')

N = int(input())
L = 2**N
W = [[*map(int, input().split())] for _ in range(N)]

dp = [[[(INF, None)] * L for _ in range(N)] for _ in range(2)]
for i in range(N):
    dp[1][i][0] = (0, i)

for u in range(N):
    dp[0]

    for i in range(L):
        cost, first = dp[u][i]
        if cost == INF:
            continue
        for v in range(N):
            if W[u][v] == 0:
                continue

            bitmask = i | (1 << v)
            if bitmask == i:
                continue
            if v == first and bitmask < L - 1:
                continue
            if cost + W[u][v] < dp[v][bitmask][0]:
                dp[v][bitmask] = (cost + W[u][v], first)

print(min(dp[i][-1][0] for i in range(N)))
