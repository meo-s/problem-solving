// https://www.acmicpc.net/problem/4225

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
constexpr auto MAX_POINT_COUNT = sz(100);

template <typename T> struct Vec2 {
    T x, y;
};

template <typename T> T CCW(Vec2<T> const& o, Vec2<T> const& u, Vec2<T> const& v) {
    return (u.x - o.x) * (v.y - o.y) - (u.y - o.y) * (v.x - o.x);
}

template <typename T> void ConvexHull(std::vector<Vec2<T>> const& points, std::vector<Vec2<T>>& out) {
    out.resize(MAX_POINT_COUNT + 1);
    auto k = sz();
    for (auto i = sz(); i < points.size(); ++i) {
        while (2 <= k && CCW(out[k - 2], out[k - 1], points[i]) <= 0) {
            --k;
        }
        out[k++] = points[i];
    }
    for (auto i = points.size() - 1, t = k + 1; 0 < i; --i) {
        while (t <= k && CCW(out[k - 2], out[k - 1], points[i - 1]) <= 0) {
            --k;
        }
        out[k++] = points[i - 1];
    }
    if (0 < k) {
        out.resize(k - 1);
    }
}

template <typename T> double Measure(std::vector<Vec2<T>> const& convex_hull) {
    auto min_width = std::numeric_limits<double>::max();
    for (auto i = sz(), k = sz(1); i < convex_hull.size(); ++i) {
        auto const& pi = convex_hull[i];
        auto const& pj = convex_hull[(i + 1) % convex_hull.size()];
        for (;;) {
            auto const nk = (k + 1) % convex_hull.size();
            if (CCW(pi, pj, convex_hull[nk]) <= CCW(pi, pj, convex_hull[k])) {
                auto const dx = pj.x - pi.x;
                auto const dy = pj.y - pi.y;
                auto const width = CCW(pi, pj, convex_hull[k]) / std::sqrt(double(dx * dx + dy * dy));
                min_width = std::min(min_width, width);
                break;
            }
            k = (k + 1) % convex_hull.size();
        }
    }
    return min_width;
}

template <typename T> std::istream& operator>>(std::istream& is, Vec2<T>& v) { return is >> v.x >> v.y; }

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto points = std::vector<Vec2<i32>>(MAX_POINT_COUNT);
    auto convex_hull = std::vector<Vec2<i32>>(MAX_POINT_COUNT + 1);
    for (auto t = 1;;) {
        points.resize(Read<std::size_t>(std::cin));
        if (points.size() == 0) {
            break;
        }
        for (auto& point : points) {
            std::cin >> point;
        }

        std::sort(std::begin(points), std::end(points),
                  [](auto const& lhs, auto const& rhs) { return lhs.x < rhs.x || (lhs.x == rhs.x && lhs.y < rhs.y); });
        ConvexHull(points, convex_hull);
        auto const ans = i32(std::ceil(Measure(convex_hull) * 100));
        std::printf("Case %d: %d.%02d\n", t++, ans / 100, ans % 100);
    }

    return 0;
}
