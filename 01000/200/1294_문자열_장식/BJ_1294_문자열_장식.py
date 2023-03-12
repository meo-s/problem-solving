# https://www.acmicpc.net/problem/1294


def cmp(si, sj):
    for k in range(len(si) + len(sj)):
        c1 = si[k] if k < len(si) else sj[k - len(si)]
        c2 = sj[k] if k < len(sj) else si[k - len(sj)]
        if c1 != c2:
            return [1, -1][c1 < c2]
    return 0


s = [input() for _ in range(int(input()))]

ans = []
while 0 < len(s):
    k, c = None, None
    for i, si in enumerate(s):
        if c is None or si[0] < c or (si[0] == c and cmp(si, s[k]) < 0):
            k, c = i, si[0]

    ans.append(c)
    s[k] = s[k][1:]
    s.pop(k) if len(s[k]) == 0 else None

print(*ans, sep='')
