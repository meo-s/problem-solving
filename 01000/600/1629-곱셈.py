# https://www.acmicpc.net/problem/1629

import math


A, B, C = map(int, input().split())
A %= C

ans = 1
while 0 < B:
    n = A
    for _ in range((log2B := int(math.log2(B)))):
        n = (n * n) % C

    ans = (ans * n) % C
    B -= 2 ** log2B

print(ans)
