# https://www.acmicpc.net/problem/1744

import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

zero_ones = [0, 0]
pos, neg = [], []
for _ in range(int(readline())):
    n = int(readline())
    if n in [0, 1]:
        zero_ones[n] += 1
    else:
        [neg, pos][0 < n].append(n)

pos.sort(reverse=True)
neg.sort()
neg = neg[:-1] if len(neg) % 2 != 0 and 0 < zero_ones[0] else neg

pair_sum = 0
for nums in [pos, neg]:
    for i in range(0, len(nums), 2):
        n = nums[i] * (nums[i + 1] if i + 1 < len(nums) else 1)
        pair_sum += n

print(pair_sum + zero_ones[1])
