// https://www.acmicpc.net/problem/2458

#include <iostream>
#include <vector>
#include <cstdint>
#include <algorithm>
#include <array>

using namespace std;
using sz = size_t;

struct Person {
    array<bool, 500> knowns;
    sz num_knowns;
};

auto people = array<Person, 500>{};

void propagate(vector<vector<sz>> const& edges, sz const p, sz const u) {
    if (!people[u].knowns[p]) {
        people[u].knowns[p] = true;
        people[p].knowns[u] = true;
        ++people[u].num_knowns;
        ++people[p].num_knowns;
    }

    for (auto const v : edges[u]) {
        if (!people[v].knowns[p]) propagate(edges, p, v);
    }
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    sz N, M; cin >> N >> M;
    auto edges = vector<vector<sz>>(N);
    for (sz i = 0; i < M; ++i) {
        sz u, v; cin >> u >> v;
        edges[u - 1].emplace_back(v - 1);
    }
       
    for (sz u = 0; u < N; ++u) {
        for (auto const v : edges[u]) {
            if (!people[v].knowns[u]) propagate(edges, u, v);
        }
    }

    cout << count_if(people.begin(), people.begin() + N, [&N](Person const& p) { return p.num_knowns == N - 1; }) << endl;
    return 0;
}
