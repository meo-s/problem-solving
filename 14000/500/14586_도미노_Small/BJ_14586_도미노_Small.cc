// https://www.acmicpc.net/problem/14586

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <ranges>
#include <tuple>
#include <vector>

#define left first
#define right second

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;
constexpr auto NEVAL = i32(-1);

struct Bar {
  i64 x;
  i64 h;
};

std::istream& operator>>(std::istream& is, Bar& bar) {
  return is >> bar.x >> bar.h;
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

auto CacheJumps(std::vector<Bar> const& bars) {
  auto jumps = std::vector<std::pair<i32, i32>>(bars.size());
  auto const n = static_cast<i32>(bars.size());
  for (auto const i : rng::views::iota(i32(), n)) {
    jumps[i] = {i, i};
    {
      auto t = bars[i].x - bars[i].h;
      while (0 < jumps[i].left && t <= bars[jumps[i].left - 1].x) {
        --jumps[i].left;
        t = std::min(t, bars[jumps[i].left].x - bars[jumps[i].left].h);
      }
    }
    {
      auto t = bars[i].x + bars[i].h;
      while (jumps[i].right + 1 < n && bars[jumps[i].right + 1].x <= t) {
        ++jumps[i].right;
        t = std::max(t, bars[jumps[i].right].x + bars[jumps[i].right].h);
      }
    }
  }
  return jumps;
}

i32 Simulate(std::vector<std::pair<i32, i32>> const& jumps,
             std::vector<std::vector<i32>>& dp, i32 const l, i32 const r) {
  if (r < l || l < 0 || static_cast<i32>(dp.size()) <= r) {
    return 0;
  }
  if (dp[l][r] == NEVAL) {
    dp[l][r] = r - l + 1;
    for (auto i = l; i <= r; ++i) {
      dp[l][r] = std::min({dp[l][r],
                           1 + Simulate(jumps, dp, l, jumps[i].left - 1) +
                               Simulate(jumps, dp, i + 1, r),
                           1 + Simulate(jumps, dp, l, i - 1) +
                               Simulate(jumps, dp, jumps[i].right + 1, r)});
    }
  }
  return dp[l][r];
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<i32>(std::cin);
  auto bars = std::vector<Bar>(n);
  std::copy_n(std::istream_iterator<Bar>(std::cin), n, bars.begin());
  rng::sort(bars, std::less<i64>(), &Bar::x);

  auto dp = std::vector<std::vector<i32>>(n);
  rng::for_each(dp, [n](auto& dp) mutable { dp.resize(n, NEVAL); });
  std::cout << Simulate(CacheJumps(bars), dp, 0, static_cast<i32>(n) - 1)
            << std::endl;
  return EXIT_SUCCESS;
}
