// https://www.acmicpc.net/problem/17835

#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <queue>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;

struct SharedContext {
    vector<vector<pair<i32, i32>>> g;
    vector<i32> rooms;
};

SharedContext setup() {
    auto in = istream_iterator<i32>{cin};

    auto const N = *in;
    auto const M = *++in;
    auto const K = *++in;

    auto g = vector<vector<pair<i32, i32>>>(sz(N));
    for (i32 i = 0; i < M; ++i) {
        auto const u = *++in - 1;
        auto const v = *++in - 1;
        auto const w = *++in;
        g[v].emplace_back(u, w);
    }

    auto rooms = vector<i32>(sz(K));
    for (i32 i = 0; i < K; ++i) {
        rooms[i] = *++in - 1;
    }

    return {move(g), move(rooms)};
}

void dijkstra(vector<vector<pair<i32, i32>>> const& g, vector<i64>& dists, vector<i32> const& starts) {
    struct Comparator {
        bool operator()(pair<i32, i64> const lhs, pair<i32, i64> const rhs) { return rhs.second < lhs.second; }
    };

    auto waypoints = priority_queue<pair<i32, i64>, vector<pair<i32, i64>>, Comparator>{};
    for (auto const start : starts) {
        dists[start] = 0;
        waypoints.emplace(start, 0LL);
    }

    while (!waypoints.empty()) {
        auto const [u, dist] = waypoints.top();
        waypoints.pop();

        if (dists[u] != dist) {
            continue;
        }

        for (auto const& [v, w] : g[u]) {
            if (dist + w < dists[v]) {
                dists[v] = dist + w;
                waypoints.emplace(v, dists[v]);
            }
        }
    }
}

void solve(SharedContext const& ctx) {
    auto dists = vector<i64>(ctx.g.size(), numeric_limits<i64>::max());
    dijkstra(ctx.g, dists, ctx.rooms);

    auto worstCity = -1;
    auto maxDist = numeric_limits<i64>::min();
    for (sz i = 0; i < dists.size(); ++i) {
        if (maxDist < dists[i]) {
            maxDist = dists[i];
            worstCity = i + 1;
        }
    }

    cout << worstCity << '\n' << maxDist << endl;
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    solve(setup());

    return 0;
}
