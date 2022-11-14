# https://www.acmicpc.net/problem/18870

input()
nums = [*map(int, input().split())]

unique_nums = set(nums)
ranks = dict(zip(sorted(unique_nums), range(len(unique_nums))))

ans = [None] * len(nums)
for i, n in enumerate(nums):
    ans[i] = ranks[n]

print(' '.join(map(str, ans)))
