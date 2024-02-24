// https://www.acmicpc.net/problem/3409

#include <algorithm>
#include <bitset>
#include <concepts>
#include <cstdint>
#include <functional>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <unordered_map>
#include <variant>
#include <vector>

#define _R ::std::ranges::

using i32 = std::int32_t;
template <typename T>
using CRef = std::reference_wrapper<T const>;
using Graph = std::vector<std::variant<std::nullptr_t, std::pair<i32, i32>, CRef<std::string>>>;
using GraphSummary = std::vector<std::variant<std::nullptr_t, std::bitset<'z' - 'a' + 1>>>;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

std::string& Read(std::istream& is, std::string& v)
{
    std::getline(is, v);
    return v;
}

struct Branch_t
{
};

struct Leaf_t
{
};

[[nodiscard]] bool IsLeafNode(Graph::value_type const& node) noexcept
{
    return std::holds_alternative<CRef<std::string>>(node);
}

template <std::forward_iterator Iter>
[[nodiscard]] Iter Match(Iter it, Iter const end, Graph const& g, GraphSummary const& summary,
                         i32 const u) noexcept;

template <std::forward_iterator Iter>
[[nodiscard]] Iter Match(Iter it, Iter const end, Graph const& g, GraphSummary const& summary,
                         i32 const u, Leaf_t) noexcept
{
    (void)_R all_of(std::get<CRef<std::string>>(g[u]).get(), [&it, end](auto const ch) {
        if (*it == ch)
        {
            ++it;
        }
        return it != end;
    });
    return it;
}

template <std::forward_iterator Iter>
[[nodiscard]] Iter Match(Iter it, Iter const end, Graph const& g, GraphSummary const& summary,
                         i32 const u, Branch_t) noexcept
{
    auto const& children = std::get<std::pair<i32, i32>>(g[u]);
    if (std::get<1>(summary[children.first]).test(*it - 'a'))
    {
        it = Match(std::forward<Iter>(it), end, g, summary, children.first);
    }
    if (it != end && std::get<1>(summary[children.second]).test(*it - 'a'))
    {
        it = Match(std::forward<Iter>(it), end, g, summary, children.second);
    }
    return it;
}

template <std::forward_iterator Iter>
[[nodiscard]] Iter Match(Iter it, Iter const end, Graph const& g, GraphSummary const& summary,
                         i32 const u) noexcept
{
    return IsLeafNode(g[u]) ? Match(it, end, g, summary, u, Leaf_t{})
                            : Match(it, end, g, summary, u, Branch_t{});
}

void Summarize(Graph const& g, GraphSummary& summary, i32 const u) noexcept;

void Summarize(Graph const& g, GraphSummary& summary, i32 const u, Leaf_t) noexcept
{
    auto& flags = std::get<1>(summary[u]);
    _R for_each(std::get<CRef<std::string>>(g[u]).get(),
                [&](auto const alpha) { flags.set(alpha - 'a'); });
}

void Summarize(Graph const& g, GraphSummary& summary, i32 const u, Branch_t) noexcept
{
    auto const& children = std::get<std::pair<i32, i32>>(g[u]);
    Summarize(g, summary, children.first);
    Summarize(g, summary, children.second);
    std::get<1>(summary[u]) =
        std::get<1>(summary[children.first]) | std::get<1>(summary[children.second]);
}

void Summarize(Graph const& g, GraphSummary& summary, i32 const u) noexcept
{
    if (std::holds_alternative<std::nullptr_t>(summary[u]))
    {
        summary[u] = std::bitset<'z' - 'a' + 1>();
        if (!std::holds_alternative<std::nullptr_t>(g[u]))
        {
            return IsLeafNode(g[u]) ? Summarize(g, summary, u, Leaf_t{})
                                    : Summarize(g, summary, u, Branch_t{});
        }
    }
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto g = Graph(500);
    auto g_summary = GraphSummary();
    auto line = std::string();
    _R for_each(_R views::iota(i32(0), Read<i32>(std::cin)), [&](auto) {
        g.clear();
        g_summary.clear();
        auto s2i = std::unordered_map<std::string, i32>();

        auto to_index = [&]<typename T>(T&& s) {
            auto const [node, created] =
                s2i.try_emplace(std::forward<T>(s), static_cast<i32>(s2i.size()));
            if (created)
            {
                g.emplace_back();
            }
            return node;
        };

        auto const n = Read<i32>(std::cin);
        (void)std::cin.get(); // \n
        _R for_each(_R views::iota(i32(0), n), [&](auto) {
            (void)Read(std::cin, line);
            auto const it0 = line.find('=');
            auto const it1 = line.find('+', it0 + 3);
            if (it1 == std::string::npos)
            { // A = ...
                auto const a = to_index(line.substr(0, it0 - 1));
                auto const v = to_index(line.substr(it0 + 2));
                g[a->second] = std::cref(v->first);
            }
            else
            { // A = B + C
                auto const a = to_index(line.substr(0, it0 - 1));
                auto const b = to_index(line.substr(it0 + 2, it1 - (it0 + 3)));
                auto const c = to_index(line.substr(it1 + 2));
                g[a->second] = std::pair{b->second, c->second};
            }
        });

        auto const t = to_index(Read<std::string>(std::cin))->second;
        g_summary.resize(g.size());
        Summarize(g, g_summary, t);

        auto const p = Read<std::string>(std::cin);
        std::cout << (Match(_R cbegin(p), _R cend(p), g, g_summary, t) == _R cend(p) ? "YES" : "NO")
                  << '\n';
    });
}
