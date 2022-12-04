# https://www.acmicpc.net/problem/9663


def search(N, depth=0, vertical_mask=0, ls_mask=0, rs_mask=0):
    if depth == N:
        return 1

    count = 0
    ls_mask, rs_mask = ls_mask << 1, rs_mask >> 1
    for i in range(N):
        if ((Q := 1 << i) & (vertical_mask | ls_mask | rs_mask)) == 0:
            count += search(N, depth + 1, vertical_mask | Q, ls_mask | Q, rs_mask | Q)

    return count


print(search(int(input())))
