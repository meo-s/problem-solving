# https://www.acmicpc.net/problem/1422

import math
from functools import cmp_to_key


def cmp(lhs, rhs):
    lhsrhs = lhs * 10**int(math.log10(rhs) + 1) + rhs
    rhslhs = rhs * 10**int(math.log10(lhs) + 1) + lhs
    return [-1, 1][rhslhs < lhsrhs]


K, N = map(int, input().split())
nums = sorted([int(input()) for _ in range(K)], reverse=True)
nums = sorted([nums[0]] * (N - len(nums)) + nums, key=cmp_to_key(cmp))
print(*nums[::-1], sep='')
