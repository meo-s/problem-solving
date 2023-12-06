// https://www.acmicpc.net/problem/5615

#include <algorithm>
#include <array>
#include <cassert>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using u64 = std::uint64_t;

struct MillerRabin {
  constexpr static auto PRIMES = std::array<u64, 3>{2, 7, 61};

  constexpr static u64 ModPow(u64 n, u64 k, u64 const modular) noexcept {
    assert(modular != 0);
    auto nk = u64(1);  // n^k
    n %= modular;
    while (0 < k) {
      if (k % 2 != 0) {
        nk = (nk * n) % modular;
      }
      n = (n * n) % modular;
      k /= 2;
    }
    return nk;
  }

  constexpr static bool Test(u64 n, u64 a) noexcept {
    for (auto m = n - 1;; m /= 2) {
      auto const am = ModPow(a, m, n);  // a^m
      if (am == n - 1) {
        return true;
      }
      if (m % 2 != 0) {
        return am == 1;
      }
    }
  }

  constexpr static bool Test(u64 const n) noexcept {
    return std::ranges::all_of(std::views::all(PRIMES), [&n](auto const prime) {
      return n <= prime || Test(n, prime);
    });
  }
};

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);
  auto areas = std::vector<u64>(Read<sz>(std::cin));
  std::copy_n(std::istream_iterator<u64>(std::cin), areas.size(),
              std::begin(areas));
  std::cout << std::ranges::count_if(
                   std::views::transform(
                       areas, [](auto const area) { return area * 2 + 1; }),
                   static_cast<bool (&)(u64 const)>(MillerRabin::Test))
            << std::endl;
  return 0;
}
