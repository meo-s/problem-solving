// https://www.acmicpc.net/problem/1214

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <ranges>

namespace rng = std::ranges;

using i64 = std::int64_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

[[nodiscard]] constexpr i64 MultipleOf(i64 const n, i64 const min) noexcept {
  return min + (n - min % n) % n;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const d = Read<i64>(std::cin);
  auto const [p, q] = std::minmax({Read<i64>(std::cin), Read<i64>(std::cin)});
  auto ans = MultipleOf(q, d);
  for (auto const i : rng::views::iota(i64(), std::min(p, d / q) + 1)) {
    ans = std::min(ans, q * i + MultipleOf(p, d - q * i));
  }
  std::cout << ans << std::endl;
  return EXIT_SUCCESS;
}
