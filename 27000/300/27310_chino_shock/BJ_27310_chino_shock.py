# https://www.acmicpc.net/problem/27310

emoji = input()
CHAR_COSTS = {':': 1, '_': 5}
print(sum(map(lambda ch: CHAR_COSTS.get(ch, 0), emoji)) + len(emoji))
