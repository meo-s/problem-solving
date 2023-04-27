# https://www.acmicpc.net/problem/3015

import sys


def readline():
    return sys.stdin.readline().rstrip()


N = int(readline())
people = [int(readline()) for _ in range(N)]

st = []
conts = []
n_pairs = 0
for i in range(N):
    while 0 < len(st) and st[-1] < people[i]:
        n_pairs += 1
        conts.pop()
        st.pop()

    conts.append((conts[-1] + 1) if 0 < len(st) and st[-1] == people[i] else 0)
    st.append(people[i])

    n_pairs += int(1 < len(st)) + conts[-1]
    n_pairs -= int(1 < len(st) and st[0] == st[-1])

print(n_pairs)
