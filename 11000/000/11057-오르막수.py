# https://www.acmicpc.net/problem/11057

N = int(input())
nums = [[1] * 10] + [[0] * 10 for _ in range(N - 1)]
for i in range(1, N):
    for j in range(10):
        nums[i][j] = sum(nums[i - 1][:j + 1]) % 10007

print(sum(nums[-1]) % 10007)
