// https://www.acmicpc.net/problem/25172

#include <algorithm>
#include <cstdint>
#include <deque>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Graph = std::vector<std::vector<i32>>;

class DisjointSet
{
    i32 _disjoint_set_count;
    mutable std::vector<i32> _parents;

  public:
    DisjointSet(i32 const n) : _disjoint_set_count(n), _parents(static_cast<sz>(n))
    {
        rng::copy(rng::views::iota(i32(), n), std::begin(_parents));
    }

    [[nodiscard]] i32 Count() const noexcept
    {
        return _disjoint_set_count;
    }

    [[nodiscard]] i32 Find(i32 const u) const noexcept
    {
        return _parents[u] != u ? _parents[u] = Find(_parents[u]) : u;
    }

    void Merge(i32 const u, i32 const v) noexcept
    {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp)
        {
            _parents[vp] = up;
            --_disjoint_set_count;
        }
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
    auto const m = Read<i32>(std::cin);

    auto g = Graph(static_cast<sz>(n));
    rng::for_each(rng::views::iota(i32(), m), [&](auto const _) mutable {
        auto const u = Read<i32>(std::cin) - 1;
        auto const v = Read<i32>(std::cin) - 1;
        g[u].emplace_back(v);
        g[v].emplace_back(u);
    });

    auto blacklist = std::vector<i32>(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, std::rbegin(blacklist));
    auto visitable = std::vector<bool>(static_cast<sz>(n));
    auto disjoint_set = DisjointSet(n);
    auto tour = [&, visited = std::vector<bool>(static_cast<sz>(n))](auto &&callee, auto const u) mutable -> void {
        visited[u] = true;
        for (auto const v : g[u])
        {
            if (visitable[v])
            {
                disjoint_set.Merge(u, v);
                if (!visited[v])
                {
                    callee(std::forward<decltype(callee)>(callee), v);
                }
            }
        }
    };

    auto ans = std::deque<bool>();
    ans.emplace_back(false);
    rng::transform(rng::views::iota(i32(1), n + 1) | rng::views::reverse, blacklist, std::front_inserter(ans),
                   [&](auto const i, auto city) mutable {
                       --city;
                       visitable[city] = true;
                       if (rng::any_of(g[city], [&visitable](auto const v) { return visitable[v]; }))
                       {
                           tour(tour, city);
                       }
                       return disjoint_set.Count() == i;
                   });
    rng::for_each(ans, [](auto const is_ideal) { std::cout << (is_ideal ? "CONNECT" : "DISCONNECT") << '\n'; });
    return 0;
}
