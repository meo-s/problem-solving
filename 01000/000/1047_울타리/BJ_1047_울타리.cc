// https://www.acmicpc.net/problem/1047

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

struct Point {
  i32 x;
  i32 y;
};

struct Tree : public Point {
  i32 length;
};

[[nodiscard]] bool Contains(Point const& lb, Point const& rt, Point const& pt) {
  return lb.x <= pt.x && pt.x <= rt.x && lb.y <= pt.y && pt.y <= rt.y;
}

std::istream& operator>>(std::istream& is, Tree& tree) {
  return is >> tree.x >> tree.y >> tree.length;
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

template <typename ForwardIterator, typename Callable>
void ForEachPair(ForwardIterator const& start, ForwardIterator const& end,
                 Callable&& fn) {
  for (auto one = start; one != end; ++one) {
    for (auto two = one; two != end; ++two) {
      fn(*one, *two);
    }
  }
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<sz>(std::cin);
  auto trees = std::vector<Tree>(n);
  auto x_coords = std::vector<i32>(n);
  auto y_coords = std::vector<i32>(n);
  std::copy_n(std::istream_iterator<Tree>(std::cin), n, std::begin(trees));
  rng::copy(
      trees | rng::views::transform([](auto const& tree) { return tree.x; }),
      std::begin(x_coords));
  rng::copy(
      trees | rng::views::transform([](auto const& tree) { return tree.y; }),
      std::begin(y_coords));
  rng::sort(x_coords);
  rng::sort(y_coords);
  rng::sort(trees, [](auto const& lhs, auto const& rhs) {
    return rhs.length < lhs.length;
  });

  auto const simulate = [&trees](Point const& lb, Point const& rt) {
    auto num_cuts = i32();
    auto total_length = i32();
    for (auto const& tree : trees) {
      if (!Contains(lb, rt, tree)) {
        ++num_cuts;
        total_length += tree.length;
      }
    }
    if ((rt.y - lb.y) * 2 + (rt.x - lb.x) * 2 <= total_length) {
      return num_cuts;
    }
    for (auto const& tree : trees) {
      if (Contains(lb, rt, tree)) {
        ++num_cuts;
        total_length += tree.length;
      }
      if ((rt.y - lb.y) * 2 + (rt.x - lb.x) * 2 <= total_length) {
        break;
      }
    }
    return num_cuts;
  };

  auto ans = static_cast<i32>(n);
  ForEachPair(std::cbegin(y_coords), std::cend(y_coords),
              [&](auto const& y1, auto const& y2) mutable {
                ForEachPair(std::cbegin(x_coords), std::cend(x_coords),
                            [&](auto const& x1, auto const& x2) mutable {
                              ans = std::min(ans, simulate({x1, y1}, {x2, y2}));
                            });
              });
  std::cout << ans << std::endl;
  return 0;
}
