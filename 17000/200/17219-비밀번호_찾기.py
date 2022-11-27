# https://www.acmicpc.net/problem/17219

import sys

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
passwords = dict(readline().split() for _ in range(N))
print(*[passwords[readline()] for _ in range(M)], sep='\n')
