# https:///www.acmicpc.net/problem/1132

import sys
import operator
import functools
from collections import defaultdict


def readline():
    return sys.stdin.readline().rstrip()


N = int(readline())
anums = [readline() for _ in range(N)]

L = max(map(len, anums))
priorities = defaultdict(lambda: 0)
for anum in anums:
    for i, a in enumerate(anum):
        priorities[a] += 10 ** (len(anum) - i)

priorities = [(a, -p) for a, p in priorities.items()]
priorities.sort(key=operator.itemgetter(1))
digits = dict([(a, 9 - i) for i, (a, _) in enumerate(priorities)])

target = 0
a_zero = ([None] + [a for a, digit in digits.items() if digit == 0])[-1]
zeroable = set(''.join(anums)) - set(anum[0] for anum in anums)
while a_zero is not None and a_zero not in zeroable:
    target += 1
    for a, n in digits.items():
        if n == target:
            digits[a_zero], digits[a] = n, 0
            a_zero = a
            break


to_num = lambda anum: functools.reduce(lambda n, a: n * 10 + digits[a], anum, 0)
print(sum(map(to_num, anums)))
