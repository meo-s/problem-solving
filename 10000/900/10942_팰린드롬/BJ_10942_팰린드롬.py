# https://www.acmicpc.net/problem/10942

import sys


def find_palindrome(seq, offset):
    even = 0
    while even < offset + 1 and even < len(seq) - (offset + 1):
        if not seq[offset - even] == seq[(offset + 1) + even]:
            break
        even += 1

    odd = 0
    while odd < min(offset + 1, len(seq) - offset):
        if not seq[offset - odd] == seq[offset + odd]:
            break
        odd += 1

    return even * 2, odd * 2 - 1


readline = lambda: sys.stdin.readline().strip()

N = int(readline())
seq = [*map(int, readline().split())]

palindromes = [find_palindrome(seq, i) for i in range(len(seq))]
for _ in range(int(readline())):
    beg, end = map(int, readline().split())
    beg -= 1  # convert to [beg, end)

    mid = (beg + end - 1) // 2
    print(int(end - beg <= palindromes[mid][(end - beg) & 1]))
