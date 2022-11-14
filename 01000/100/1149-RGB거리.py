# https://www.acmicpc.net/problem/1149

import sys
readline = lambda: sys.stdin.readline().strip()


N = int(readline())
costs = [[0] * 3 for _ in range(N + 1)]

for i in range(1, N + 1):
    local_costs = tuple(map(int, readline().split()))
    costs[i][0] = min(costs[i - 1][1], costs[i - 1][2]) + local_costs[0]
    costs[i][1] = min(costs[i - 1][0], costs[i - 1][2]) + local_costs[1]
    costs[i][2] = min(costs[i - 1][0], costs[i - 1][1]) + local_costs[2]

print(min(costs[-1]))
