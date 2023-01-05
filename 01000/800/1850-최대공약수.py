# https://www.acmicpc.net/problem/1850

def gcd(a, b):
    while 0 < (r := a % b):
        a, b = b, r
    return b


A, B = map(int, input().split())

n_ones = gcd(min(A, B), abs(A - B)) if A != B else A
print('1' * n_ones)
