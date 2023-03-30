# https://www.acmicpc.net/problem/12865

import sys

readline = lambda: sys.stdin.readline().strip()

N, K = map(int, readline().split())
items = [tuple(map(int, readline().split())) for _ in range(N)]

dp = [[0] * (K + 1)]
visited = set([0])
for i, (w, v) in enumerate(items, start=1):
    dp.append([*dp[-1]])
    for w0 in [*visited]:
        if w0 + w <= K:
            visited.add(w0 + w)
            dp[i][w0 + w] = max(dp[i - 1][w0 + w], dp[i - 1][w0] + v)

print(max(dp[-1]))

# N, K = map(int, readline().split())
# items = [tuple(map(int, readline().split())) for _ in range(N)]

# dp = [[0] * (K + 1)]
# for w, v in items:
#     dp.append([0] * (K + 1))
#     for w0 in range(K + 1):
#         dp[-1][w0] = dp[-2][w0]
#         dp[-1][w0] = max(dp[-1][w0], dp[-2][w0 - w] + v) if 0 <= w0 - w else dp[-1][w0]

# print(max(dp[-1]))
