// https://www.acmicpc.net/problem/2699

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T> struct Point {
    T x, y;

    Point() = default;

    Point(T const x, T const y) : x(x), y(y) {}

    bool operator<(Point const& other) const { return other.y < y || (y == other.y && x < other.x); }
};

template <typename T> T ccw(Point<T> const& o, Point<T> const& u, Point<T> const& v) {
    return (u.x - o.x) * (v.y - o.y) - (v.x - o.x) * (u.y - o.y);
}

void solve() {
    auto in = std::istream_iterator<i32>(std::cin);
    auto points = std::vector<Point<i32>>(sz(*in));
    for (auto& point : points) {
        point = {*++in, *++in};
    }

    std::sort(std::begin(points), std::end(points));

    auto convex_hull = std::vector<Point<i32>>(points.size() + 1);
    auto k = sz();
    for (auto const& point : points) {
        while (2 <= k && 0 <= ccw(convex_hull[k - 2], convex_hull[k - 1], point)) {
            --k;
        }
        convex_hull[k++] = point;
    }
    for (auto i = points.size() - 1, t = k; 0 < i; --i) {
        while (t < k && 0 <= ccw(convex_hull[k - 2], convex_hull[k - 1], points[i - 1])) {
            --k;
        }
        convex_hull[k++] = points[i - 1];
    }

    convex_hull.resize(k - 1);
    std::cout << convex_hull.size() << '\n';
    for (auto const& vertex : convex_hull) {
        std::cout << vertex.x << ' ' << vertex.y << '\n';
    }
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto t = i32();
    std::cin >> t;
    while (0 < t--) {
        solve();
    }

    std::cout.flush();
    return 0;
}