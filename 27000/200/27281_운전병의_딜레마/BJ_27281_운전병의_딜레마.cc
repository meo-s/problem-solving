// https://www.acmicpc.net/problem/27281

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <queue>
#include <utility>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;
using Edge = tuple<i32, i32, i32>;
using Graph = vector<vector<Edge>>;
constexpr auto Inf = numeric_limits<i64>::max();

void dijkstra(Graph const& g, i32 const max_t, i32 const max_s, vector<i64>& dists) {
    using Waypoint = pair<i32, i64>;
    struct Greater {
        bool operator()(Waypoint const& lhs, Waypoint const& rhs) { return rhs.second < lhs.second; }
    };

    fill(dists.begin(), dists.end(), Inf);
    dists[0] = 0;

    auto waypoints = priority_queue<Waypoint, vector<Waypoint>, Greater>{};
    waypoints.emplace(0, 0);

    while (!waypoints.empty()) {
        auto const [u, t0] = waypoints.top();
        waypoints.pop();
        if (dists[u] != t0) {
            continue;
        }

        for (auto const& [v, t, s] : g[u]) {
            auto const nt = t0 + t + max(s - max_s, 0);
            if (nt <= max_t && nt < dists[v]) {
                dists[v] = nt;
                waypoints.emplace(v, nt);
            }
        }
    }
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};
    auto const n = *in;
    auto const m = *++in;
    auto const t = *++in;
    auto g = Graph(sz(n));
    for (i32 i = 0; i < m; ++i) {
        auto const u = *++in - 1;
        auto const v = *++in - 1;
        auto const t = *++in;
        auto const s = *++in;
        g[u].emplace_back(v, t, s);
        g[v].emplace_back(u, t, s);
    }

    auto dists = vector<i64>(g.size());
    auto ans = numeric_limits<i32>::max();
    auto lb = i32{};
    auto ub = 1'000'000'001;
    while (lb < ub) {
        auto const max_s = (lb + ub) / 2;
        dijkstra(g, t, max_s, dists);
        if (dists.back() != Inf) {
            ans = min(ans, max_s);
            ub = max_s;
        } else {
            lb = max_s + 1;
        }
    }

    cout << (ans != numeric_limits<i32>::max() ? ans : -1) << endl;
    return 0;
}
