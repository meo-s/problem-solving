// https://www.acmicpc.net/problem/2504

#include <cstdint>
#include <iostream>
#include <stack>
#include <string>

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

i32 Skim(std::stack<i32>& st) {
  auto value = i32();
  while (!st.empty() && i32(0) <= st.top()) {
    value += st.top();
    st.pop();
  }
  return value;
}

[[nodiscard]] constexpr i32 GetOppositeBracket(char const ch) noexcept {
  switch (ch) {
    case '(':
      return -2;
    case ')':
      return -1;
    case '[':
      return -4;
    case ']':
      return -3;
    default:
      std::terminate();
  }
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const expression = Read<std::string>(std::cin);
  auto st = std::stack<i32>();
  for (auto i = sz(); i < expression.length(); ++i) {
    switch (expression[i]) {
      case '(':
        st.emplace(i32(-1));
        break;
      case '[':
        st.emplace(i32(-3));
        break;
      case ')':
      case ']':
        if (!st.empty() && st.top() == GetOppositeBracket(expression[i])) {
          st.pop();
          st.emplace(i32(expression[i] == ')' ? 2 : 3));
        } else {
          auto const value = Skim(st);
          if (st.empty() || st.top() != GetOppositeBracket(expression[i])) {
            std::cout << 0 << std::endl;
            std::exit(0);
          }
          st.pop();
          st.emplace(value * i32(expression[i] == ')' ? 2 : 3));
        }
        break;
    }
  }

  auto const ans = Skim(st);
  std::cout << (st.empty() ? ans : i32(0)) << std::endl;
  return 0;
}
