// https://www.acmicpc.net/problem/17386

#include <iostream>
#include <tuple>
#include <utility>

template <typename T> using Point = std::pair<T, T>;

template <typename T> using Line = std::pair<Point<T>, Point<T>>;

template <typename T> inline T& GetX(Point<T>& point) { return std::get<0>(point); }

template <typename T> inline T const& GetX(Point<T> const& point) { return std::get<0>(point); }

template <typename T> inline T& GetY(Point<T>& point) { return std::get<1>(point); }

template <typename T> inline T const& GetY(Point<T> const& point) { return std::get<1>(point); }

template <typename T> inline std::istream& operator>>(std::istream& is, Line<T>& line) {
    std::cin >> GetX(std::get<0>(line)) >> GetY(std::get<0>(line)) >> GetX(std::get<1>(line)) >>
        GetY(std::get<1>(line));
    return is;
}

template <typename T> inline T Sign(T v) { return v != 0 ? (0 < v ? 1 : -1) : 0; }

template <typename T> inline T CCW(Point<T> const& o, Point<T> const& u, Point<T> const& v) {
    return (GetX(u) - GetX(o)) * (GetY(v) - GetY(o)) - (GetY(u) - GetY(o)) * (GetX(v) - GetX(o));
}

template <typename T> bool Intersect(Line<T> const& a, Line<T> const& b) {
    auto const a0a1b0 = Sign(CCW(std::get<0>(a), std::get<1>(a), std::get<0>(b)));
    auto const a0a1b1 = Sign(CCW(std::get<0>(a), std::get<1>(a), std::get<1>(b)));
    if (0 <= a0a1b0 * a0a1b1) {
        return false;
    }
    auto const b0b1a0 = Sign(CCW(std::get<0>(b), std::get<1>(b), std::get<0>(a)));
    auto const b0b1a1 = Sign(CCW(std::get<0>(b), std::get<1>(b), std::get<1>(a)));
    if (0 <= b0b1a0 * b0b1a1) {
        return false;
    }
    return true;
}

int main(int const argc, char const* argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto l1 = Line<long long>(), l2 = Line<long long>();
    std::cin >> l1 >> l2;
    std::cout << (Intersect(l1, l2) ? 1 : 0) << std::endl;
    return 0;
}
