# https://www.acmicpc.net/problem/12904

import sys

readline = lambda: sys.stdin.readline().strip()

S, T = readline(), readline()
while len(S) < len(T):
    t, T = T[-1], T[:-1]
    T = T[::-1] if t == 'B' else T

print(int(S == T))
