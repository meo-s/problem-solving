# https://www.acmicpc.net/problem/2110

import sys


def try_setting_up(homes, C, min_dist):
    j = 0
    for i in range(1, len(homes)):
        if len(homes) - i < C:
            break
        if min_dist <= homes[i] - homes[j]:
            j = i
            if (C := C - 1) == 0:
                break

    return C == 0


readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, C = map(int, readline().split())
homes = sorted([-1_000_000_000] + [int(readline()) for _ in range(N)])

lb = 0
ub = homes[-1] - homes[1] + 1
max_dist = 0
while lb < ub:
    mid = (lb + ub) // 2
    if try_setting_up(homes, C, mid):
        max_dist = max(max_dist, mid)
        lb = mid + 1
    else:
        ub = mid

print(max_dist)
