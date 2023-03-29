// https://www.acmicpc.net/problem/170170

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <tuple>
#include <vector>
#include <array>
#include <numeric>

using namespace std;
using sz = size_t;
using i32 = int32_t;

constexpr int WALL = 1;
constexpr int dy[] = {0, -1, -1};
constexpr int dx[] = {-1, -1, 0};

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<int>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto const N = static_cast<sz>(*in);
    auto m = vector<vector<int>>(N);
    for (auto& mr : m) {
        mr.reserve(N);
        for (i32 i = 0; i < N; ++i) {
            mr.emplace_back(*++in);
        }
    }

    auto dp = vector <vector<array<i32, 3>>>(N);  // 0: 0', 1: 45', 2: 90'
    for (auto& dpr : dp) {
        dpr.resize(N);
    }

    ++dp[0][1][0];
    for (i32 y = 0; y < N; ++y) {
        for (i32 x = 0; x < N; ++x) {
            if (m[y][x] != WALL)  {
                for (i32 cd = 0; cd < 3; ++cd) {
                    if (cd == 1) {
                        if (0 <= y - 1 && m[y - 1][x] == WALL) continue;
                        if (0 <= x - 1 && m[y][x - 1] == WALL) continue;
                    }

                    auto py = y + dy[cd];
                    auto px = x + dx[cd];
                    if (0 <= py && 0 <= px) {
                        for (i32 pd = max(cd - 1, 0); pd <= min(cd + 1, 2); ++pd) {
                            dp[y][x][cd] += dp[py][px][pd];
                        }
                    }
                }
            }
        }
    }

    cout << accumulate(dp[N - 1][N - 1].begin(), dp[N - 1][N - 1].end(), 0) << endl;
    return 0;
}
