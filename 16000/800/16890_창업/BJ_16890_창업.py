import math
from collections import deque

s1, s2 = sorted(input()), sorted(input(), reverse=True)
N = len(s1)
s1 = deque(s1[:math.ceil(N / 2)])
s2 = deque(s2[:N // 2])

s, l, r = [None] * N, 0, N - 1
while l <= r:
    if len(s2) == 0 or s1[0] < s2[0]:
        s[l] = s1.popleft()
        l += 1
    else:
        s[r] = s1.pop()
        r -= 1
    if len(s2) == 0:
        break
    if len(s1) == 0 or s1[0] < s2[0]:
        s[l] = s2.popleft()
        l += 1
    else:
        s[r] = s2.pop()
        r -= 1

print(*s, sep='')
