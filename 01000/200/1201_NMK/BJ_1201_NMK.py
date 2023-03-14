# https://www.acmicpc.net/problem/1201

N, M, K = map(int, input().split())
if not K + (M - 1) <= N <= M * K:
    print(-1)
else:
    r = N - M
    n = 1
    while n <= N:
        print(*reversed(range(n, n + 1 + min(r, K - 1))), sep=' ', end=' ')
        n += 1 + min(r, K - 1)
        r -= min(r, K - 1)
    print()
