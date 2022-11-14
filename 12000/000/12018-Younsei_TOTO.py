# https://www.acmicpc.net/problem/12018

import itertools
import sys
readline = lambda: sys.stdin.readline().strip()


N, m = map(int, readline().split())

lecture_costs = []
for _ in range(N):
    _, cap = map(int, readline().split())
    appliers = sorted(itertools.chain([1], map(int, readline().split())), reverse=True)
    lecture_costs.append(appliers[min(cap - 1, len(appliers) - 1)])

lecture_costs.sort()

i = 0
while i < len(lecture_costs) and lecture_costs[i] <= m:
    m -= lecture_costs[i]
    i += 1

print(i)
