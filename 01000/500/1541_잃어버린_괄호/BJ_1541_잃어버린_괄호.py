# https://www.acmicpc.net/problem/1541

import sys
readline = lambda: sys.stdin.readline()


ans = 0
n = '+'
inversed = False
for i, c in enumerate((s := readline())):
    if not (c in ['-', '+'] or i == len(s) - 1):
        n += c
    else:
        inversed = (n := int(n)) < 0 or inversed
        ans += min(n, -n) if inversed else n
        n = c

print(ans)
