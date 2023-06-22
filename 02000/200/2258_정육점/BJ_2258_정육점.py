# https://www.acmicpc.net/problem/2258

import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline()

N, M = map(int, readline().split())

meats = defaultdict(list)
for w, p in (map(int, readline().split()) for _ in range(N)):
    meats[p].append(w)

for v in meats.values():
    v.sort(reverse=True)

local_sum = 0
for i, p in enumerate(prices := sorted(meats.keys())):
    optimal_price = 0

    for w in meats[p]:
        if M <= local_sum:
            break
        optimal_price += p
        local_sum += w

    if M <= local_sum:
        optimal_price = min(optimal_price, prices[i + 1]) if i + 1 < len(prices) else optimal_price
        break

print([optimal_price, -1][local_sum < M])
