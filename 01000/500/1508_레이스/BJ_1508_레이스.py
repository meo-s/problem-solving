# https://www.acmicpc.net/problem/1508

from collections import deque


def simulate(d, trace=None):
    r = M
    pi = 0
    for i in range(1, K + 1):
        if d <= P[i] - P[pi]:
            trace.append(i) if trace is not None else None
            pi = i
            if (r := r - 1) == 0:
                break
    return r == 0


N, M, K = map(int, input().split())
P = [float('-inf'), *map(int, input().split())]

max_d = 0
lb, ub = 1, N + 1
while lb < ub:
    mid = (lb + ub) // 2
    if simulate(mid):
        max_d = max(max_d, mid)
        lb = mid + 1
    else:
        ub = mid

simulate(max_d, trace := deque())
for i in range(1, K + 1):
    print([0, 1][0 < len(trace) and trace[0] == i], end='')
    trace.popleft() if 0 < len(trace) and trace[0] == i else None
