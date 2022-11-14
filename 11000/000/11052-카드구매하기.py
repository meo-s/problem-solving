# https://www.acmicpc.net/problem/11052

import math


def nth_cost(costs, upper_bound, n):
    return costs[n] + costs[upper_bound - n - 1] 

goal = int(input())
costs = list(map(int, input().split()))
for i in range(1, goal):
    costs[i] = max(max(nth_cost(costs, i, n) for n in range(math.ceil(i / 2))), costs[i])

print(costs[-1])
