// https://www.acmicpc.net/problem/17396

#include <algorithm>
#include <iostream>
#include <iterator>
#include <limits>
#include <queue>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;
using Graph = vector<vector<pair<i32, i32>>>;

i64 dijkstra(Graph const& g) {
    struct Comparator {
        bool operator()(pair<i32, i64> const& lhs, pair<i32, i64> const& rhs) { return lhs.second > rhs.second; }
    };

    auto dists = vector<i64>(g.size(), numeric_limits<i64>::max());
    dists[0] = 0;
    auto waypoints = priority_queue<pair<i32, i64>, vector<pair<i32, i64>>, Comparator>();
    waypoints.emplace(0, 0);

    while (!waypoints.empty()) {
        auto const [u, dist] = waypoints.top();
        waypoints.pop();
        if (dists[u] != dist) {
            continue;
        }
        for (auto const [v, w] : g[u]) {
            if (dist + w < dists[v]) {
                dists[v] = dist + w;
                waypoints.emplace(v, dists[v]);
            }
        }
    }

    return (dists.back() < numeric_limits<i64>::max()) ? dists.back() : -1;
}

int main(int const argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};

    auto const N = *in;
    auto const M = *++in;
    auto viewable = vector<i32>(sz(N));
    copy_n(++in, N, viewable.begin());
    auto g = Graph(sz(N));
    for (i32 i = 0; i < M; ++i) {
        auto const u = *++in;
        auto const v = *++in;
        auto const w = *++in;
        if (viewable[u] != 0 && u != N - 1) {
            continue;
        }
        if (viewable[v] != 0 && v != N - 1) {
            continue;
        }
        g[u].emplace_back(v, w);
        g[v].emplace_back(u, w);
    }

    cout << dijkstra(g) << endl;
    return 0;
}
