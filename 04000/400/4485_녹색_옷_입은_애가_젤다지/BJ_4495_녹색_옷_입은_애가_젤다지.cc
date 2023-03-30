// https://www.acmicpc.net/problem/4495

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <limits>
#include <queue>

using namespace std;
using sz = size_t;
using i32 = int32_t;

constexpr i32 INF = numeric_limits<i32>::max();
constexpr i32 dy[] = {1, -1, 0, 0};
constexpr i32 dx[] = {0, 0, 1, -1};

struct Comparator {
    template <typename... TArgs> bool operator()(tuple<TArgs...> const& lhs, tuple<TArgs...> const& rhs) {
        return get<0>(rhs) < get<0>(lhs);
    }
};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto edges = array<array<i32, 125>, 125>{};
    auto costs = array<array<i32, 125>, 125>{};

    for (i32 t = 1;; ++t) {
        i32 N;
        cin >> N;
        if (N == 0) break;

        for (i32 y = 0; y < N; ++y) {
            fill(costs[y].begin(), costs[y].end(), INF);
            for (i32 x = 0; x < N; ++x) {
                cin >> edges[y][x];
            }
        }

        costs[0][0] = edges[0][0];

        auto waypoints = priority_queue<tuple<i32, i32, i32>, vector<tuple<i32, i32, i32>>, Comparator>{};
        waypoints.emplace(costs[0][0], 0, 0);
        while (!waypoints.empty()) {
            auto const [cost, y, x] = waypoints.top();
            waypoints.pop();

            if (costs[y][x] != cost) continue;

            for (i32 i = 0; i < 4; ++i) {
                auto const ny = y + dy[i];
                auto const nx = x + dx[i];
                if (0 <= ny && ny < N && 0 <= nx && nx < N) {
                    if (cost + edges[ny][nx] < costs[ny][nx]) {
                        costs[ny][nx] = cost + edges[ny][nx];
                        waypoints.emplace(costs[ny][nx], ny, nx);
                    }
                }
            }
        }

        cout << "Problem " << t << ": " << costs[N - 1][N - 1] << '\n';
    }

    cout.flush();
    return 0;
}
