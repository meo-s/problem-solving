# https://www.acmicpc.net/problem/1620

import re
import sys

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
ntoname, nameton = {}, {}
for i in range(1, N + 1):
    name = readline()
    ntoname[str(i)] = name
    nameton[name] = i

for _ in range(M):
    q = readline()
    print([ntoname, nameton][re.match(r'^[0-9]', q) is None][q])
