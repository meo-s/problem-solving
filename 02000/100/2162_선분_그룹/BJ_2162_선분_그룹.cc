// https://www.acmicpc.net/problem/2162

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <unordered_map>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T> struct Vec2 {
    T x, y;
};

template <typename T> std::istream& operator>>(std::istream& is, Vec2<T>& v) { return is >> v.x >> v.y; }

template <typename T> T CCW(Vec2<T> const& o, Vec2<T> const& u, Vec2<T> const& v) {
    return (u.x - o.x) * (v.y - o.y) - (u.y - o.y) * (v.x - o.x);
}

template <typename T> T Sign(T const& v) { return v == 0 ? 0 : (0 < v ? 1 : -1); }

template <typename T> bool Intersects(std::pair<Vec2<T>, Vec2<T>> const& a, std::pair<Vec2<T>, Vec2<T>> const& b) {
    auto const contains = [](std::pair<Vec2<T>, Vec2<T>> const& line, Vec2<T> const& p) {
        auto const [x_min, x_max] = std::minmax(line.first.x, line.second.x);
        auto const [y_min, y_max] = std::minmax(line.first.y, line.second.y);
        return x_min <= p.x && p.x <= x_max && y_min <= p.y && p.y <= y_max;
    };

    auto const a0a1b0 = Sign(CCW(a.first, a.second, b.first));
    if (a0a1b0 == 0 && contains(a, b.first)) {
        return true;
    }
    auto const a0a1b1 = Sign(CCW(a.first, a.second, b.second));
    if (a0a1b1 == 0 && contains(a, b.second)) {
        return true;
    }
    auto const b0b1a0 = Sign(CCW(b.first, b.second, a.first));
    if (b0b1a0 == 0 && contains(b, a.first)) {
        return true;
    }
    auto const b0b1a1 = Sign(CCW(b.first, b.second, a.second));
    if (b0b1a1 == 0 && contains(b, a.second)) {
        return true;
    }
    return a0a1b0 * a0a1b1 < 0 && b0b1a0 * b0b1a1 < 0;
}

class DisjointSet {

    mutable std::vector<sz> _parents;

  public:
    DisjointSet(sz const size) : _parents(size) { Clear(); }

    sz Size() const { return _parents.size(); }

    void Clear() {
        for (auto i = sz(); i < _parents.size(); ++i) {
            _parents[i] = i;
        }
    }

    sz Find(sz const u) const {
        if (_parents[u] != u) {
            _parents[u] = Find(_parents[u]);
        }
        return _parents[u];
    }

    bool Merge(sz const u, sz const v) {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp) {
            _parents[vp] = up;
        }
        return up != vp;
    }
};

int main(int const argc, char const* argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto n = sz();
    std::cin >> n;
    auto lineSegments = std::vector<std::pair<Vec2<i32>, Vec2<i32>>>(n);
    for (auto& lineSegment : lineSegments) {
        std::cin >> lineSegment.first >> lineSegment.second;
    }

    auto disjointSet = DisjointSet(lineSegments.size());
    for (auto i = sz(); i < disjointSet.Size(); ++i) {
        for (auto j = i + 1; j < disjointSet.Size(); ++j) {
            if (Intersects(lineSegments[i], lineSegments[j])) {
                disjointSet.Merge(i, j);
            }
        }
    }

    auto distributions = std::unordered_map<sz, i32>();
    for (auto i = sz(); i < disjointSet.Size(); ++i) {
        if (auto const iter = distributions.find(disjointSet.Find(i)); iter == distributions.end()) {
            distributions[disjointSet.Find(i)] = 1;
        } else {
            ++iter->second;
        }
    }

    auto const max_set_size =
        std::max_element(std::cbegin(distributions), std::cend(distributions), [](auto const& lhs, auto const& rhs) {
            return lhs.second < rhs.second;
        })->second;
    std::cout << distributions.size() << '\n' << max_set_size << std::endl;
    return 0;
}
