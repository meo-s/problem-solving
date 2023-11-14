// https://www.acmicpc.net/problem/2143

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <unordered_map>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const t = Read<i32>(std::cin);
    auto a = std::vector<i32>(Read<sz>(std::cin));
    std::copy_n(std::istream_iterator<i32>(std::cin), a.size(), std::begin(a));
    auto b = std::vector<i32>(Read<sz>(std::cin));
    std::copy_n(std::istream_iterator<i32>(std::cin), b.size(), std::begin(b));

    auto b_sum = std::unordered_map<i32, i32>();
    for (auto k = sz(); k < b.size(); ++k) {
        auto cur_sum = i32();
        for (auto i = k; i < b.size(); ++i) {
            cur_sum += b[i];
            if (auto const it = b_sum.find(cur_sum); it != b_sum.end()) {
                ++it->second;
            } else {
                b_sum[cur_sum] = 1;
            }
        }
    }

    auto ans = i64();
    for (auto k = sz(); k < a.size(); ++k) {
        auto cur_sum = i32();
        for (auto i = k; i < a.size(); ++i) {
            cur_sum += a[i];
            if (auto const it = b_sum.find(t - cur_sum); it != b_sum.end()) {
                ans += it->second;
            }
        }
    }
    std::cout << ans << std::endl;

    return 0;
}
