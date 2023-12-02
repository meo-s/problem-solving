// https://www.acmicpc.net/problem/3679

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <numeric>
#include <ranges>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T>
struct Vec2 {
  T x, y;
};

template <typename T>
std::istream& operator>>(std::istream& is, Vec2<T>& v) {
  return is >> v.x >> v.y;
}

template <typename T>
T CCW(Vec2<T> const& o, Vec2<T> const& u, Vec2<T> const& v) {
  return (u.x - o.x) * (v.y - o.y) - (v.x - o.x) * (u.y - o.y);
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  std::cin >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  constexpr static auto MAX_POINT_COUNT = sz(2000);
  auto points = std::vector<Vec2<i32>>(MAX_POINT_COUNT);
  auto indices = std::vector<i32>(MAX_POINT_COUNT);
  auto vertices = std::vector<i32>(MAX_POINT_COUNT);
  auto in_vertices = std::vector<bool>(MAX_POINT_COUNT);
  for (auto t = Read<i32>(std::cin); 0 < t--;) {
    points.resize(Read<i32>(std::cin));
    std::copy_n(std::istream_iterator<Vec2<i32>>(std::cin), points.size(),
                std::begin(points));

    indices.resize(points.size());
    std::iota(std::begin(indices), std::end(indices), 0);
    std::sort(std::begin(indices), std::end(indices),
              [&points](auto const& i, auto const& j) {
                auto const& lhs = points[i];
                auto const& rhs = points[j];
                return lhs.x != rhs.x ? lhs.x < rhs.x : lhs.y < rhs.y;
              });

    vertices.resize(0);
    in_vertices.resize(points.size());
    std::fill(std::begin(in_vertices), std::end(in_vertices), false);
    for (auto const i : indices) {
      while (2 <= vertices.size() &&
             CCW(points[vertices[vertices.size() - 2]], points[vertices.back()],
                 points[i]) < 0) {
        in_vertices[vertices.back()] = false;
        vertices.pop_back();
      }
      in_vertices[i] = true;
      vertices.emplace_back(static_cast<i32>(i));
    }

    for (auto const i : vertices) {
      std::cout << i << ' ';
    }
    for (auto const i : std::views::reverse(indices)) {
      if (!in_vertices[i]) {
        std::cout << i << ' ';
      }
    }
    std::cout << '\n';
  }

  return 0;
}
