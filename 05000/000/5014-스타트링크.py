# https://www.acmicpc.net/problem/5014

from collections import deque

F, S, G, U, D = map(int, input().split())

costs = [-1] * F
costs[S - 1] = 0

floors = deque([S - 1])
while (0 < len(floors)) and (costs[G - 1] < 0):
    now = floors.popleft()
    for floor in [now - D, now + U]:
        if (not 0 <= floor < F) or 0 <= costs[floor]:
            continue
        costs[floor] = costs[now] + 1
        floors.append(floor)

print([costs[G - 1], 'use the stairs'][costs[G - 1] < 0])
