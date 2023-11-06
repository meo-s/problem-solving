// https://www.acmicpc.net/problem/20149

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <type_traits>
#include <utility>

using u8 = std::uint8_t;
using i64 = std::int64_t;

template <typename T> struct Vec2;
template <typename T> using Line = std::pair<Vec2<i64>, Vec2<i64>>;

enum class Intersection : u8 { None, Point, LineSegment };

template <typename T> struct Vec2 {
    T x, y;

    Vec2() = default;

    Vec2(T const& x, T const& y) : x(x), y(y) {}

    bool operator==(Vec2 const& other) const { return x == other.x && y == other.y; }

    template <typename U, typename R = std::common_type_t<T, U>> Vec2<R> operator+(Vec2<U> const& other) const {
        return {x + other.x, y + other.y};
    }

    template <typename U, typename R = std::common_type_t<T, U>> Vec2<R> operator-(Vec2<U> const& other) const {
        return {x - other.x, y - other.y};
    }

    T Dot(Vec2 const& other) { return x * other.x + y * other.y; }

    Vec2 Scale(T const& scalar) { return {x * scalar, y * scalar}; }

    template <typename U> static Vec2 Cast(Vec2<U> const& v) { return {static_cast<T>(v.x), static_cast<T>(v.y)}; }

    static T CCW(Vec2 const& o, Vec2 const& u, Vec2 const& v) {
        return (u.x - o.x) * (v.y - o.y) - (u.y - o.y) * (v.x - o.x);
    }
};

template <typename T> std::istream& operator>>(std::istream& is, Vec2<T>& v) { return is >> v.x >> v.y; }

template <typename T> std::ostream& operator<<(std::ostream& os, Vec2<T> const& v) { return os << v.x << ' ' << v.y; }

template <typename T> T Sign(T const& v) { return v != 0 ? (0 < v ? 1 : -1) : 0; }

template <typename T, typename R = double>
std::pair<Intersection, Vec2<double>> Intersects(Line<T> const& a, Line<T> const& b) {
    auto const contains = [](auto const& line, auto const& point) {
        auto const [x1, x2] = std::minmax(line.first.x, line.second.x);
        auto const [y1, y2] = std::minmax(line.first.y, line.second.y);
        return x1 <= point.x && point.x <= x2 && y1 <= point.y && point.y <= y2;
    };

    auto const a0a1b0 = Sign(Vec2<T>::CCW(a.first, a.second, b.first));
    auto const a0a1b1 = Sign(Vec2<T>::CCW(a.first, a.second, b.second));
    auto const b0b1a0 = Sign(Vec2<T>::CCW(b.first, b.second, a.first));
    auto const b0b1a1 = Sign(Vec2<T>::CCW(b.first, b.second, a.second));
    if (a0a1b0 == 0 || a0a1b1 == 0 || b0b1a0 == 0 || b0b1a1 == 0) {
        auto const contains_ab0 = (contains(a, b.first) && a0a1b0 == 0);
        auto const contains_ab1 = (contains(a, b.second) && a0a1b1 == 0);
        auto const contains_ba0 = (contains(b, a.first) && b0b1a0 == 0);
        auto const contains_ba1 = (contains(b, a.second) && b0b1a1 == 0);
        auto const contains_any = (contains_ab0 || contains_ab1 || contains_ba0 || contains_ba1);
        if (a.first == b.first && !contains_ba1 && !contains_ab1) {
            return {Intersection::Point, Vec2<R>::Cast(a.first)}; // a0 & b0
        }
        if (a.first == b.second && !contains_ba1 && !contains_ab0) {
            return {Intersection::Point, Vec2<R>::Cast(a.first)}; // a0 & b1
        }
        if (a.second == b.first && !contains_ba0 && !contains_ab1) {
            return {Intersection::Point, Vec2<R>::Cast(a.second)}; // a1 & b0
        }
        if (a.second == b.second && !contains_ba0 && !contains_ab0) {
            return {Intersection::Point, Vec2<R>::Cast(a.second)}; // a1 & b1
        }
        if (!contains_any || (a0a1b0 == 0 && a0a1b1 == 0 && b0b1a0 == 0 && b0b1a1 == 0)) {
            return {contains_any ? Intersection::LineSegment : Intersection::None, {}};
        }
    } else {
        if (0 < a0a1b0 * a0a1b1 || 0 < b0b1a0 * b0b1a1) {
            return {Intersection::None, {}};
        }
    }

    auto const u = b.first - a.first;
    auto const v = b.second - a.first;
    auto const w = a.second - a.first;
    auto const num = u.x * w.y - u.y * w.x;
    auto const den = w.x * (v.y - u.y) - w.y * (v.x - u.x);
    auto const vu = Vec2<R>::Cast(v - u).Scale(double(num) / den);
    return {Intersection::Point, (u + vu) + a.first};
}

int main(int const argc, char const* argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);
    std::cout.precision(16);

    auto a = Line<i64>();
    auto b = Line<i64>();
    std::cin >> a.first >> a.second >> b.first >> b.second;
    if (auto [itype, ipoint] = Intersects<i64>(a, b); itype == Intersection::None) {
        std::cout << 0 << std::endl;
    } else {
        std::cout << "1\n";
        if (itype == Intersection::Point) {
            std::cout << ipoint << '\n';
        }
    }

    return 0;
}
