// https://www.acmicpc.net/problem/13711

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

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

  auto const n = Read<sz>(std::cin);
  auto indices = std::vector<sz>(n + 1);
  for (auto i = sz(1); i <= n; ++i) {
    indices[Read<sz>(std::cin)] = i;
  }

  auto dp = std::vector<sz>(n + 1);
  dp.resize(1);
  for (auto i = sz(); i < n; ++i) {
    auto const ni = Read<sz>(std::cin);
    if (dp.back() < indices[ni]) {
      dp.emplace_back(indices[ni]);
    } else {
      auto it = std::lower_bound(std::begin(dp), --std::end(dp), indices[ni]);
      *it = indices[ni];
    }
  }
  std::cout << dp.size() - 1 << std::endl;
  return 0;
}
