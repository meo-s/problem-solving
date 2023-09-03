# https://www.acmicpc.net/problem/5525

N, M = int(input()), int(input())
S = input()

cnt = 0
i = 0
while i < M:
    if S[i] == 'I':
        OIs = 0
        while i + 3 <= M and S[i + 1:i + 3] == 'OI':
            OIs += 1
            if N <= OIs:
                cnt += 1
            i += 2
    i += 1

print(cnt)
