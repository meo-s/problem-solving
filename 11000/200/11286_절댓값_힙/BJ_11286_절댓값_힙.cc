// https://www.acmicpc.net/problem/11286

#include <cmath>
#include <functional>
#include <iostream>
#include <iterator>
#include <vector>

template <typename T> class Heap {
public:
  using Container = ::std::vector<T>;

  Heap(::std::function<bool(T const&, T const&)> const& cmp)
      : _cont{}, _cmp{cmp} {}

  Heap(Heap const&) = delete;
  ~Heap() = default;

  Container& native() { return _cont; }

  T& at(size_t i) { return _cont.at(i); }

  typename Container::size_type size() const { return _cont.size(); }

  template <typename U> void push(U&& v) {
    _cont.emplace_back(::std::forward<U>(v));

    auto i = _cont.size() - 1;
    while (0 < i) {
      auto const j = (i - 1) / 2;
      if (!cmpi(i, j)) {
        break;
      }

      swapi(i, j);
      i = j;
    }
  }

  T pop() {
    if (1 < _cont.size()) {
      ::std::swap(at(0), at(size() - 1));
    }

    auto ret = ::std::move(at(size() - 1));
    _cont.pop_back();

    if (1 < size()) {
      auto i = static_cast<decltype(size())>(0);
      while (i * 2 + 1 < size()) {
        auto j = i;
        if (cmpi(i * 2 + 1, j)) j = i * 2 + 1;
        if (i * 2 + 2 < size() && cmpi(i * 2 + 2, j)) j = i * 2 + 2;
        if (i == j) break;
        swapi(i, j);
        i = j;
      }
    }

    return ret;
  }

private:
  void swapi(size_t i, size_t j) { ::std::swap(at(i), at(j)); }

  bool cmpi(size_t i, size_t j) { return _cmp(at(i), at(j)); }

private:
  ::std::vector<T> _cont;

  ::std::function<bool(T, T)> _cmp;
};

template <typename T> void print(::std::vector<T> const& vec) {
  for (auto const& v : vec) {
    ::std::cout << v << ' ';
  }

  ::std::cout.put('\n');
}

bool abs_cmp(int const& v1, int const& v2) {
  if (::std::abs(v1) < ::std::abs(v2)) return true;
  return (::std::abs(v1) == ::std::abs(v2)) && (v1 < v2);
}

int main(int argc, char* argv[]) {
  ::std::cin.tie(0);
  ::std::cout.sync_with_stdio(false);
  auto it = ::std::istream_iterator<int>{::std::cin};

  auto heap = Heap<int>{abs_cmp};
  heap.native().reserve(100000);

  auto N = *it;
  while (0 < N--) {
    auto cmd = *(++it);
    if (cmd != 0) heap.push(cmd);
    else ::std::cout << (0 < heap.size() ? heap.pop() : 0) << '\n';
  }

  return 0;
}
