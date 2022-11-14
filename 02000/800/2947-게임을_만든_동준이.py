# https://www.acmicpc.net/problem/2847

import sys
readline = lambda: sys.stdin.readline().strip()


scores = [int(readline()) for _ in range(int(readline()))][::-1]

n_fixes = 0
for i in range(1, len(scores)):
    n_fixes += max(scores[i] - scores[i - 1] + 1, 0)
    scores[i] = min(scores[i - 1] - 1, scores[i])

print(n_fixes)
