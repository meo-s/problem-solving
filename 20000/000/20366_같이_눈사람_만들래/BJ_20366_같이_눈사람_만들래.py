N = int(input())
snows = sorted([*map(int, input().split())])

min_diff = float('inf')
for lb in range(N - 3):
    for ub in range(lb + 3, N):
        h = snows[lb] + snows[ub]

        i, j = lb + 1, ub - 1
        while i < j:
            while (ni := i + 1) < j:
                if abs(h - (snows[i] + snows[j])) < abs(h - (snows[ni] + snows[j])):
                    break
                i = ni

            min_diff = min(min_diff, abs(h - (snows[i] + snows[j])))
            j -= 1

print(min_diff)
