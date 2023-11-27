// https://www.acmicpc.net/problem/2447

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <vector>

using i32 = std::int32_t;
using Canvas = std::vector<std::vector<char>>;

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

Canvas CreateCanvas(i32 const w) {
    auto canvas = Canvas(w);
    for (auto& row : canvas) {
        row.resize(w);
        std::fill(std::begin(row), std::end(row), '*');
    }
    return canvas;
}

void PrintCanvas(Canvas const& canvas) {
    for (auto& row : canvas) {
        std::copy(std::cbegin(row), std::cend(row), std::ostream_iterator<char>(std::cout));
        std::cout << '\n';
    }
}

void Draw(Canvas& out, i32 const y0, i32 const x0, i32 const w) {
    auto const w3 = w / 3;
    if (1 < w3) {
        for (auto i = i32(); i < 3; ++i) {
            for (auto j = i32(); j < 3; ++j) {
                if (i != 1 || j != 1) {
                    Draw(out, y0 + i * w3, x0 + j * w3, w3);
                }
            }
        }
    }
    for (auto dy = 0; dy < w3; ++dy) {
        std::fill_n(std::begin(out[y0 + w3 + dy]) + x0 + w3, w3, ' ');
    }
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto canvas = CreateCanvas(n);
    Draw(canvas, 0, 0, n);
    PrintCanvas(canvas);
    std::cout.flush();
    return 0;
}
