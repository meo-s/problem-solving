# https://www.acmicpc.net/problem/19539

N = int(input())
trees = [*map(int, input().split(' '))]

remains = {2: 0, 1: 0}
for tree in trees:
    remains[2] += tree // 2
    remains[1] += tree % 2

while 0 < (n := (remains[2] - remains[1]) // 3):
    remains[2] -= 2 * n + remains[1]
    remains[1] = n

print(['NO', 'YES'][remains[2] == remains[1]])
