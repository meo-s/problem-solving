#include <cstdint>
#include <iostream>
#include <iterator>
#include <list>
#include <utility>
#include <vector>

using namespace std;

using sz = size_t;
using tree_t = vector<list<pair<sz, int>>>;
using tree_diameter_t = pair<int, int>;

tree_diameter_t diameter_of(tree_t const& tree, sz const start = 1, sz const predessor = 0) {
  auto max_d = INT32_MIN;
  auto top1_r = INT32_MIN, top2_r = INT32_MIN;
  for (auto const& [v, w] : tree[start]) {
    if (v == predessor) continue;

    auto const& [d, r] = diameter_of(tree, v, start);
    max_d = max(max_d, d);
    if (top1_r < r + w) {
      top2_r = top1_r;
      top1_r = r + w;
    } else {
      top2_r = max(top2_r, r + w);
    }
  }

  return {max(max_d, max(max(top1_r + top2_r, 0), top1_r)), max(top1_r, 0)};
}

int main(int argc, char* argv[]) {
  cin.tie(0);
  cout.sync_with_stdio(false);
  auto in = istream_iterator<int>{cin};

  auto const V = sz(*in);
  auto tree = tree_t(V + 1);
  for (sz i = 0; i < V; ++i) {
    auto const u = *(++in);
    do {
      auto const v = *(++in);
      if (v == -1) break;

      auto const w = *(++in);
      tree[u].emplace_back(sz(v), w);
    } while (true);
  }

  auto const [max_d, max_r] = diameter_of(tree);
  cout << max_d << endl;

  return 0;
}
