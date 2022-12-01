# https://www.acmicpc.net/problem/15650


def search(N, M, nums=[1], depth=0):
    if depth < M:
        for i in range(nums[-1], N + 1):
            nums.append(i)
            search(N, M, nums, depth + 1)
            nums.pop()
    else:
        print(*nums[1:], sep=' ')


search(*map(int, input().split()))
