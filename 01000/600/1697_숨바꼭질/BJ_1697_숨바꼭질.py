# https://www.acmicpc.net/problem/1697

from collections import deque


start, goal = tuple(map(int, input().strip().split(' ')))

costs = [-1] * 100001
costs[start] = 0

points = deque([start])
while not 0 <= costs[goal]:
    x = points.popleft()
    for dx in [-1, 1, x]:
        if (x + dx) == start:
            continue
        if 0 <= x + dx < len(costs):
            if costs[x + dx] == -1:
                costs[x + dx] = costs[x] + 1
                points.append(x + dx)

print(costs[goal])
