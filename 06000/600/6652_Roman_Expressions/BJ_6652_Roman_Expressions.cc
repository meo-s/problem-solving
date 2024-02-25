// https://www.acmicpc.net/problem/6652

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <string>
#include <string_view>
#include <unordered_map>

#define _R ::std::ranges::

using i64 = std::int64_t;

template <char CH, char... OTHERS>
bool OneOf(char const ch)
{
    if (ch == CH)
    {
        return true;
    }
    if constexpr (0 < sizeof...(OTHERS))
    {
        return OneOf<OTHERS...>(ch);
    }
    else
    {
        return false;
    }
}

i64 rtoi(std::string_view const r) noexcept
{
    auto val = i64();
    for (auto it = _R cbegin(r); it != _R cend(r); _R advance(it, 1))
    {
        auto const next_it = _R next(it);
        switch (*it)
        {
        case 'I':
            if (next_it != _R cend(r) && OneOf<'V', 'X'>(*next_it))
            {
                val += (*next_it == 'V' ? 4 : 9);
                _R advance(it, 1);
            }
            else
            {
                val += 1;
            }
            break;
        case 'V':
            val += 5;
            break;
        case 'X':
            if (next_it != _R cend(r) && OneOf<'L', 'C'>(*next_it))
            {
                val += (*next_it == 'L' ? 40 : 90);
                _R advance(it, 1);
            }
            else
            {
                val += 10;
            }
            break;
        case 'L':
            val += 50;
            break;
        case 'C':
            if (next_it != _R cend(r) && OneOf<'D', 'M'>(*next_it))
            {
                val += (*next_it == 'D' ? 400 : 900);
                _R advance(it, 1);
            }
            else
            {
                val += 100;
            }
            break;
        case 'D':
            val += 500;
            break;
        case 'M':
            val += 1000;
            break;
        }
    }
    return val;
}

std::string itor(i64 const n)
{
    if (n == 0)
    {
        return "O";
    }

    auto val = std::string();
    if (0 < n / 1000)
    {
        val.assign(n / 1000, 'M');
    }
    if (auto count = (n / 100) % 10; 0 < count)
    {
        switch (count)
        {
        case 9:
            val += "CM";
            break;
        case 4:
            val += "CD";
            break;
        default:
            if (5 <= count)
            {
                val.push_back('D');
                count -= 5;
            }
            _R fill_n(std::back_inserter(val), count, 'C');
        }
    }
    if (auto count = (n / 10) % 10; 0 < count)
    {
        switch (count)
        {
        case 10:
            val += 'X';
            break;
        case 9:
            val += "XC";
            break;
        case 4:
            val += "XL";
            break;
        default:
            if (5 <= count)
            {
                val.push_back('L');
                count -= 5;
            }
            _R fill_n(std::back_inserter(val), count, 'X');
        }
    }
    if (auto count = n % 10; 0 < count)
    {
        switch (count)
        {
        case 9:
            val += "IX";
            break;
        case 4:
            val += "IV";
            break;
        default:
            if (5 <= count)
            {
                val.push_back('V');
                count -= 5;
            }
            _R fill_n(std::back_inserter(val), count, 'I');
        }
    }
    return val;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto line = std::string(10001, '\0');
    auto regs = std::unordered_map<std::string, i64>();
    for (;;)
    {
        std::getline(std::cin, line);
        if (line == "QUIT")
        {
            std::cout << "Bye" << std::endl;
            return EXIT_SUCCESS;
        }

        auto it = _R find(line, '=');
        if (it == _R cend(line)) // RESET
        {
            regs.clear();
            std::cout << "Ready\n";
            continue;
        }

        auto const reg = std::string_view{_R cbegin(line), it};
        auto panic = false;
        auto calc = [&, val = i64(0)](char const op, i64 const rhs) mutable {
            switch (op)
            {
            case '+':
                return (val += rhs);
            case '-':
                return (val -= rhs);
            case '=':
                return (val = rhs);
            default:
                panic = true;
                return val;
            }
        };

        while (it != _R cend(line) && !panic)
        {
            auto const next = _R find_if(_R next(it, 2), _R cend(line), OneOf<'+', '-'>);
            auto const term = std::string_view{_R next(it), next};
            if (std::isdigit(term[0]) == 0)
            {
                (void)calc(*it, rtoi(term));
            }
            else
            {
                if (auto const reg = regs.find(std::string(term)); reg != _R end(regs))
                {
                    (void)calc(*it, reg->second);
                }
                else
                {
                    panic = true;
                }
            }
            it = next;
        }

        auto const val = calc('+', 0);
        if (panic || (val < 0 || 10000 < val))
        {
            std::cout << "Error\n";
        }
        else
        {
            regs[std::string(reg)] = val;
            std::cout << reg << '=' << itor(val) << '\n';
        }
    }
}
