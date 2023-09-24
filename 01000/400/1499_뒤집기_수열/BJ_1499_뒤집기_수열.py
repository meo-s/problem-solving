# https://www.acmicpc.net/problem/1499


def find(a, b, dp={}):
    if a == b: return 0

    if (a, b) not in dp:
        dp[(a, b)] = float('inf')

        for i in range(0, len(a) - 1):
            if 0 < i and a[i - 1] != b[i - 1]: break

            for j in range(len(a), i + 1, -1):
                if j < len(a) and a[j] != b[j]: break

                dp[(a, b)] = min(dp[(a, b)], find(a[i:j][::-1], b[i:j], dp) + 1)

    return dp[(a, b)]


ans = find(input(), input(), {})
print([-1, ans][ans != float('inf')])
