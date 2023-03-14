# https://www.acmicpc.net/problem/1102

import sys
from itertools import repeat

readline = lambda: sys.stdin.readline().rstrip()  # noqa

INF = float('inf')
N = int(readline())
C = [[*map(int, readline().split())] for _ in range(N)]
S = readline()
P = int(readline())

on, state = 0, 0
for bitmask, c in enumerate(S):
    on += [0, 1][c == 'Y']
    state |= [0, 1][c == 'Y'] << bitmask

if on == 0 or P <= on:
    print([-1, 0][P <= on])
else:
    M = [[INF] * (1 << N) for _ in range(N)]  # 발전소 상태별 재가동 최소 비용
    for u in range(N):
        for v in range(N):
            for bitmask in range(1 << N):
                if bitmask & (1 << u) != 0:
                    M[v][bitmask] = min(M[v][bitmask], C[u][v])

    dp = [[INF] * (1 << N) for _ in range(2)]
    dp[0][state] = 0

    for k in range(on, P):
        dp[1][:] = repeat(INF, 1 << N)
        for i in range(N):
            for bitmask in range((1 << k) - 1, 1 << N):
                if bitmask & (1 << i) != 0:
                    continue
                dp[1][bitmask | (1 << i)] = min(dp[1][bitmask | (1 << i)], dp[0][bitmask] + M[i][bitmask])

        dp[::] = dp[::-1]

    print(min(dp[0][:]))
