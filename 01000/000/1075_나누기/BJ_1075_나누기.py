# https://www.acmicpc.net/problem/1075

import itertools

N = int(input())
F = int(input())
for n in itertools.count((N // 100) * 100):
    if n % F == 0:
        print(str(n % 100).rjust(2, '0'))
        break
