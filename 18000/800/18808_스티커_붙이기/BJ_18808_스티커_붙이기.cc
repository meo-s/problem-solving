// https://www.acmicpc.net/problem

#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <ranges>

namespace rng = std::ranges;

using i32 = std::int32_t;
using sz = std::size_t;

constexpr auto BOARD_ROWS = static_cast<i32>(100);
constexpr auto BOARD_COLS = static_cast<i32>(100);

constexpr auto PAPER_ROWS = static_cast<i32>(10);
constexpr auto PAPER_COLS = static_cast<i32>(10);

using Board = std::array<i32, static_cast<sz>(BOARD_ROWS* BOARD_COLS)>;
using Paper = std::array<i32, static_cast<sz>(PAPER_ROWS* PAPER_COLS)>;

template <typename Container>
[[nodiscard]] decltype(auto) At(Container&& c, i32 const cols, i32 const y, i32 const x) noexcept
{
    return c[y * cols + x];
}

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

std::pair<i32, i32> Read(std::istream& is, Paper& paper)
{
    auto const h = Read<i32>(std::cin);
    auto const w = Read<i32>(std::cin);
    for (auto y = i32(); y < h; ++y)
    {
        std::copy_n(std::istream_iterator<i32>(std::cin), w, &At(paper, PAPER_COLS, y, i32()));
    }
    return {h, w};
}

bool Fit(Board const& board, Paper const& paper, i32 ph, i32 pw, i32 offset_y, i32 offset_x)
{
    for (auto y = i32(); y < ph; ++y)
    {
        for (auto x = i32(); x < pw; ++x)
        {
            if (At(paper, PAPER_COLS, y, x) == i32())
            {
                continue;
            }
            if (At(board, BOARD_COLS, offset_y + y, offset_x + x) != i32())
            {
                return false;
            }
        }
    }
    return true;
}

void Attach(Board& board, Paper const& paper, i32 ph, i32 pw, i32 offset_y, i32 offset_x)
{
    for (auto y = i32(); y < ph; ++y)
    {
        for (auto x = i32(); x < pw; ++x)
        {
            if (At(paper, PAPER_COLS, y, x) != i32())
            {
                At(board, BOARD_COLS, offset_y + y, offset_x + x) = i32(1);
            }
        }
    }
}

void Rotate(Paper& paper, i32& h, i32& w)
{
    thread_local static auto buf = Paper();
    for (auto y = i32(); y < h; ++y)
    {
        std::copy_n(&At(paper, PAPER_COLS, y, i32()), w, &At(buf, PAPER_COLS, y, i32()));
    }
    std::swap(h, w);
    for (auto y = i32(); y < h; ++y)
    {
        for (auto x = i32(); x < w; ++x)
        {
            At(paper, PAPER_COLS, y, x) = At(buf, PAPER_COLS, (w - i32(1)) - x, y);
        }
    }
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const bh = Read<i32>(std::cin);
    auto const bw = Read<i32>(std::cin);
    auto board = Board();
    auto paper = Paper();
    for (auto i = Read<i32>(std::cin); 0 < i; --i)
    {
        auto [ph, pw] = Read(std::cin, paper);
        (void)rng::any_of(rng::views::iota(i32(), i32(4)), [&]([[maybe_unused]] auto const _) {
            for (auto offset_y = i32(); offset_y <= bh - ph; ++offset_y)
            {
                for (auto offset_x = i32(); offset_x <= bw - pw; ++offset_x)
                {
                    if (Fit(board, paper, ph, pw, offset_y, offset_x))
                    {
                        Attach(board, paper, ph, pw, offset_y, offset_x);
                        return true;
                    }
                }
            }
            Rotate(paper, ph, pw);
            return false;
        });
    }

    auto const ans = rng::count_if(board, [](auto const v) { return v != i32(); });
    std::cout << ans << std::endl;
    return EXIT_SUCCESS;
}