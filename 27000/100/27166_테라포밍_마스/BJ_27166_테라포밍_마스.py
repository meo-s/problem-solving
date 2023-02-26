# https://www.acmicpc.net/problem/27166

def calc_required_time(credit, regen, tr_cost, tr_goal):
    x = tr_cost * tr_goal - credit
    return (x // regen) if x % regen == 0 else (x // regen + 1)


credit, regen, regen_cost, tr_upcost, tr_goal = map(int, input().split())

now = 0
ans = calc_required_time(credit, regen, tr_upcost, tr_goal)
if ans <= tr_goal:
    ans = tr_goal
else:
    while regen < tr_upcost:
        now += 1
        credit += regen
        if regen_cost <= credit:
            credit -= regen_cost
            regen += 1
            ans = min(ans, now + calc_required_time(credit, regen, tr_upcost, tr_goal))

print(ans)
