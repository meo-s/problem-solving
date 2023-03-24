# https://www.acmicpc.net/problem/21740


from functools import cmp_to_key


def flip(s):
    return ''.join(reversed([['6', '9'][c == '6'] if c in '69' else c for c in s]))


def compare(s1, s2):
    l1, l2 = len(s1), len(s2)
    for i in range(l1 + l2):
        c1 = s1[i] if i < l1 else s2[i - l1]
        c2 = s2[i] if i < l2 else s1[i - l2]
        if c1 != c2:
            return [-1, 1][c2 < c1]
    return 0


N = int(input())
S = sorted(map(flip, input().split()), key=cmp_to_key(compare))
max_len = max(map(len, S))
for i in reversed(range(N)):
    if len(S[i]) == max_len:
        S.insert(i, S[i])
        break

print(*map(flip, S), sep='')
