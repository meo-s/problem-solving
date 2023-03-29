# https://www.acmicpc.net/problem/17070

import sys


def dfs(y, x, angle=0):
    if y == N - 1 and x == N - 1:
        return 1

    n_cases = 0

    if angle <= 45:
        if dp[y][x][0] < 0:
            dp[y][x][0] = dfs(y, x + 1, 0) if x + 1 < N and home[y][x + 1] == 0 else 0
        n_cases += dp[y][x][0]

    if 45 <= angle <= 90:
        if dp[y][x][1] < 0:
            dp[y][x][1] = dfs(y + 1, x, 90) if y + 1 < N and home[y + 1][x] == 0 else 0
        n_cases += dp[y][x][1]

    if dp[y][x][2] < 0:
        if not (y + 1 < N and x + 1 < N):
            dp[y][x][2] = 0
        elif any(home[y + dy][x + dx] == 1 for dy, dx in [(1, 0), (0, 1), (1, 1)]):
            dp[y][x][2] = 0
        else:
            dp[y][x][2] = dfs(y + 1, x + 1, 45)
    n_cases += dp[y][x][2]

    return n_cases


readline = lambda: sys.stdin.readline().strip()

N = int(readline())
home = [[*map(int, readline().split())] for _ in range(N)]

dp = [[[-1] * 3 for _ in range(N)] for _ in range(N)]
print(dfs(0, 1))
