# https://www.acmicpc.net/problem/1700

import sys
from heapq import heapify, heappush, heappop
from collections import defaultdict
from collections import deque


def next_use(items):
    items.popleft()
    return items[0] if 0 < len(items) else float('-inf')


readline = lambda: sys.stdin.readline().strip()

N, K = map(int, readline().split())
logs = [*map(int, readline().split())]

items = defaultdict(deque)
for i, item in enumerate(logs):
    items[item].append(-i)

n_unplugs = 0
multi_tap, in_multi_tap = [], [False] * 101
for item in logs:
    if not in_multi_tap[item]:
        if len(multi_tap) == N:
            n_unplugs += 1
            _, unplug = heappop(multi_tap)
            in_multi_tap[unplug] = False

        heappush(multi_tap, (next_use(items[item]), item))
        in_multi_tap[item] = True
    else:
        for i in range(len(multi_tap)):
            if multi_tap[i][1] == item:
                multi_tap[i] = (next_use(items[item]), item)
                heapify(multi_tap)
                break

print(n_unplugs)
