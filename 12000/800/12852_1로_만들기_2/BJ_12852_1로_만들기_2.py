# https://www.acmicpc.net/problem/12852

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()
N = int(readline())

logs = [None] * (N + 1)
logs[1] = (None, 0)

nums = deque([(1, 0)])
while logs[N] is None:
    n, dist = nums.popleft()
    for dn in [1, n, 2 * n]:
        if not n + dn <= N or logs[n + dn] is not None:
            continue
        logs[n + dn] = (n, dist + 1)
        nums.append((n + dn, dist + 1))

trace = [N]
while (prev := logs[trace[-1]][0]) is not None:
    trace.append(prev)

print(len(trace) - 1)
print(*trace, sep=' ')
