// https://www.acmicpc.net/problem/14284

#include <algorithm>
#include <cstdint>
#include <functional>
#include <iostream>
#include <iterator>
#include <limits>
#include <utility>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using Edge = pair<i32, i32>;
using Graph = vector<vector<Edge>>;

template <typename T> class Heap {
  public:
    explicit Heap(function<bool(T const&, T const&)> less) : _less{less} {}

    bool empty() const { return _data.size() == 0; }

    T const& top() const { return _data[0]; }

    template <typename... TArgs> void emplace(TArgs&&... args) {
        _data.emplace_back(forward<TArgs>(args)...);
        up(i32(_data.size() - 1));
    }

    void pop() {
        swap(0, i32(_data.size() - 1));
        down(0, i32(_data.size() - 1));
        _data.pop_back();
    }

  private:
    void swap(i32 i, i32 j) { ::swap(_data[i], _data[j]); }

    void up(i32 u) {
        for (auto v = (u - 1) / 2; v != u && _less(_data[u], _data[v]);) {
            swap(u, v);
            u = v;
            v = (u - 1) / 2;
        }
    }

    void down(i32 u, i32 const n) {
        for (auto v = u * 2 + 1; 0 < v && v < n;) {
            if (v + 1 < n && _less(_data[v + 1], _data[v])) {
                ++v;
            }
            if (!_less(_data[v], _data[u])) {
                break;
            }
            swap(v, u);
            u = v;
            v = u * 2 + 1;
        }
    }

    function<bool(T const&, T const&)> _less;
    vector<T> _data;
};

i32 dijkstra(Graph const g, pair<i32, i32> const vertices) {
    auto const less = [](Edge const& lhs, Edge const& rhs) { return lhs.second < rhs.second; };
    auto dists = vector<i32>(g.size(), numeric_limits<i32>::max());
    dists[vertices.first] = 0;
    auto waypoints = Heap<pair<i32, i32>>{less};
    waypoints.emplace(vertices.first, 0);

    while (!waypoints.empty()) {
        auto const [u, dist] = waypoints.top();
        waypoints.pop();
        if (dist != dists[u]) {
            continue;
        }

        for (auto const& [v, w] : g[u]) {
            if (dist + w < dists[v]) {
                dists[v] = dist + w;
                waypoints.emplace(v, dists[v]);
            }
        }
    }

    return dists[vertices.second];
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};
    auto g = Graph(sz(*in));
    for (auto m = *++in; 0 < m; --m) {
        auto const u = *++in - 1;
        auto const v = *++in - 1;
        auto const w = *++in;
        g[u].emplace_back(v, w);
        g[v].emplace_back(u, w);
    }

    auto const u = *++in - 1;
    auto const v = *++in - 1;
    cout << dijkstra(g, {u, v}) << endl;
    return 0;
}
