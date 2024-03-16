# https://www.acmicpc.net/problem/2884

hh, mm = map(int, input().split())
hh -= 1 if mm < 45 else 0
print((hh + 24) % 24, (mm + 15) % 60, sep=' ')
