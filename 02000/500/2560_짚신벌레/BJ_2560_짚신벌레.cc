// https://www.acmicpc.net/problem/2560

#include <cstdint>
#include <iostream>
#include <vector>

using i32 = std::int32_t;

constexpr auto MODULAR = i32(1000);

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main(int const argc, char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const a = Read<i32>(std::cin);
  auto const b = Read<i32>(std::cin);
  auto const d = Read<i32>(std::cin);
  auto const n = Read<i32>(std::cin);

  auto num_objects = i32(1);
  auto num_reprods = i32(0);
  auto delta_objects = std::vector<i32>(n + 1);
  auto delta_reprods = std::vector<i32>(n + 1);
  delta_objects[d] = -1;
  delta_reprods[a] = 1;
  delta_reprods[b] = -1;
  for (auto day = i32(1); day <= n; ++day) {
    num_objects = (num_objects + delta_objects[day] + MODULAR) % MODULAR;
    num_reprods = (num_reprods + delta_reprods[day] + MODULAR) % MODULAR;
    num_objects = (num_objects + num_reprods) % MODULAR;
    if (day + d <= n) {
      delta_objects[day + d] = -num_reprods;
    }
    if (day + a <= n) {
      delta_reprods[day + a] += num_reprods;
    }
    if (day + b <= n) {
      delta_reprods[day + b] -= num_reprods;
    }
  }

  std::cout << num_objects << std::endl;
  return 0;
}
