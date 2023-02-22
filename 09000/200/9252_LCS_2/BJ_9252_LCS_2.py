# https://www.acmicpc.net/problem/9252

import operator

s1, s2 = input(), input()

dp = [(0, -1, None)] * len(s1)
for c in s2:
    now = [None] * len(s1)
    max_dp = (0, -1, None)
    for i in range(len(s1)):
        max_dp = max(max_dp, dp[i - 1], key=operator.itemgetter(0)) if 0 < i else max_dp
        now[i] = (max_dp[0] + 1, i, max_dp) if s1[i] == c else dp[i]

    dp = now

print((lcs := max(dp, key=operator.itemgetter(0)))[0])
if 0 < lcs[0]:
    trace = []
    while 0 <= lcs[1]:
        trace.append(s1[lcs[1]])
        lcs = lcs[-1]

    print(*trace[::-1], sep='')
