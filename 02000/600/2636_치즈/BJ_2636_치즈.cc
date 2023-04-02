// https://www.acmicpc.net/problem/2636

#include <algorithm>
#include <array>
#include <deque>
#include <iostream>
#include <iterator>
#include <utility>

using namespace std;

constexpr array<int, 4> dy = {1, -1, 0, 0};
constexpr array<int, 4> dx = {0, 0, 1, -1};
array<array<int, 100>, 100> table;
array<array<bool, 100>, 100> visited;

void bfs(int const H, int const W, deque<pair<int, int>>& cheeses, int const y0 = 0, int const x0 = 0) {
    auto outer = deque<pair<int, int>>{{y0, x0}};
    while (0 < outer.size()) {
        auto const [y, x] = outer.front();
        outer.pop_front();
        for (auto i = 0; i < 4; ++i) {
            auto const ny = y + dy[i];
            auto const nx = x + dx[i];
            if (0 <= ny && ny < H && 0 <= nx && nx < W && !visited[ny][nx]) {
                visited[ny][nx] = true;
                (table[ny][nx] == 0 ? outer : cheeses).emplace_back(ny, nx);
            }
        }
    }
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int H, W;
    cin >> H >> W;
    for (auto y = 0; y < H; ++y)
        copy_n(istream_iterator<int>{cin}, W, begin(table[y]));

    auto cheeses = deque<pair<int, int>>{};
    bfs(H, W, cheeses);

    auto ans = pair{0, 0};
    while (0 < cheeses.size()) {
        ans = {get<0>(ans) + 1, static_cast<int>(cheeses.size())};
        for (auto i = 0; i < get<1>(ans); ++i) {
            auto const [y, x] = cheeses.front();
            cheeses.pop_front();
            bfs(H, W, cheeses, y, x);
        }
    }

    cout << get<0>(ans) << '\n' << get<1>(ans) << endl;
    return 0;
}
