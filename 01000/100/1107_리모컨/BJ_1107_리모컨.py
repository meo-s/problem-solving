# https://www.acmicpc.net/problem/1107

import sys

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
lenN = len(str(N))

keys = set(str(n) for n in range(10))
if 0 < int(readline()):
    for key in readline().split():
        keys.remove(key)


def search(n='', min_clicks=abs(int(N) - 100)):
    if 0 < len(n):
        min_clicks = min(abs(int(n) - N) + len(str(int(n))), min_clicks)
    if len(n) == lenN + 1:
        return min_clicks
    return min([*map(search, (n + key for key in keys)), min_clicks])


print(search())
