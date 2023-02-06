# https://www.acmicpc.net/problem/1253

from bisect import bisect_left
from collections import Counter, defaultdict

N = int(input())
nums = sorted(map(int, input().split()))
count = Counter(nums)
goods = defaultdict(int)
for i in range(N):
    for j in range(N):
        if i != j:
            pick = nums[bisect_left(nums, nums[i] + nums[j], 0, N - 1)]
            if nums[i] + nums[j] == pick:
                if 1 + int(nums[i] == pick) + int(nums[j] == pick) <= count[pick]:
                    goods[pick] = min(goods[pick] + 1, count[pick])

print(sum(goods.values()))
