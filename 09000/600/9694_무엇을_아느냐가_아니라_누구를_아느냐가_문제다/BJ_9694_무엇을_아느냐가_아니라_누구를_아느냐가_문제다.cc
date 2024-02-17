// https://www.acmicpc.net/problem/9694

#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <limits>
#include <queue>
#include <ranges>
#include <stack>
#include <utility>

#define _R ::std::ranges::

constexpr static auto MAX_M = std::size_t(20);
using i32 = std::int32_t;
using Graph = std::array<std::vector<std::pair<i32, i32>>, MAX_M>;

template <typename T, typename Comp>
class PriorityQueue : public std::priority_queue<T, std::vector<T>, Comp>
{
  public:
    std::vector<T>& GetContainer()
    {
        return this->c;
    }
};

void Dijkstra(i32 const test_case, Graph const& g, i32 const m)
{
    thread_local static auto nexts =
        PriorityQueue<std::pair<i32, i32>, decltype([](auto const& lhs, auto const& rhs) {
                          return lhs.second > rhs.second;
                      })>();

    auto dists = std::array<std::pair<i32, i32>, MAX_M>();
    _R fill_n(_R begin(dists), m, std::make_pair(i32(-1), std::numeric_limits<i32>::max()));

    nexts.GetContainer().clear();
    nexts.emplace(i32(), i32());
    dists[0] = {i32(-1), i32()};
    while (!nexts.empty())
    {
        auto const [u, dist] = nexts.top();
        nexts.pop();
        if (dist == dists[u].second)
        {
            for (auto const& [v, w] : g[u])
            {
                if (dist + w < dists[v].second)
                {
                    dists[v] = {u, dist + w};
                    nexts.emplace(v, dist + w);
                }
            }
        }
    }

    if (dists[m - 1].first == i32(-1))
    {
        std::cout << "Case #" << test_case << ": -1\n";
    }
    else
    {
        thread_local static auto trace = std::stack<i32>();
        trace.emplace(m - 1);
        while (dists[trace.top()].first != i32(-1))
        {
            trace.emplace(dists[trace.top()].first);
        }
        std::cout << "Case #" << test_case << ": ";
        while (!trace.empty())
        {
            std::cout << trace.top() << ' ';
            trace.pop();
        }
        std::cout << '\n';
    }
}

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto g = Graph();
    for (auto const test_case : _R views::iota(i32(), Read<i32>(std::cin)))
    {
        auto const n = Read<i32>(std::cin);
        auto const m = Read<i32>(std::cin);
        _R for_each_n(_R begin(g), m, &Graph::value_type::clear);
        _R for_each(_R views::iota(i32(), n), [&g]([[maybe_unused]] auto) {
            auto const u = Read<i32>(std::cin);
            auto const v = Read<i32>(std::cin);
            auto const w = Read<i32>(std::cin);
            g[u].emplace_back(v, w);
            g[v].emplace_back(u, w);
        });
        Dijkstra(test_case + 1, g, m);
    }

    return EXIT_SUCCESS;
}
