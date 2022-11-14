# https://www.acmicpc.net/problem/1461

import itertools
from collections import defaultdict

N, M = map(int, input().split())

weights = defaultdict(list)
for w in map(int, input().split()):
    weights[w // abs(w)].append(w)
for partial in weights.values():
    partial.sort(key=abs, reverse=True)

dist = 0
for partial in weights.values():
    for i in range(0, len(partial), M):
        dist += abs(max(partial[i:i + M], key=abs)) * 2

dist -= abs(max(itertools.chain(*weights.values()), key=abs))
print(dist)
