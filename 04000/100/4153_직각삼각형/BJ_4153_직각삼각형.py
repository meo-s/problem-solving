# https://www.acmicpc.net/problem/4153

while sum(lines := sorted(map(int, input().split()))) != 0:
    print(['right', 'wrong'][lines[0]**2 + lines[1]**2 != lines[2]**2])
