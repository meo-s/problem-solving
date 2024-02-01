# https://www.acmicpc.net/problem/2475

print(sum(map(lambda v: int(v) ** 2, input().split())) % 10)
