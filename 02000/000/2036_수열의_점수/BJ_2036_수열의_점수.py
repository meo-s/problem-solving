# https://www.acmicpc.net/problem/2036

import sys

readline = lambda: sys.stdin.readline().strip()

ans = 0
lt0, gt1 = [], []
for _ in range(int(readline())):
    n = int(readline())
    if n == 1:
        ans += 1
        continue

    (gt1 if 0 < n else lt0).append(n)

lt0.sort()
gt1.sort(reverse=True)

for nums in [lt0, gt1]:
    ans += sum(nums[2 * i] * nums[2 * i + 1] for i in range(len(nums) // 2))
    ans += nums[-1] if 0 < len(nums) % 2 else 0

print(ans)
