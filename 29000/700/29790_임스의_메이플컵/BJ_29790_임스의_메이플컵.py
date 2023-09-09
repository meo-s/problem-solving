# https://www.acmicpc.net/problem/29790

import sys


readline = lambda: sys.stdin.readline().rstrip()

N, U, L = map(int, readline().split())
if 1000 <= N:
    if 8000 <= U or 260 <= L:
        print('Very Good')
    else:
        print('Good')
else:
    print('Bad')
