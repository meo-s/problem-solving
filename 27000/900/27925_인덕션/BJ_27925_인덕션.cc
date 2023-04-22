// https://www.acmicpc.net/problem/27925

#include <algorithm>
#include <cmath>
#include <cstring>
#include <iostream>
#include <utility>

using namespace std;

int dp[5001][10][10][10] = {};

int dist_of(int a, int b) { return min(abs(a - b) % 10, 10 - abs(a - b) % 10); }

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    memset(dp, 50000, sizeof(dp));
    dp[0][0][0][0] = 0;

    int N;
    cin >> N;
    for (int i = 0; i < N; ++i) {
        int T;
        cin >> T;
        for (int a = 0; a < 10; ++a) {
            for (int b = 0; b < 10; ++b) {
                for (int c = 0; c < 10; ++c) {
                    dp[i + 1][T][b][c] = min(dp[i + 1][T][b][c], dp[i][a][b][c] + dist_of(T, a));
                    dp[i + 1][a][T][c] = min(dp[i + 1][a][T][c], dp[i][a][b][c] + dist_of(T, b));
                    dp[i + 1][a][b][T] = min(dp[i + 1][a][b][T], dp[i][a][b][c] + dist_of(T, c));
                }
            }
        }
    }

    int ans = 50000;
    for (int a = 0; a < 10; ++a) {
        for (int b = 0; b < 10; ++b) {
            for (int c = 0; c < 10; ++c) {
                ans = min(ans, dp[N][a][b][c]);
            }
        }
    }

    cout << ans << endl;
    return 0;
}
