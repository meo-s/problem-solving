# https://www.acmicpc.net/problem/11444

def fibonacci_matrix(k):
    def mat22mul(m1, m2):
        return tuple((m1[i] * m2[j] + m1[i + 1] * m2[j + 2]) % 1_000_000_007 for i in (0, 2) for j in (0, 1))

    if k <= 1:
        return (1, 1, 1, 0)

    m = mat22mul(m := fibonacci_matrix(k // 2), m)
    m = mat22mul(m, [1, 1, 1, 0]) if k % 2 else m
    return m


def fibonacci(n):
    return [0, 1, fibonacci_matrix(n)[2]][min(n, 2)]


print(fibonacci(int(input())))
