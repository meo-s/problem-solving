# https://www.acmicpc.net/problem/1467

S, E = input(), input()
availables = [0] * 10
removables = [0] * 10
for c in S:
    availables[ord(c) - ord('0')] += 1
for c in E:
    removables[ord(c) - ord('0')] += 1

st = []
for c in S:
    c = ord(c) - ord('0')
    if availables[c] == removables[c]:
        availables[c] -= 1
        removables[c] -= 1
        continue

    while 0 < len(st) and st[-1] < c and 0 < removables[st[-1]]:
        removables[st.pop()] -= 1

    availables[c] -= 1
    st.append(c)

print(*st, sep='')
