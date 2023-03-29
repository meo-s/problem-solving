// https://www.acmicpc.net/problem/1600

#include <cstdint>
#include <iostream>
#include <iterator>
#include <vector>
#include <tuple>
#include <deque>
#include <algorithm>
#include <limits>

using namespace std;
using sz = size_t;
using i32 = int32_t;

constexpr i32 INF = numeric_limits<i32>::max();
constexpr i32 dy[] = {-1, 0, 1, 0, -2, -1, 1, 2, 2, 1, -1, -2};
constexpr i32 dx[] = {0, 1, 0, -1, 1, 2, 2, 1, -1, -2, -2, -1};

template <typename T, sz S>
constexpr sz countof(T(&_)[S]) {
    return S;
}

i32 dists[200][200][31] = {};

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<int>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto K = static_cast<size_t>(*in);
    auto W = static_cast<size_t>(*++in);
    auto H = static_cast<size_t>(*++in);
    auto m = vector<vector<i32>>(H);
    for (auto& mr : m) {
        mr.reserve(W);
        for (i32 x = 0; x < W; ++x) {
            mr.emplace_back(*++in);
        }
    }

    fill((i32*)dists, (i32*)dists + 200 * 200 * 31, INF);
    dists[0][0][0] = 0;

    auto waypoints = deque<tuple<int, int, int>>{};  // 0: y, 1: x, 2, k
    waypoints.emplace_back(0, 0, 0);
    while (0 < waypoints.size()) {
        auto const[y, x, k] = waypoints.front();
        waypoints.pop_front();
        if (y == H - 1 && x == W - 1) {
            break;
        }

        for (int i = 0; i < countof(dy); ++i) {
            if (k == K && 4 <= i) {
                break;
            }

            i32 const ny = y + dy[i];
            i32 const nx = x + dx[i];
            if (ny < 0 || H <= ny || nx < 0 || W <= nx) {
                continue;
            }

            if (m[ny][nx] == 1) {
                continue;
            }

            i32 const nk = k + (4 <= i ? 1 : 0);
            if (dists[y][x][k] + 1 < dists[ny][nx][nk]) {
                dists[ny][nx][nk] = dists[y][x][k] + 1;
                waypoints.emplace_back(ny, nx, nk);
            }
        }
    }

    i32 ans = INF;
    for (i32 i = 0; i <= K; ++i) {
        ans = min(ans, dists[H - 1][W - 1][i]);
    }

    cout << (ans < INF ? ans : -1) << endl; 
    return 0;
}
