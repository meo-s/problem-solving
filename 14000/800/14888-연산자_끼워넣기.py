# https://www.acmicpc.net/problem/14888

input()
nums = [*map(int, input().split())]
remains = [*map(int, input().split())]

add = lambda lhs, rhs: lhs + rhs
sub = lambda lhs, rhs: lhs - rhs
mul = lambda lhs, rhs: lhs * rhs
div = lambda lhs, rhs: lhs // rhs if 0<lhs*rhs else -(-lhs // rhs)
ops = [add, sub, mul, div]


def search(v=nums[0], offset=1):
    if len(nums) <= offset:
        return v, v

    totalmax, totalmin = float('-inf'), float('inf')
    for i in range(len(ops)):
        if 0 < remains[i]:
            remains[i] -= 1
            submax, submin = search(ops[i](v, nums[offset]), offset + 1)
            remains[i] += 1
            totalmax = max(totalmax, submax)
            totalmin = min(totalmin, submin)

    return tuple(map(int, [totalmax, totalmin]))


print(*search(),sep='\n')
