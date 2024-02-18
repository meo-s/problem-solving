// https://www.acmicpc.net/problem/6482

#include <algorithm>
#include <array>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <tuple>
#include <utility>
#include <vector>

#define _R ::std::ranges::

bool IsBlank(char const ch) noexcept
{
    return ch == ' ';
}

bool IsAlpha(char const ch) noexcept
{
    return std::isalpha(ch) != 0;
}

bool IsOperator(char const ch) noexcept
{
    return ch == '+' || ch == '-';
}

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

template <>
std::string Read<std::string>(std::istream& is)
{
    auto v = std::string();
    std::getline(is, v);
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    for (;;)
    {
        auto const e = Read<std::string>(std::cin);
        if (e.length() == 0 || std::cin.eof())
        {
            std::exit(EXIT_SUCCESS);
        }

        auto ans = 0;
        auto vars = std::vector<std::tuple<char, int, int>>();
        auto has_unary_minus = false;
        auto beg = _R cbegin(e);
        auto const end = _R find_if_not(_R crbegin(e), _R crend(e), IsBlank).base();
        do
        {
            beg = _R find_if_not(beg, end, IsBlank);
            auto it = _R find_if(beg, end, IsAlpha);
            vars.emplace_back(*it, *it - 'a' + 1, *it - 'a' + 1);
            if (beg != it)
            {
                std::get<1>(vars.back()) += (*beg == '+' ? 1 : -1);
                std::get<2>(vars.back()) += (*beg == '+' ? 1 : -1);
            }
            else
            {
                // it_...xy
                auto const x = _R find_if(_R next(it), end, IsOperator);
                auto const y = _R next(x, 1, end);
                if (x != y && y != end && IsOperator(*x) && *x == *y)
                {
                    std::get<1>(vars.back()) += (*x == '+' ? 1 : -1);
                    it = y;
                }
            }

            ans += (has_unary_minus ? -1 : 1) * std::get<2>(vars.back());
            beg = _R find_if(_R next(it), end, IsOperator);
            if (beg != end)
            {
                has_unary_minus = (*beg == '-');
                _R advance(beg, 1);
            }
        } while (beg != end);

        std::ranges::sort(vars);
        std::cout << "Expression: " << e << '\n';
        std::cout << "    value = " << ans << '\n';
        _R for_each(vars, [](auto const var) {
            std::cout << "    " << std::get<0>(var) << " = " << std::get<1>(var) << '\n';
        });
    }
}
