# https://www.acmicpc.net/problem/1764

import sys
from collections import Counter

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
print(len(a := sorted([k for k, v in Counter([readline() for _ in range(N + M)]).items() if v == 2])), *a, sep='\n')
