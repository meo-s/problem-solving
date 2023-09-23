# https://www.acmicpc.net/problem/18870

input()
nums = [*map(int, input().split())]

unique_nums = sorted(set(nums))
ranks = dict(((n, i) for i, n in enumerate(unique_nums)))
print(*(ranks[n] for n in nums), sep=' ')
