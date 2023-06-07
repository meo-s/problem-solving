# https://www.acmicpc.net/problem/10809

indices = [-1] * (ord('z') - ord('a') + 1)
for i, c in enumerate(input()):
    if indices[ord(c) - ord('a')] == -1:
        indices[ord(c) - ord('a')] = i

print(*indices, sep=' ')
