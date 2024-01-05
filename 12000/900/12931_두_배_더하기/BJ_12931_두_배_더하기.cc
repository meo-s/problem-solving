// https://www.acmicpc.net/problem/12931

#include <algorithm>
#include <cstdint>
#include <iostream>

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

  auto num_op1 = i32();
  auto max_op2 = i32();
  for (auto n = Read<sz>(std::cin); 0 < n; --n) {
    auto b = Read<i32>(std::cin);
    auto num_op2 = i32();
    while (0 < b) {
      if (b % 2 == 0) {
        b /= 2;
        ++num_op2;
      } else {
        b -= 1;
        ++num_op1;
      }
    }
    max_op2 = std::max(max_op2, num_op2);
  }
  std::cout << num_op1 + max_op2 << std::endl;
  return EXIT_SUCCESS;
}
