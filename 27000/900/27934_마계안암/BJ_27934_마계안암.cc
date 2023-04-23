// https://www.acmicpc.net/problem/27934

#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <queue>
#include <utility>
#include <vector>

using namespace std;
using i32 = int32_t;
using i64 = int64_t;

constexpr i64 M = 998'244'353;
constexpr i64 NOT_CACHED = -2;

void dijkstra(vector<vector<pair<i32, i32>>> const& edges, vector<i64>& dists, vector<vector<i32>>& traces) {
    auto waypoints = priority_queue<pair<i64, i32>>{};
    waypoints.emplace(0, 0);
    while (0 < waypoints.size()) {
        auto const [dist, u] = waypoints.top();
        waypoints.pop();
        if (dists[u] < -dist) continue;

        for (auto const& [v, w] : edges[u]) {
            if (-dist + w == dists[v]) traces[v].emplace_back(u);
            else if (-dist + w < dists[v]) {
                dists[v] = -dist + w;
                traces[v].clear();
                traces[v].emplace_back(u);
                waypoints.emplace(dist - w, v);
            }
        }
    }
}

i64 dfs(vector<vector<i32>> const& traces, vector<bool>& visited, vector<i64>& dp, i32 const u = 0) {
    if (visited[u]) dp[u] = -1;
    if (dp[u] == NOT_CACHED) {
        visited[u] = true;
        dp[u] = 0;
        for (auto const v : traces[u]) {
            auto const possibles = dfs(traces, visited, dp, v);
            if (possibles != -1) dp[u] = (dp[u] + possibles) % M;
            else {
                dp[u] = -1;
                break;
            }
        }
        visited[u] = false;
    }
    return dp[u];
}

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<i32>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto V = *in;
    auto E = *++in;
    auto edges = vector<vector<pair<i32, i32>>>(V);
    for (auto i = 0; i < E; ++i) {
        auto const u = *++in - 1;
        auto const v = *++in - 1;
        auto const w = *++in;
        edges[u].emplace_back(v, w);
    }

    auto dists = vector<i64>(V, numeric_limits<i64>::max());
    dists[0] = 0;
    auto traces = vector<vector<i32>>(V);
    dijkstra(edges, dists, traces);

    auto visited = vector<bool>(V);
    auto dp = vector<i64>(V, NOT_CACHED);
    dp[0] = traces[0].size() == 0 ? 1 : -1;
    for (auto i = 0; i < V; ++i) {
        dfs(traces, visited, dp, i);
    }

    for (auto const possibles : dp) {
        cout << possibles << '\n';
    }

    cout.flush();
    return 0;
}
