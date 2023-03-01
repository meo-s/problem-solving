# https://www.acmicpc.net/problem/3176

import sys
import math
from itertools import repeat


class DfsTree:
    def __init__(self, n_vertices, edges, root=1):
        self._n_vertices = n_vertices
        self._root = root
        self._parents = [(i, 0) for i in range(n_vertices + 1)]
        self._ranges = [None] * (n_vertices + 1)
        self._depth = 0

        self._compose_tree(edges, root)

        N = int(math.log2(self._depth) + 1)
        self._min_costs = [[sys.maxsize] * N for _ in range(n_vertices + 1)]
        self._max_costs = [[0] * N for _ in range(n_vertices + 1)]
        self._ancestors = [[0] * N for _ in range(n_vertices + 1)]
        self._cache_ancestors(edges)

    def _compose_tree(self, edges, u, beg=1, depth=1):
        self._depth = max(self._depth, depth)

        end = beg + 1
        for v, w in edges[u]:
            if v != self._parents[u][0]:
                self._parents[v] = (u, w)
                end = self._compose_tree(edges, v, end, depth + 1)

        self._ranges[u] = (beg, end)
        return end + 1

    def _cache_ancestors(self, edges, u=None):
        if (u := u or self._root) == self._root:
            self._ancestors[u][:] = repeat(u, len(self._ancestors[u]))
        else:
            v, w = self._parents[u]
            self._ancestors[u][0] = v
            self._min_costs[u][0] = w
            self._max_costs[u][0] = w
            for i in range(1, len(self._ancestors[u])):
                ancestor = self._ancestors[u][i - 1]
                self._ancestors[u][i] = self._ancestors[ancestor][i - 1]
                self._min_costs[u][i] = min(self._min_costs[u][i - 1], self._min_costs[ancestor][i - 1])
                self._max_costs[u][i] = max(self._max_costs[u][i - 1], self._max_costs[ancestor][i - 1])

        for v, _ in edges[u]:
            if v != self._parents[u][0]:
                self._cache_ancestors(edges, v)

    def is_ancestor_of(self, parent, child):
        beg_p, end_p = self._ranges[parent]
        beg_c, end_c = self._ranges[child]
        return beg_p <= beg_c and end_c <= end_p

    def query_minmax(self, a, b):
        min_cost, max_cost = sys.maxsize, 0
        for u, v in [(a, b), (b, a)]:
            if not self.is_ancestor_of(u, v):
                for i in reversed(range(len(self._ancestors[u]))):
                    if not self.is_ancestor_of(self._ancestors[u][i], v):
                        min_cost = min(min_cost, self._min_costs[u][i])
                        max_cost = max(max_cost, self._max_costs[u][i])
                        u = self._ancestors[u][i]

                if u != v:
                    min_cost = min(min_cost, self._min_costs[u][0])
                    max_cost = max(max_cost, self._max_costs[u][0])

        return min_cost, max_cost


sys.setrecursionlimit(10**5)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

V = int(readline())
edges = [[] for _ in range(V + 1)]
for _ in range(V - 1):
    u, v, w = map(int, readline().split())
    edges[u].append((v, w))
    edges[v].append((u, w))

tree = DfsTree(V, edges, 1)
for _ in range(int(readline())):
    u, v = map(int, readline().split())
    sys.stdout.write(' '.join(map(str, tree.query_minmax(u, v))) + '\n')

sys.stdout.flush()
