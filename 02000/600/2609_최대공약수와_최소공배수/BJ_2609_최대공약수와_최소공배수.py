# https://www.acmicpc.net/problem/2609

def gcd(a, b):
    a, b = max(a, b), min(a, b)
    while (r := a % b) != 0:
        a = b
        b = r
    return b


a, b = map(int, input().split())
print(gcd(a, b), a // gcd(a, b) * b, sep='\n')
