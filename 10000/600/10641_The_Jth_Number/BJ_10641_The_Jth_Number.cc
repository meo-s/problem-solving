// https://www.acmicpc.net/problem/10641

#include <algorithm>
#include <cstdint>
#include <cstring>
#include <iostream>
#include <tuple>
#include <vector>

using namespace std;

using i32 = int32_t;
using i64 = int64_t;
using sz = size_t;

struct Node {
	Node* l;
	Node* r;
	i64 v;
	i32 p;

	Node()
		: l{nullptr}
		, r{nullptr}
		, v{0}
		, p{0} {}

	Node(Node const& other)
		: l{other.l}
		, r{other.r}
		, v{other.v}
		, p{other.p} {}
};

class LazySegmentTree {

	i32 size;
	vector<Node*> roots;

	void commit_left_node(Node* node, bool clone = true) {
		node->l = node->l != nullptr ? (clone ? new Node{*node->l} : node->l) : new Node{};
	}

	void commit_right_node(Node* node, bool clone = true) {
		node->r = node->r != nullptr ? (clone ? new Node{*node->r} : node->r) : new Node{};
	}

	i64 update(Node* node, i32 const left, i32 const right, i32 const beg, i32 const end) {
		auto const mid = (left + right) / 2;
		if (left == beg && right == end) {
			++node->p;
			return end - beg;
		}
		else {
			i64 const before = node->v;

			if (end <= mid) {  // left <= beg < end <= mid
				commit_left_node(node);
				node->v += update(node->l, left, mid, beg, end);
			}
			else if (mid <= beg) { // mid <= beg < end <= right
				commit_right_node(node);
				node->v += update(node->r, mid, right, beg, end);
			}
			else {
				commit_left_node(node);
				node->v += update(node->l, left, mid, beg, mid);
				commit_right_node(node);
				node->v += update(node->r, mid, right, mid, end);
			}

			return node->v - before;
		}
	}

	i64 query_left(Node* node, i32 const left, i32 const right, i32 const beg, i32 const end) {
		auto const mid = (left + right) / 2;
		return (node->l != nullptr ? query(node->l, left, mid, beg, min(end, mid)) : 0) + i64(min(end, mid) - beg) * node->p;
	}

	i64 query_right(Node* node, i32 const left, i32 const right, i32 const beg, i32 const end) {
		auto const mid = (left + right) / 2;
		return (node->r != nullptr ? query(node->r, mid, right, max(beg, mid), end) : 0) + i64(end - max(beg, mid)) * node->p;
	}

	i64 query(Node* node, i32 const left, i32 const right, i32 const beg, i32 const end) {
		if (left == beg && right == end) {
			return node->v + i64(end - beg) * node->p;
		}
		else {
			auto const mid = (left + right) / 2;
			if (end <= mid) {
				return query_left(node, left, right, beg, end);
			}
			else if (mid <= beg) {
				return query_right(node, left, right, beg, end);
			}
			else {
				return query_left(node, left, right, beg, end) + query_right(node, left, right, beg, end);
			}
		}
	}

public:

	LazySegmentTree(i32 const size)
		: size{size}, roots(0) {
		roots.reserve(500'001);
		roots.emplace_back(new Node{});
	}

	void update(i32 const beg, i32 const end) {
		roots.emplace_back(new Node{*roots.back()});
		update(roots.back(), 1, size + 1, beg, end);
	}

	i64 query(i32 const k, i32 const beg, i32 const end) {
		return query(roots[k], 1, size + 1, beg, end);
	}

};


template <typename T>
T in() {
	T ret; cin >> ret;
	return ret;
}

i32 N, M, Q;
vector<tuple<i32, i32, i32>> qm(0);

void init() {
	N = in<i32>();
	M = in<i32>();
	Q = in<i32>();

	qm.reserve(M);
	for (i32 i = 0; i < M; ++i) {
		i32 const a = in<i32>();
		i32 const b = in<i32>() + 1;
		i32 const v = in<i32>();
		qm.emplace_back(a, b, v);
	}

	sort(qm.begin(), qm.end(), [](auto const& lhs, auto const& rhs) {return get<2>(lhs) < get<2>(rhs); });
}

void execute_queries() {
	LazySegmentTree tree(N);
	for (i32 m = 0; m < M; ++m) {
		auto const [a, b, _] = qm[m];
		tree.update(a, b);
	}

	for (i32 q = 0; q < Q; ++q) {
		i32 const x = in<i32>();
		i32 const y = in<i32>() + 1;
		i64 const j = in<i64>();

		i32 ans = -1;
		i32 lb = 0;
		i32 ub = M;
		while (lb < ub) {
			auto const mid = (lb + ub) / 2;
			if (j <= tree.query(mid + 1, x, y)) {
				ub = mid;
				ans = get<2>(qm[mid]);
			}
			else {
				lb = mid + 1;
			}
		}

		cout << ans << '\n';
	}

	cout.flush();
}

int main(int argc, char const* argv[]) {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	init();
	execute_queries();

	return 0;
}
