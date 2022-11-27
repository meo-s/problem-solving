# https://www.acmicpc.net/problem/9375

import functools
import operator
import sys
from collections import Counter

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    clothes = Counter([readline().split()[-1] for _ in range(int(readline()))])
    print(functools.reduce(operator.mul, (v + 1 for v in clothes.values()), 1) - 1)
