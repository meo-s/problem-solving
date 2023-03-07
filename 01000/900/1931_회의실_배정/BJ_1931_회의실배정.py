# https://www.acmicpc.net/problem/1931

meetings = [tuple(map(int, input().split(' '))) for _ in range(int(input()))]
meetings = sorted(meetings, key=lambda v: v[0], reverse=True)
meetings = sorted(meetings, key=lambda v: v[1], reverse=True)

n_availables = 0
last_end_time = 0
while 0 < len(meetings):
    for _ in range(len(meetings)):
        begin_time, end_time = meetings.pop()
        if last_end_time <= begin_time:
            last_end_time = end_time 
            n_availables += 1
            break

print(n_availables)
