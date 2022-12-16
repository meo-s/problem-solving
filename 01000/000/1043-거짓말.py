# https://www.acmicpc.net/problem/1043

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
knowns = deque([*map(int, readline().split())][1:])
parties = [set([*map(int, readline().split())][1:]) for _ in range(M)]

schedules = [[] for _ in range(N + 1)]
for party, people in enumerate(parties):
    for person in people:
        schedules[person].append(party)

can_lie = [1] * M
visited = [False] * (N + 1)
while 0 < len(knowns):
    known = knowns.popleft()
    for party in schedules[known]:
        can_lie[party] = 0
        for person in parties[party]:
            if not visited[person]:
                visited[person] = True
                knowns.append(person)

print(sum(can_lie))
