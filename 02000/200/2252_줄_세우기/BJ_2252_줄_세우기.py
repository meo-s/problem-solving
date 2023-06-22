# https://www.acmicpc.net/problem/2252

import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
dependencies = [0] * (N + 1)
dependencies[0] = sys.maxsize
comparisons = defaultdict(list)
for _ in range(M):
    u, v = map(int, readline().split())
    comparisons[u].append(v)
    dependencies[v] += 1

people = []
for person in range(1, N + 1):
    people.append(person) if dependencies[person] == 0 else None

pivot = -1
while pivot < len(people) - 1:
    person = people[(pivot := pivot + 1)]
    for other in comparisons[person]:
        dependencies[other] -= 1
        if dependencies[other] == 0:
            people.append(other)

print(*people, sep=' ')
