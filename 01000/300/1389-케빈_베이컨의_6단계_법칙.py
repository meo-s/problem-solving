# https://www.acmicpc.net/problem/1389

import collections


N, M = map(int, input().split())

edges = collections.defaultdict(set)
for _ in range(M):
    i, j = map(int, input().split())
    edges[i - 1].add(j - 1)
    edges[j - 1].add(i - 1)


def search(target):
    counts = [-1] * N
    counts[target] = 0
    waypoints = collections.deque([target])
    while 0 < len(waypoints):
        node = waypoints.popleft()
        for to in edges[node]:
            if 0 <= counts[to]:
                continue
            counts[to] = counts[node] + 1
            waypoints.append(to)

    return counts


print(sorted([(i + 1, sum(search(i))) for i in range(N)], key=lambda v: v[1])[0][0])
