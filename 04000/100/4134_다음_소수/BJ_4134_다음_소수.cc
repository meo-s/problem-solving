// https://www.acmicpc.net/problem/4134

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <ranges>

using i32 = std::int32_t;
using u64 = std::uint64_t;

struct MillerRabin {
  constexpr static auto PRIMES = std::array{2ULL, 7ULL, 61ULL};

  constexpr static u64 ModPow(u64 n, u64 k, u64 const modular) noexcept {
    auto nk = u64(1);
    while (0 < k) {
      if (k % 2 != 0) {
        nk = (nk * n) % modular;
      }
      n = (n * n) % modular;
      k /= 2;
    }
    return nk;
  }

  constexpr static bool Test(u64 n, u64 const a) noexcept {
    if (n == 0) {
      return false;
    }
    for (auto m = n - 1;; m /= 2) {
      auto const am = ModPow(a, m, n);
      if (am + 1 == n) {
        return true;
      }
      if (m % 2 != 0) {
        return am == 1;
      }
    }
  }

  constexpr static bool Test(u64 const n) noexcept {
    return std::ranges::all_of(std::views::all(PRIMES), [&](auto const prime) {
      return n <= prime || Test(n, prime);
    });
  }
};

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  std::cin >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  for (auto t = Read<i32>(std::cin); 0 < t; --t) {
    auto const n = std::max(Read<u64>(std::cin), u64(1));
    switch (n) {
      case u64(2):
      case u64(3):
        std::cout << n << '\n';
        break;
      case u64(1):
      case u64(4):
        std::cout << n + 1 << '\n';
        break;
      default:
        for (auto i = n / 6;; ++i) {
          if (n <= 6 * i - 1 && MillerRabin::Test(6 * i - 1)) {
            std::cout << 6 * i - 1 << '\n';
            break;
          }
          if (n <= 6 * i + 1 && MillerRabin::Test(6 * i + 1)) {
            std::cout << 6 * i + 1 << '\n';
            break;
          }
        }
    }
  }
  std::cout.flush();
  return 0;
}
