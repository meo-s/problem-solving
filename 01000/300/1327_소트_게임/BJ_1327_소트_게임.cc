// https://www.acmicpc.net/problem/1327

#include <algorithm>
#include <cstdint>
#include <deque>
#include <iostream>
#include <iterator>
#include <string>
#include <unordered_map>

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

  auto const n = Read<std::size_t>(std::cin);
  auto const k = Read<std::size_t>(std::cin);
  auto initial_state = std::string(n + 1, '\0');
  std::copy_n(std::istream_iterator<char>(std::cin), n,
              std::begin(initial_state));
  auto ans = std::string(n + 1, '\0');
  for (auto i = std::size_t(); i < n; ++i) {
    ans[i] = static_cast<char>('1' + i);
  }

  auto visited = std::unordered_map<std::string, std::int32_t>();
  auto middle_states = std::deque<std::string>();
  visited.emplace(initial_state, std::int32_t());
  middle_states.emplace_back(initial_state);
  while (!middle_states.empty() && visited.find(ans) == visited.end()) {
    auto const& state = middle_states.front();
    auto const dist = visited[state];
    auto next_state = std::string(n + 1, '\0');
    for (auto i = std::size_t(); i + k <= n; ++i) {
      for (auto j = std::size_t(); j < n; ++j) {
        if (j < i || i + k <= j) {
          next_state[j] = state[j];
        } else {
          next_state[j] = state[i + (k - 1) - (j - i)];
        }
      }
      if (visited.find(next_state) == visited.end()) {
        visited.emplace(next_state, dist + 1);
        middle_states.emplace_back(next_state);
      }
    }
    middle_states.pop_front();
  }
  auto const& it = visited.find(ans);
  std::cout << (it != visited.end() ? it->second : std::int32_t(-1))
            << std::endl;
  return 0;
}
