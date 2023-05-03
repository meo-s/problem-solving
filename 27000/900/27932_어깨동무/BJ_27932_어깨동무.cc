// https://www.acmicpc.net/problem/27932

#include <algorithm>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;

int simulate(vector<int> const& people, int const H) {
    auto k = 0;
    for (vector<int>::size_type i = 0; i < people.size(); ++i) {
        if (0 < i && H < abs(people[i] - people[i - 1])) ++k;
        else if (i + 1 < people.size() && H < abs(people[i] - people[i + 1])) ++k;
    }
    return k;
}

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<int>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto const N = *in;
    auto const K = *++in;
    auto people = vector<int>(N);
    copy_n(++in, people.size(), begin(people));

    int lb = 0, ub = 1'000'000'001;
    auto min_H = ub;
    while (lb < ub) {
        auto const mid = (lb + ub) / 2;
        if (simulate(people, mid) <= K) {
            min_H = min(min_H, mid);
            ub = mid;
        } else lb = mid + 1;
    }

    cout << min_H << endl;
    return 0;
}
