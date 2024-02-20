// https://www.acmicpc.net/problem/4387

#include <algorithm>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <queue>
#include <ranges>
#include <string>
#include <string_view>
#include <unordered_map>
#include <unordered_set>
#include <utility>

using sz = std::size_t;
using StringPair = std::pair<std::reference_wrapper<std::string const>, sz>;
using StringPairCmp = decltype([](auto const& lhs, auto const rhs) {
    auto const lhs_remains = lhs.first.get().length() - lhs.second;
    auto const rhs_remains = rhs.first.get().length() - rhs.second;
    return lhs_remains != rhs_remains ? rhs_remains < lhs_remains
                                      : rhs.first.get().length() < lhs.first.get().length();
});

#define _R ::std::ranges::
constexpr static auto HISTORY_LIMIT = sz(1001);

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

    auto const s = Read<std::string>(std::cin);
    auto rules = std::unordered_multimap<std::string, std::string>();
    for (;;)
    {
        auto const rule = Read<std::string>(std::cin);
        if (std::cin.eof())
        {
            break;
        }

        auto const it = _R find(rule, '>');
        auto const s1 = std::string_view(_R next(_R cbegin(rule)), _R prev(it, 2));
        auto const s2 = std::string_view(_R next(it, 2), _R prev(_R cend(rule)));
        rules.emplace(s1, s2);
    }

    auto buf = std::string(10011, '\0');
    auto history = std::unordered_set<std::string>();
    auto string_pool = std::priority_queue<StringPair, std::vector<StringPair>, StringPairCmp>();
    auto const& initial_str = *history.emplace(s.substr(1, s.size() - 2)).first;
    string_pool.emplace(std::cref(initial_str), sz());
    while (!string_pool.empty())
    {
        auto const& src = static_cast<std::string const&>(string_pool.top().first);
        auto const offset = string_pool.top().second;
        string_pool.pop();
        for (auto const i : _R views::iota(offset, src.length()) | _R views::reverse)
        {
            for (auto count : _R views::iota(sz(), std::min<sz>(src.length() - i, 10)))
            {
                auto const key = src.substr(i, ++count);
                if (auto it = rules.find(key); it != _R end(rules))
                {
                    _R copy_n(_R cbegin(src), i, _R begin(buf));
                    for (; it != _R end(rules) && it->first == key; ++it)
                    {
                        buf.resize(i);
                        _R copy(it->second, std::back_inserter(buf));
                        _R copy(_R next(_R cbegin(src), i + count), _R cend(src),
                                std::back_inserter(buf));

                        auto const [entry, inserted] = history.insert(buf);
                        if (!inserted)
                        {
                            continue;
                        }
                        if (history.size() == HISTORY_LIMIT)
                        {
                            std::cout << "Too many." << std::endl;
                            std::exit(EXIT_SUCCESS);
                        }
                        string_pool.emplace(std::cref(*entry), 9 <= i ? i - 9 : sz());
                    }
                }
            }
        }
    }

    std::cout << history.size() << std::endl;
    std::endl(std::cout);
    return EXIT_SUCCESS;
}
