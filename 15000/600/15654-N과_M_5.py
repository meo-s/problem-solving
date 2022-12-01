# https://www.acmicpc.net/problem/15654


def search(nums, max_len, comb=[], used=0):
    if len(comb) < max_len:
        for i, n in enumerate(nums):
            if (used & (1 << i)) == 0:
                comb.append(n)
                search(nums, max_len, comb, used | (1 << i))
                comb.pop()
    else:
        print(*comb, sep=' ')


_, M = map(int, input().split())
nums = sorted(map(int, input().split()))
search(nums, M)
