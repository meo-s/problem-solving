// https://www.acmicpc.net/problem/2589

#include <algorithm>
#include <array>
#include <deque>
#include <iostream>
#include <iterator>
#include <utility>
#include <vector>

constexpr auto dydx = std::array<std::pair<int, int>, 4>{{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}};

int bfs(std::vector<std::vector<char>> const& map, std::vector<std::vector<int>>& visited, int const h, int const w,
        int const y, int const x, int const mark) {

    auto farthest = 0;
    auto island = std::deque<std::pair<std::pair<int, int>, int>>();
    island.emplace_back(std::make_pair(y, x), 0);
    visited[y][x] = mark;
    while (!island.empty()) {
        auto const [pos, dist] = island.front();
        auto const [y, x] = pos;
        island.pop_front();
        farthest = std::max(farthest, dist);

        for (auto const& [dy, dx] : dydx) {
            auto const ny = y + dy;
            auto const nx = x + dx;
            if (0 <= ny && ny < h && 0 <= nx && nx < w) {
                if (map[ny][nx] == 'L' && visited[ny][nx] != mark) {
                    visited[ny][nx] = mark;
                    island.emplace_back(std::make_pair(ny, nx), dist + 1);
                }
            }
        }
    }

    return farthest;
}

int main(int argc, char const* argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    int h, w;
    std::cin >> h >> w;
    auto map = std::vector<std::vector<char>>(0);
    auto visited = std::vector<std::vector<int>>(0);
    map.reserve(h);
    visited.reserve(h);
    for (int i = 0; i < h; ++i) {
        map.emplace_back(std::size_t(w));
        std::copy_n(std::istream_iterator<char>(std::cin), map[i].size(), std::begin(map[i]));
        visited.emplace_back(std::size_t(w));
    }

    auto ans = 0;
    for (int y = 0; y < h; ++y) {
        for (int x = 0; x < w; ++x) {
            if (map[y][x] == 'L') {
                for (auto const& [dy, dx] : dydx) {
                    auto const ny = y + dy;
                    auto const nx = x + dx;
                    if (ny < 0 || h <= ny || nx < 0 || w <= nx || map[ny][nx] == 'W') {
                        ans = std::max(ans, bfs(map, visited, h, w, y, x, y * h + x));
                        break;
                    }
                }
            }
        }
    }

    std::cout << ans << std::endl;
    return 0;
}
