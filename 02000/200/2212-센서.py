# https://www.acmicpc.net/problem/2212

_, K = input(), int(input())
sensors = sorted(set(map(int, input().split())))
distances = sorted((s2 - s1 for s1, s2 in zip(sensors[:-1], sensors[1:])), reverse=True)
print((sensors[-1] - sensors[0]) - sum(distances[:K - 1]))
