# https://www.acmicpc.net/problem/1464

s = input()
for i in range(1, len(s)):
    if s[i] < s[i - 1] and s[i] <= s[0]:
        s = (s[:i][::-1] + s[i])[::-1] + (s[i + 1:] if i + 1 < len(s) else '')

print(s)
