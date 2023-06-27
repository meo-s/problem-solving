import sys

INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()

N, M = map(int, readline().split())
usages = [*map(int, readline().split())]
costs = [*map(int, readline().split())]

dp = [-INF] * (100**2 + 1)
dp[sum(costs)] = sum(usages)
for usage, cost in zip(usages, costs):
    for i in range(len(dp)):
        if dp[i] != -INF:
            dp[i - cost] = max(dp[i - cost], dp[i] - usage)

optimal_cost = 0
for cost, free in enumerate(dp):
    if M <= free:
        optimal_cost = cost
        break

print(optimal_cost)
