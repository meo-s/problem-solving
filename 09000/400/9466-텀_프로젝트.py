import sys

readline = lambda: sys.stdin.readline().rstrip()

for _ in range(int(readline())):
    N = int(readline())

    follows, dependencies = {}, [0] * (N + 1)
    for u, v in enumerate(map(int, readline().split()), start=1):
        follows[u] = v
        dependencies[v] += 1

    dropouts = []
    for i in range(1, N + 1):
        dropouts.append(i) if dependencies[i] == 0 else None

    i = -1
    while i < len(dropouts) - 1:
        wish = follows[dropouts[(i := i + 1)]]
        dependencies[wish] -= 1
        dropouts.append(wish) if dependencies[wish] == 0 else None

    print(len(dropouts))
