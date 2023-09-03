# https://www.acmicpc.net/problem/11729

history = []


def hanoi(s, t, n):
    hanoi(s, s ^ t, n - 1) if 1 < n else None
    history.append((s, t))
    hanoi(s ^ t, t, n - 1) if 1 < n else None


hanoi(1, 3, int(input()))

print(len(history))
for s, t in history:
    print(s, t, sep=' ')
