def viewables(buildings, origin):
    n_viewables = 0
    for to in range(len(buildings)):
        if to != origin:
            is_interrupted = False

            x1, x2 = min(origin, to), max(origin, to)
            dy = (buildings[x2] - buildings[x1]) / (x2 - x1)
            for i in range(1, x2 - x1):
                if buildings[x1] + i * dy - buildings[x1 + i] <= 1e-8:
                    is_interrupted = True
                    break

            n_viewables += int(not is_interrupted)

    return n_viewables


N = int(input())
buildings = [*map(int, input().split())]
print(max(viewables(buildings, i) for i in range(N)))
