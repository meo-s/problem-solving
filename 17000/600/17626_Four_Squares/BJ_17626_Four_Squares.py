# https://www.acmicpc.net/problem/17626

import sys
import math
import itertools
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

'''
N = int(readline())
counts = [0] + [4] * N
sets = defaultdict(list)
for i in range(1, int(math.sqrt(N)) + 1):
    sets[1].append(i**2)
    counts[i**2] = 1

for set1, set2 in [(1, 1), (1, 2)]:
    for n1, n2 in itertools.product(sets[set1], sets[set2]):
        if n1 + n2 <= N and set1 + set2 < counts[n1 + n2]:
            sets[set1 + set2].append(n1 + n2)
            counts[n1 + n2] = set1 + set2

print(counts[N])
'''

N = int(readline())
counts = [0] + [float('inf')] * N
for i in range(1, N + 1):
    j = 1
    while j**2 <= i:
        counts[i] = min(counts[i], counts[i - j**2])
        j += 1
    counts[i] += 1

print(counts[N])
