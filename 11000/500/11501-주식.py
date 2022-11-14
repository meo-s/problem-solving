# https://www.acmicpc.net/problem/11501

import sys
readline = lambda: sys.stdin.readline().strip()


for _ in range(int(readline())):
    N = int(readline())
    profit = 0
    k, prices = 0, tuple(map(int, readline().split()))[::-1]
    for i in range(1, N):
        profit += max(0, prices[k] - prices[i])
        k = i if prices[k] < prices[i] else k

    print(profit)
