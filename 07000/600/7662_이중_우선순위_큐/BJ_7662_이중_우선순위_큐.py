# https://www.acmicpc.net/problem/7662

import sys
from collections import defaultdict
from heapq import heappush, heappop

readline = lambda: sys.stdin.readline().strip()


class DPQ:
    def __init__(self):
        self._max_heap = []
        self._min_heap = []
        self._elements = defaultdict(int)
        self._n_elements = 0

    def push(self, v):
        self._elements[v] += 1
        self._n_elements += 1
        heappush(self._max_heap, -v)
        heappush(self._min_heap, v)

    def pop_max(self):
        while not 0 < self._elements[(v := -heappop(self._max_heap))]:
            pass
        self._n_elements -= 1
        self._elements[v] -= 1
        return v

    def pop_min(self):
        while not 0 < self._elements[(v := heappop(self._min_heap))]:
            pass
        self._n_elements -= 1
        self._elements[v] -= 1
        return v

    def __len__(self):
        return self._n_elements


for _ in range(int(readline())):
    q = DPQ()
    for _ in range(int(readline())):
        cmd, arg = readline().split()
        arg = int(arg)

        if cmd == 'I':
            q.push(arg)
        elif 0 < len(q):
            [q.pop_min, q.pop_max][0 < arg]()

    if 0 < len(q):
        minv = maxv = q.pop_max()
        minv = q.pop_min() if 0 < len(q) else minv
        print(maxv, minv, sep=' ')
    else:
        print('EMPTY')