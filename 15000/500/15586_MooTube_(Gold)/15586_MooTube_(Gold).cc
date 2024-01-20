// https://www.acmicpc.net/problem/15586

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <map>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Query = std::tuple<i32, i32, i32>;
using Graph = std::vector<std::vector<i32>>;

class DisjointSet
{
    mutable std::vector<i32> _parents;
    std::vector<i32> _sizes;

  public:
    DisjointSet(i32 const n) : _parents(static_cast<sz>(n)), _sizes(static_cast<sz>(n), i32(1))
    {
        rng::copy(rng::views::iota(i32(), n), std::begin(_parents));
    }

    void Merge(i32 const u, i32 const v) noexcept
    {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp)
        {
            _parents[vp] = up;
            _sizes[up] += std::exchange(_sizes[vp], i32());
        }
    }

    [[nodiscard]] i32 Find(i32 const u) const noexcept
    {
        return _parents[u] != u ? _parents[u] = Find(_parents[u]) : u;
    }

    [[nodiscard]] i32 SizeOf(i32 const u) const noexcept
    {
        return _sizes[Find(u)];
    }
};

template <typename T>
T Read(std::istream &is)
{
    auto v = T();
    is >> v;
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const *const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto const q = Read<i32>(std::cin);
    auto const edges = [&]() {
        auto ret = std::multimap<i32, std::pair<i32, i32>>();
        rng::for_each(rng::views::iota(i32(), n - 1), [&ret](auto const _) mutable {
            auto const u = Read<i32>(std::cin) - 1;
            auto const v = Read<i32>(std::cin) - 1;
            ret.emplace(Read<i32>(std::cin), std::pair(u, v));
        });
        return ret;
    }();
    auto const queries = [&]() {
        auto ret = std::vector<Query>(static_cast<sz>(q));
        rng::for_each(rng::views::iota(i32(), q), [&ret](auto const i) mutable {
            std::get<0>(ret[i]) = i;
            std::get<1>(ret[i]) = Read<i32>(std::cin);     // k
            std::get<2>(ret[i]) = Read<i32>(std::cin) - 1; // v
        });
        rng::sort(ret, [](auto const &lhs, auto const &rhs) { return std::get<1>(rhs) < std::get<1>(lhs); });
        return ret;
    }();

    auto ans = std::vector<i32>(static_cast<sz>(q));
    rng::for_each(queries,
                  [&ans, &edges, e = edges.crbegin(), disjoint_set = DisjointSet(n)](auto const &query) mutable {
                      auto const [i, k, v] = query;
                      while (e != edges.crend() && k <= e->first)
                      {
                          auto const [u, v] = e->second;
                          disjoint_set.Merge(u, v);
                          ++e;
                      }
                      ans[i] = disjoint_set.SizeOf(v) - 1;
                  });
    rng::copy(ans, std::ostream_iterator<i32>(std::cout, "\n"));
    return 0;
}
