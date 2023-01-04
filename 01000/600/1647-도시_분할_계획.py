# https://www.acmicpc.net/problem/1647

import sys
import operator


class UnionFindSet:
    def __init__(self, N):
        self._parents = [i for i in range(N + 1)]

    def find(self, u):
        if self._parents[u] != u:
            self._parents[u] = self.find(self._parents[u])
        return self._parents[u]

    def union(self, u, v):
        up, vp = self.find(u), self.find(v)
        if up != vp:
            self._parents[vp] = up


readline = lambda: sys.stdin.readline().strip()

V, E = map(int, readline().split())
edges = [tuple(map(int, readline().split())) for _ in range(E)]
edges.sort(key=operator.itemgetter(-1), reverse=True)

cost, n_roads = 0, 0
union_find = UnionFindSet(V)
while n_roads < V - 2:
    u, v, w = edges.pop()
    up, vp = union_find.find(u), union_find.find(v)
    if up != vp:
        cost += w
        n_roads += 1
        union_find.union(u, v)

print(cost)
