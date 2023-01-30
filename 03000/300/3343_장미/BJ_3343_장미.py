# https://www.acmicpc.net/problem/3343

def GCD(a, b):
    while 0 < a % b:
        a, b = b, a % b
    return b


def LCM(a, b):
    return a * b // GCD(a, b)


def bound(N, divisor):
    return N // divisor + int(0 < N % divisor)


def find_min_cost(N):
    min_cost = float('inf')
    for i in range(0, bound(N, A) + 1):
        min_cost = min(min_cost, B * i + D * bound(max(0, N - A * i), C))
    return [0, min_cost][min_cost < float('inf')]


N, A, B, C, D = map(int, input().split())
lcm = LCM(A, C)
lcm_cost = min(lcm // A * B, lcm // C * D)

greedy_cost = lcm_cost * (max(0, N - lcm) // lcm)
print(greedy_cost + find_min_cost(N - lcm * (max(0, N - lcm) // lcm)))
