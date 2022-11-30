# https://www.acmicpc.net/problem/16953

from collections import deque

x, goal = map(int, input().split())

''' bfs
goal_dist = None
points = deque([(x, 1)])
while 0 < len(points):
    x, dist = points.popleft()
    if x == goal:
        goal_dist = dist
        break

    for x_next in [x * 2, x * 10 + 1]:
        if 1 <= x_next <= goal:
            points.append((x_next, dist + 1))

print([-1, goal_dist][goal_dist is not None])
'''

''' greedy
'''
dist = 1
while x < goal:
    dist += 1
    if goal % 10 == 1:
        goal //= 10
    elif goal & 1 == 0:
        goal //= 2
    else:
        break

print([-1, dist][x == goal])
