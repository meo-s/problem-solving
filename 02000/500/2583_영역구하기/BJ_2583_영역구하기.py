# https://www.acmicpc.net/problem/2583

H, W, K  = map(int, input().split())
regions = [[0] * W for _ in range(H)]

for _ in range(K):
    x1, y1, x2, y2 = map(int, input().split())
    for y in range(y1, y2):
        for x in range(x1, x2):
            regions[y][x] = -1

def calc_area(point):
    x, y = point
    regions[y][x] = 1

    area = 1
    points = [point]
    while 0 < len(points):
        x, y = points.pop()
        for dx, dy in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if (not 0 <= x + dx < W) or (not 0 <= y + dy < H):
                continue
            if regions[y + dy][x + dx] != 0:
                continue
            regions[y + dy][x + dx] = (area := area + 1)
            points.append((x + dx, y + dy))

    return area


areas = []
for y in range(H):
    for x in range(W):
        if regions[y][x] == 0:
            areas.append(calc_area((x, y)))

print(len(areas), ' '.join(map(str, sorted(areas))), sep='\n')
