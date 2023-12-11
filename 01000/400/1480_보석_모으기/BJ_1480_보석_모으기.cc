// https://www.acmicpc.net/problem/1480

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

using sz = std::size_t;
using u16 = std::uint16_t;
using i32 = std::int32_t;

namespace ranges = std::ranges;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

template <typename Callable>
void PickJewels(std::vector<i32> const& jewels,
                i32 const c,
                u16 const base_bitmask,
                Callable&& callback,
                u16 const extra_bitmask = 0,
                sz const index = 0,
                i32 const num_jewels = 0,
                i32 const weight = 0) {
  if (index == jewels.size() || weight == c || c < weight + jewels[index]) {
    callback(base_bitmask | extra_bitmask, num_jewels);
    return;
  }

  PickJewels(jewels, c, base_bitmask, callback, extra_bitmask, index + 1,
             num_jewels, weight);
  if ((base_bitmask & (1 << index)) == 0 && weight + jewels[index] <= c) {
    PickJewels(jewels, c, base_bitmask, callback, extra_bitmask | (1 << index),
               index + 1, num_jewels + 1, weight + jewels[index]);
  }
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<i32>(std::cin);
  auto const m = Read<i32>(std::cin);
  auto const c = Read<i32>(std::cin);
  auto jewels = std::vector<i32>(n);
  std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(jewels));
  ranges::sort(ranges::views::all(jewels));

  auto dp = std::array{std::vector<i32>(sz(1) << n, -1),
                       std::vector<i32>(sz(1) << n, -1)};
  dp[1][0] = 0;
  for (auto k = i32(); k < m; ++k) {
    std::swap(dp[0], dp[1]);
    for (auto i = sz(); i < dp[0].size(); ++i) {
      if (dp[0][i] == -1) {
        continue;
      }
      PickJewels(jewels, c, static_cast<u16>(i),
                 [&dp, i](u16 const bitmask, i32 const num_jewels) mutable {
                   dp[1][bitmask] =
                       std::max(dp[1][bitmask], dp[0][i] + num_jewels);
                 });
    }
  }
  std::cout << ranges::max(ranges::views::all(dp[1])) << std::endl;
  return 0;
}
