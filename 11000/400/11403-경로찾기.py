# https://www.acmicpc.net/problem/11403

N = int(input())
edges = [[*map(int, input().split())] for _ in range(N)]
connected = [[0] * N for _ in range(N)]

for target_node in range(N):
    visited = [False] * N
    waypoints = [target_node]
    while 0 < len(waypoints):
        node = waypoints.pop()
        for i in range(N):
            if not visited[i] and edges[node][i] == 1:
                visited[i] = True
                connected[target_node][i] = 1
                waypoints.append(i)

for i in range(N):
    print(' '.join(map(str, connected[i])))
