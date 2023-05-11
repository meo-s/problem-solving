# https://www.acmicpc.net/problem/2467

N = int(input())
liquids = [*map(int, input().split())]

i, j = 0, len(liquids) - 1
min_dist = abs(liquids[i] + liquids[j])
where = (i, j)
while i < j:
    while i < j - 1 and abs(liquids[i + 1] + liquids[j]) < abs(liquids[i] + liquids[j]):
        i += 1
    if abs(liquids[i] + liquids[j]) < min_dist:
        min_dist = abs(liquids[i] + liquids[j])
        where = (i, j)
    j -= 1

print(*(liquids[i] for i in where), sep=' ')
