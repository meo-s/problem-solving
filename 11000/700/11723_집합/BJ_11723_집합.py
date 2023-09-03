# https://www.acmicpc.net/problem/11723

import sys

readline = lambda: sys.stdin.readline().strip()

S = [0] * 21
def add(x):
    S[x] |= 1
def remove(x):
    S[x] = 0
def check(x):
    print(S[x])
def toggle(x):
    S[x] ^= 1
def all_():
    global S
    S = [1] * 21
def empty():
    global S
    S = [0] * 21

funcs = {'ad': add, 're': remove, 'ch': check, 'to': toggle, 'al': all_, 'em': empty}
for _ in range(int(readline())):
    cmd, *args = readline().split()
    funcs[cmd[:2]](*map(int, args))
