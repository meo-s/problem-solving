// https://www.acmicpc.net/problem/22865

#include <algorithm>
#include <iostream>
#include <iterator>
#include <limits>
#include <queue>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using Graph = vector<vector<pair<i32, i32>>>;

i32 dijkstra(Graph const& g, vector<i32> const& starts) {
    using Waypoint = pair<i32, i32>;
    struct Comparator {
        bool operator()(Waypoint const& lhs, Waypoint const& rhs) { return rhs.second < lhs.second; }
    };

    auto dists = vector<i32>(g.size(), numeric_limits<i32>::max());
    auto waypoints = priority_queue<Waypoint, vector<Waypoint>, Comparator>();
    for (auto const u : starts) {
        dists[u] = 0;
        waypoints.emplace(u, 0);
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

    return i32(distance(dists.cbegin(), max_element(dists.cbegin(), dists.cend()))) + 1;
}

int main(int const argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};
    auto n = *in;
    auto friends = vector<i32>(3);
    for (i32 i = 0; i < 3; ++i) {
        friends[i] = *++in - 1;
    }
    auto g = Graph(sz(n));
    for (i32 m = *++in; 0 < m; --m) {
        auto const u = *++in - 1;
        auto const v = *++in - 1;
        auto const w = *++in;
        g[u].emplace_back(v, w);
        g[v].emplace_back(u, w);
    }

    cout << dijkstra(g, friends) << endl;
    return 0;
}
