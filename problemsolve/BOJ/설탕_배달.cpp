#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    int count = 0;

    while (true) {
        if (n % 5 == 0) {
            count += n / 5;
            cout << count;
            break;
        }
        n -= 3;
        count++;

        if (n < 0) {
            cout << -1;
            break;
        }
    }
}