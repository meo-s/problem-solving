# https://www.acmicpc.net/problem/2096

import sys

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
scores = [tuple(map(int, readline().split())) for _ in range(N)]

dp = [(0, 0), (0, 0), (0, 0)]
for i in range(N):
    minmax_scores = []
    for j in range(3):
        min_score, max_score = float('inf'), float('-inf')
        for dj in [-1, 0, 1]:
            if 0 <= j + dj < 3:
                min_score = min(dp[j + dj][0], min_score)
                max_score = max(dp[j + dj][1], max_score)

        minmax_scores.append((min_score + scores[i][j], max_score + scores[i][j]))

    dp = minmax_scores

for f, idx in [(max, 1), (min, 0)]:
    print(f(dp, key=lambda v: v[idx])[idx], end=' ')
