# https://www.acmicpc.net/problem/3830

import sys


class DisjointSet:
    def __init__(self, N):
        self.parents = [*range(N)]

    def find(self, u):
        if self.parents[u] != u:
            self.parents[u] = self.find(self.parents[u])
        return self.parents[u]

    def union(self, u, v):
        pu = self.find(u)
        pv = self.find(v)
        if pu != pv:
            self.parents[pv] = pu


def update(diffs, disjoint_set, u):
    if diffs[u][0] != (pu := disjoint_set.find(u)):
        update(diffs, disjoint_set, diffs[u][0])
        diffs[u] = (pu, diffs[diffs[u][0]][1] + diffs[u][1])


sys.setrecursionlimit(10**5)
readline = lambda: sys.stdin.readline().rstrip()

while True:
    N, Q = map(int, readline().split())
    if N + Q == 0:
        break

    disjoint_set = DisjointSet(N + 1)
    diffs = [(i, 0) for i in range(N + 1)]
    for _ in range(Q):
        q, *args = readline().split()
        if q == '!':
            a, b, w = map(int, args)
            if disjoint_set.find(a) != disjoint_set.find(b):
                update(diffs, disjoint_set, a)
                update(diffs, disjoint_set, b)

                disjoint_set.union(a, b)
                pa, pb = diffs[a][0], diffs[b][0]
                # b->pb->a->pa
                #   b->pb = diffs[b][1]
                #   b->a = w
                #   a->pa = diffs[a][1]
                diffs[pb] = (pa, diffs[a][1] + w - diffs[b][1])
        else:
            a, b = map(int, args)
            if disjoint_set.find(a) != disjoint_set.find(b):
                print('UNKNOWN')
            else:
                update(diffs, disjoint_set, a)
                update(diffs, disjoint_set, b)
                print(diffs[b][1] - diffs[a][1])
