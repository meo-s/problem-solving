# https://www.acmicpc.net/problem/10871

_, X = map(int, input().split())
print(*[n for n in map(int, input().split()) if n < X], sep=' ')
