// https://www.acmicpc.net/problem/14891

#include <array>
#include <cstdint>
#include <iostream>
#include <numeric>
#include <stack>
#include <string>
#include <type_traits>

using sz = std::size_t;
using u8 = std::uint8_t;
using u32 = std::uint32_t;
using i32 = std::int32_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

template <typename T, std::enable_if_t<std::is_unsigned_v<T>>* = nullptr>
[[nodiscard]] constexpr T RotateLeftShift(T const v, sz amount) {
  return (v << (8 * sizeof(T) - amount)) | (v >> amount);
}

template <typename T, std::enable_if_t<std::is_unsigned_v<T>>* = nullptr>
[[nodiscard]] constexpr T RotateRightShift(T const v, sz amount) {
  return (v >> (8 * sizeof(T) - amount)) | (v << amount);
}

[[nodiscard]] constexpr bool IsChained(u8 const left_side_gear,
                                       u8 const right_side_gear) {
  return (((left_side_gear >> 5) ^ (right_side_gear >> 1)) & 1) == 1;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto gears = std::array<u8, 4>{};
  for (auto i = sz(); i < gears.size(); ++i) {
    auto const initial_state =
        std::stoul(Read<std::string>(std::cin), nullptr, 2);
    gears[i] = static_cast<u8>(initial_state);
  }

  auto st = std::stack<sz>();
  for (auto k = Read<sz>(std::cin); 0 < k; --k) {
    auto const target = Read<sz>(std::cin) - 1;
    auto direction = Read<i32>(std::cin);

    st.emplace(target);
    while (0 < st.top() && IsChained(gears[st.top() - 1], gears[st.top()])) {
      st.emplace(st.top() - 1);
      direction *= -1;
    }
    while (1 < st.size()) {
      gears[st.top()] = (direction == 1 ? RotateLeftShift(gears[st.top()], 1)
                                        : RotateRightShift(gears[st.top()], 1));
      direction *= -1;
      st.pop();
    }
    while (st.top() + 1 < gears.size() &&
           IsChained(gears[st.top()], gears[st.top() + 1])) {
      direction *= -1;
      st.emplace(st.top() + 1);
    }
    while (!st.empty()) {
      gears[st.top()] = (direction == 1 ? RotateLeftShift(gears[st.top()], 1)
                                        : RotateRightShift(gears[st.top()], 1));
      direction *= -1;
      st.pop();
    }
  }

  std::cout << [&gears]() {
    auto score = u32();
    for (auto i = sz(); i < gears.size(); ++i) {
      score |= static_cast<u32>(gears[i] >> 7) << i;
    }
    return score;
  }() << std::endl;
  return 0;
}
