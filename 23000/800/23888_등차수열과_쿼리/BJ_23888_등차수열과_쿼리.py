import sys


def readline():
    return sys.stdin.readline().rstrip()


def gcd(a, b):
    a, b = max(a, b), min(a, b)
    while 0 < a % b:
        a, b = b, a % b
    return b


def nth(a, d, n):
    return a + d * (n - 1)


def sum_of(a, d, n):
    return a * n + d * (((n - 1) * n) // 2)


def query1(a, d, l, r):
    return sum_of(nth(a, d, l), d, r - l + 1)


def query2(a, d, l, r):
    if d == 0:
        return a
    if l == r:
        return nth(a, d, l)
    return gcd(a, d)


A, D = map(int, readline().split())
Q = int(readline())
queries = [None] * Q
for i in range(Q):
    qtype, l, r = map(int, readline().split())
    queries[i] = str([query1, query2][qtype - 1](A, D, l, r))

sys.stdout.write('\n'.join(map(str, queries)))
sys.stdout.flush()
