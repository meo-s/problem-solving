# https://www.acmicpc.net/problem/9019

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()
D = lambda callstack, n: (callstack + 'D', n * 2 % 10000)
S = lambda callstack, n: (callstack + 'S', [9999, n - 1][0 < n])
L = lambda callstack, n: (callstack + 'L', (n * 10 % 10000) + n // 1000)
R = lambda callstack, n: (callstack + 'R', (n % 10) * 1000 + n // 10)

for _ in range(int(readline())):
    initial, G = map(int, readline().split())

    logs = [None] * 10001
    logs[initial] = ('', initial)
    regs = deque([('', initial)])
    while logs[G] is None:
        reg = regs.popleft()
        for fn in [D, S, L, R]:
            callstack, n = fn(*reg)
            if logs[n] is None:
                logs[n] = (callstack, n)
                regs.append((callstack, n))

    print(logs[G][0])
