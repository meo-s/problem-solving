# https://www.acmicpc.net/problem/1439

s = input()
c = {s[0]: 0}
for i in range(1, len(s)):
    if s[i] != s[i - 1]:
        c[s[i]] = c.setdefault(s[i], 0) + 1

print(max(c.values()))
