# https://www.acmicpc.net/problem/27312

import sys


def readline():
    return sys.stdin.readline().rstrip()


M, N, Q = map(int, readline().split())
A = [*map(int, readline().split())]

proposal = [1] * N
for i in range(M):
    sys.stdout.write(f'? {i+1} {i+1}\n')
    sys.stdout.flush()
    proposal[i] = int(readline()) % A[i] + 1

print(f'! {" ".join(map(str, proposal))}')
