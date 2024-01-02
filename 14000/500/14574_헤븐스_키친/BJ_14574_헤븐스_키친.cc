// https://www.acmicpc.net/problem/14574

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <queue>
#include <tuple>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;
using Graph = std::vector<std::vector<i32>>;

struct Chef {
  i64 p;
  i64 c;
};

std::istream& operator>>(std::istream& is, Chef& chef) {
  return std::cin >> chef.p >> chef.c;
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

[[nodiscard]] std::pair<Graph, i64> Prim(std::vector<Chef> const& chefs) {
  auto is_idle = std::vector<bool>(chefs.size(), true);
  auto battles =
      std::priority_queue<std::tuple<i32, i32, i64>,
                          std::vector<std::tuple<i32, i32, i64>>,
                          decltype([](auto const& lhs, auto const& rhs) {
                            return std::get<2>(lhs) < std::get<2>(rhs);
                          })>();

  auto update_battles = [&](i32 const u) mutable {
    for (auto v = i32(); v < static_cast<i32>(chefs.size()); ++v) {
      if (is_idle[v]) {
        auto const &cu = chefs[u], &cv = chefs[v];
        battles.emplace(u, v, (cu.c + cv.c) / std::abs(cu.p - cv.p));
      }
    }
  };

  auto g = Graph(chefs.size());
  auto total_score = i64();
  is_idle[0] = false;
  update_battles(0);
  while (!battles.empty()) {
    auto const [u, v, score] = battles.top();
    battles.pop();
    if (is_idle[v]) {
      g[u].emplace_back(v);
      g[v].emplace_back(u);
      total_score += score;
      is_idle[v] = false;
      update_battles(v);
    }
  }
  return {std::move(g), total_score};
}

template <typename Callable>
void PostorderTraversal(Graph const& g, i32 const u, Callable&& callback,
                        i32 const p = -1) {
  for (auto const v : g[u]) {
    if (v != p) {
      PostorderTraversal(g, v, callback, u);
    }
  }
  callback(u, p);
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto chefs = std::vector<Chef>(Read<sz>(std::cin));
  std::copy_n(std::istream_iterator<Chef>(std::cin), chefs.size(),
              std::begin(chefs));

  auto const&& [g, total_score] = Prim(chefs);
  std::cout << total_score << '\n';
  PostorderTraversal(g, 0, [](auto const u, auto const p) {
    if (p != -1) {
      std::cout << p + 1 << ' ' << u + 1 << '\n';
    }
  });
  std::cout.flush();
  return 0;
}
