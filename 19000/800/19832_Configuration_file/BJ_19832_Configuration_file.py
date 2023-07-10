# https://www.acmicpc.net/problem/19832

import os
import io
from collections import defaultdict


class Scope:

    def __init__(self):
        self.valid = True

    def invalidate(self):
        self.valid = False


def take_valids(st):
    while 0 < len(st) and not st[-1][0].valid:
        st.pop()

    return st


def assign(st, current_scope, v):
    if st[-1][0] == current_scope:
        st[-1][1] = v
    else:
        st.append([current_scope, v])


_raw_readline = io.BytesIO(os.read(0, os.fstat(0).st_size)).readline
readline = lambda: _raw_readline().decode().rstrip()

scopes = [Scope()]
variables = defaultdict(lambda: [[scopes[0], 0]])

while 0 < len(line := readline()):
    if line in '{}':
        if line == '{':
            scopes.append(Scope())
        else:
            scopes[-1].invalidate()
            scopes.pop()
    else:
        k, v = line.split('=')
        var = take_valids(variables[k])

        if not 'a' <= v[0] <= 'z':
            assign(var, scopes[-1], int(v))
        else:
            assign(var, scopes[-1], v := take_valids(variables[v])[-1][1])
            print(v)
