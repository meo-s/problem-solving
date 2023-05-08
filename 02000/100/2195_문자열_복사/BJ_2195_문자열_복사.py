# https://www.acmicpc.net/problem/2195

S, P = input(), input()
offsets = {}
for i, c in enumerate(S):
    if i == 0 or S[i - 1] != S[i]:
        offsets.setdefault(c, []).append(i)

n_copies = 0
length = 0
while length < len(P):
    max_substr_length = 0
    for offset in offsets[P[length]]:
        i = 1
        while (length + i < len(P) and offset + i < len(S)) and (P[length + i] == S[offset + i]):
            i += 1

        max_substr_length = max(max_substr_length, i)

    n_copies += 1
    length += max_substr_length

print(n_copies)
