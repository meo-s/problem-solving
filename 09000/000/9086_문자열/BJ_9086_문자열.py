# https://www.acmicpc.net/problem/9086

import sys

readline = lambda: sys.stdin.readline().rstrip()  # noqa
for _ in range(int(readline())):
    s = readline()
    print(s[0], s[-1], sep='')
