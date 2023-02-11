# https://www.acmicpc.net/problem/27424

import sys
import functools
import itertools


def readline():
    return sys.stdin.readline().rstrip()


@functools.lru_cache
def length_of(i):
    return length_of(i - 2) + length_of(i - 1) + 2 if 3 <= i else 2


def search_next(n, k):
    for i in itertools.count(5 - n % 2, step=2):
        if k == 1:
            return ')'

        if n < i:
            return '0'

        if k <= 1 + length_of(i):  # Si안에 포함되는가? (1=Si 이전의 닫는 괄호)
            return query(i, k - 1)
        else:
            k -= 1 + length_of(i)


def query(n, k):
    S = ['()0', '()0', '(()())0']  # S1, S2, S3는 하드 코딩
    if n <= len(S):
        return S[n - 1][min(k, len(S[n - 1])) - 1]

    if k <= (n + 1) // 2:  # 문자열의 첫 시작의 여는 괄호 개수
        return '('

    k -= (n + 1) // 2
    S = [')(()())', ')()']  # (S1S2), (S2S3)는 하드코딩
    if k <= len(S[n % 2]):
        return S[n % 2][k - 1]

    return search_next(n, k - len(S[n % 2]))


for _ in range(int(readline())):
    n, k = map(int, readline().split())
    print(query(n, k))
