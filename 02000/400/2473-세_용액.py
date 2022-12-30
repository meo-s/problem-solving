# https://www.acmicpc.net/problem/2473

import sys
import operator

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
liquids = [*map(int, readline().split())]
liquids.sort()

best_pick = (sys.maxsize, None)
for k in range(N - 2):
    i, j = k + 1, len(liquids) - 1
    while i < j:
        L_jk = liquids[j] + liquids[k]
        while i + 1 < j and abs(liquids[i + 1] + L_jk) < abs(liquids[i] + L_jk):
            i += 1

        best_pick = min(best_pick, (liquids[i] + L_jk, (k, i, j)), key=lambda v: abs(v[0]))
        j -= 1

print(*operator.itemgetter(*best_pick[1])(liquids), sep=' ')
