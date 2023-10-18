// https://www.acmicpc.net/problem/2668

#include <algorithm>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using Graph = vector<i32>;

bool dfs(Graph const& g, vector<i32>& visited, i32 const u, i32 const p) {
    visited[u] = p;
    if (g[u] == p || (visited[g[u]] == -1 && dfs(g, visited, g[u], p))) {
        return true;
    } else {
        visited[u] = -1;
        return false;
    }
}

int main(int const argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};
    auto g = Graph(sz(*in));
    for (auto& v : g) {
        v = *++in - 1;
    }

    auto visited = vector<i32>(g.size(), -1);
    for (sz i = 0; i < visited.size(); ++i) {
        if (visited[i] == -1) {
            dfs(g, visited, i, i);
        }
    }

    cout << count_if(visited.begin(), visited.end(), [](auto const v) { return v != -1; }) << '\n';
    for (sz i = 0; i < visited.size(); ++i) {
        if (visited[i] != -1) {
            cout << i + 1 << '\n';
        }
    }

    return 0;
}
