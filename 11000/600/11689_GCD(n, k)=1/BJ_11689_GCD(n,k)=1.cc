// https://www.acmicpc.net/problem/11689

#include <cstdint>
#include <iostream>
#include <utility>

using u64 = std::uint64_t;

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

template <typename T>
T GCD(T a, T b) {
    if (a < b) {
        std::swap(a, b);
    }
    while (a % b != 0) {
        auto const r = a % b;
        a = b;
        b = r;
    }
    return b;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n0 = Read<u64>(std::cin);
    auto n = n0;
    auto nu = u64(1);
    auto de = u64(1);
    auto update = [&nu, &de](auto const v) mutable {
        nu *= v - 1;
        de *= v;
        auto const gcd = GCD(nu, de);
        nu /= gcd;
        de /= gcd;
    };
    for (auto i = u64(2); i <= n && i * i <= n0; ++i) {
        if (n % i == 0) {
            update(i);
        }
        while (n % i == 0) {
            n /= i;
        }
    }
    if (n != 1) {
        update(n);
    }
    std::cout << n0 / de * nu << std::endl;
    return 0;
}