# https://www.acmicpc.net/problem/9251

s1, s2 = input(), input()

dp = [0] * (len(s1) + 1)
for c2 in s2:
    lcs, max_lcs = [0] * (len(s1) + 1), 0
    for i, c1 in enumerate(s1, start=1):
        max_lcs = max(max_lcs, dp[i - 1])
        lcs[i] = max_lcs + 1 if c1 == c2 else dp[i]

    dp = lcs

print(max(dp))
