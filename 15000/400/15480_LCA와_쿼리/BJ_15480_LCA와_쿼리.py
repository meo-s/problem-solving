# https://www.acmicpc.net/problem/15480

import math
import sys


class DfsTree:

    __slots__ = ['root', 'edges', 'beg', 'end', 'jumps']

    def __init__(self, edges, root=1):
        self.root = root
        self.edges = edges

        L = math.ceil(math.log2(len(edges) - 1)) + 1
        self.jumps = [[-1] * L for _ in range(len(edges))]

        self.beg = [-1] * len(edges)
        self.end = [-1] * len(edges)
        self._dfs(root)

        self.jumps[root][0] = root
        for i in range(1, L):
            for u in range(1, len(edges)):
                self.jumps[u][i] = self.jumps[self.jumps[u][i - 1]][i - 1]

    def _dfs(self, u, p=-1, beg=0):
        self.beg[u] = (end := beg)
        for v in self.edges[u]:
            if v != p:
                self.jumps[v][0] = u
                end = self._dfs(v, u, end + 1) 

        self.end[u] = end
        return self.end[u]

    def is_ancestor_of(self, u, v):
        return self.beg[u] <= self.beg[v] and self.end[v] <= self.end[u]

    def lca_of(self, u, v):
        if self.is_ancestor_of(u, v):
            u, v = v, u

        L = len(self.jumps[0])
        for i in reversed(range(L)):
            if not self.is_ancestor_of(self.jumps[u][i], v):
                u = self.jumps[u][i]

        if not self.is_ancestor_of(u, v):
            u = self.jumps[u][0]

        return u

    def junction_of(self, u, v, r):
        L = len(self.jumps[0])
        isa = self.is_ancestor_of
        for i in reversed(range(L)):
            nr = self.jumps[r][i]
            if not (isa(nr, u) or isa(nr, v)):
                r = nr

        if not (isa(r, u) or isa(r, v)):
            r = self.jumps[r][0]

        return r

sys.setrecursionlimit(10**5 + 1)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
edges = [[] for _ in range(N + 1)]
for _ in range(N - 1):
    u, v = map(int, readline().split())
    edges[u].append(v)
    edges[v].append(u)

dfs_tree = DfsTree(edges)
isa = dfs_tree.is_ancestor_of
for _ in range(int(readline())):
    r, u, v = map(int, readline().split())
    lca = dfs_tree.lca_of(u, v)
    junction = dfs_tree.junction_of(u, v, r)
    print([lca, junction][isa(lca, junction)])
