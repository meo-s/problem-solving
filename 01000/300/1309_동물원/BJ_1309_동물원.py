# https://www.acmicpc.net/problem/1309

from itertools import product

N = int(input())
dp = [1, 1, 1]
for _ in range(N - 1):
    now = [0, 0, 0]
    for i, j in product(range(3), repeat=2):
        if j == 0 or i != j:
            now[i] += dp[j]
            now[i] %= 9901
    dp = now

print(sum(dp) % 9901)
