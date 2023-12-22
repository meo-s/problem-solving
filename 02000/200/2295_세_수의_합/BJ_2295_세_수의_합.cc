// https://www.acmicpc.net/problem/2295

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <ranges>
#include <vector>

namespace ranges = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  constexpr auto for_each_nested_iota = [](auto const beg, auto const end,
                                           auto&& consumer) {
    for (auto const i : ranges::views::iota(beg, end)) {
      for (auto const j : ranges::views::iota(beg, end)) {
        consumer(i, j);
      }
    }
  };

  auto const n = Read<sz>(std::cin);
  auto u = std::vector<i32>(n);
  std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(u));

  auto sums = std::vector<i64>(0);
  sums.reserve((n * (n + 1)) / 2);
  for_each_nested_iota(sz(), n,
                       [&u, &sums](auto const i, auto const j) mutable {
                         sums.emplace_back(u[i] + u[j]);
                       });

  auto ans = std::numeric_limits<i32>::min();
  std::sort(std::begin(sums), std::end(sums));
  for_each_nested_iota(
      sz(), n, [&u, &sums, &ans](auto const i, auto const j) mutable {
        if (ranges::binary_search(sums, static_cast<i64>(u[i]) - u[j])) {
          ans = std::max(ans, u[i]);
        }
      });
  std::cout << ans << std::endl;
  return 0;
}
