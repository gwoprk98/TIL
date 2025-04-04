지금 부터는 STL에 존재하고 코딩 테스트에서 유용하게 활용 가능한 기본적인 알고리즘에 대해 알아보도록 하자.

## Count
- 데이터를 세는 함수
- 특정 값의 출현 횟수를 반환한다.

### 예제 코드
```c++
#include <iostream>
#include <vector>
#include <algorithm> // for std::count
using namespace std;

int main() {
    // 예시 벡터 생성
    vector<int> v = {1, 2, 3, 4, 5, 1, 2, 1};

    // count 함수 사용 예시
    // std::count 함수는 주어진 범위에서 특정 값이 몇 번 나타나는지 센다.
    // 여기서 v.begin()과 v.end()는 벡터의 시작과 끝을 나타내며,
    // 1은 찾고자 하는 값이다.
    int count_of_1 = count(v.begin(), v.end(), 1);

    // 결과 출력
    cout << "벡터에서 1의 개수: " << count_of_1 << endl;

    // 출력값:
    // 벡터에서 1의 개수: 3


    return 0;
}
```

`std:count`함수의 시간 복잡도는 `O(N)`이다
count함수는 집합에서 특정 값의 출현 빈도를 알고 싶을 때 사용한다.

## sort
- 데이터를 정렬하는 함수이다.
- 정렬기준을 정의하지 않으면 오름차순으로 정렬된다.
- 사용자 정의형의 경우 무조건 정렬 기준을 정의해야 한다.

### 예제 코드
```c++
#include <iostream>
#include <vector>
#include <algorithm> // sort() 함수가 포함된 헤더

using namespace std; // std 네임스페이스를 사용

// 사용자 정의 비교 함수
bool compare(int a, int b) {
    // 내림차순 정렬을 위한 비교: a가 b보다 클 때 true를 반환
    // sort 함수가 비교 시 compare(a, b)가 true를 반환하면 a가 b보다 앞에 있어야 한다고 판단하여 a와 b의 위치를 교환하지 않음
    return a > b;
}

int main() {
    // 정렬할 벡터 생성
    vector<int> v = {5, 2, 9, 1, 5, 6};

    // 벡터를 오름차순으로 정렬
    // std::sort는 기본적으로 < 연산자를 사용하여 정렬
    // v.begin()은 첫 번째 요소, v.end()는 마지막 요소의 다음 위치를 가리킴 (v.end()는 정렬 대상에 포함되지 않음)
    sort(v.begin(), v.end());

    // 오름차순으로 정렬된 벡터 출력
    cout << "오름차순 정렬: ";
    for (int n : v) {
        cout << n << ' ';
    }
    cout << endl;
    // 출력값: 1 2 5 5 6 9

    // 벡터를 다시 섞어서 초기 상태로 되돌림
    v = {5, 2, 9, 1, 5, 6};

    // 벡터를 사용자 정의 비교 함수로 정렬 (내림차순)
    // std::sort는 compare 함수가 true를 반환할 때 첫 번째 요소가 두 번째 요소보다 앞에 있어야 한다고 판단
    // compare 함수는 a > b일 때 true를 반환하므로, 큰 값이 작은 값 앞에 오게 되어 내림차순 정렬이 이루어짐
    sort(v.begin(), v.end(), compare);

    // 내림차순으로 정렬된 벡터 출력
    cout << "내림차순 정렬: ";
    for (int n : v) {
        cout << n << ' ';
    }
    cout << endl;
    // 출력값: 9 6 5 5 2 1

    // 벡터를 다시 섞어서 초기 상태로 되돌림
    v = {5, 2, 9, 1, 5, 6};

    // 벡터의 앞 3개 요소만 오름차순으로 정렬
    // v.begin()에서 v.begin() + 3까지 정렬
    // 정렬 범위: [5, 2, 9] (v.begin() + 3는 정렬 대상에 포함되지 않음)
    sort(v.begin(), v.begin() + 3);

    // 부분 정렬된 벡터 출력
    cout << "부분 정렬 (앞 3개 요소 오름차순): ";
    for (int n : v) {
        cout << n << ' ';
    }
    cout << endl;
    // 출력값: 2 5 9 1 5 6

    // 벡터를 다시 섞어서 초기 상태로 되돌림
    v = {5, 2, 9, 1, 5, 6};

    // 역방향 반복자를 사용하여 벡터를 내림차순으로 정렬
    // v.rbegin()은 마지막 요소, v.rend()는 첫 번째 요소의 이전 위치를 가리킴 (v.rend()는 정렬 대상에 포함되지 않음)
    sort(v.rbegin(), v.rend());

    // 역방향으로 정렬된 벡터 출력
    cout << "역방향 반복자로 정렬 (내림차순): ";
    for (int n : v) {
        cout << n << ' ';
    }
    cout << endl;
    // 출력값: 9 6 5 5 2 1

    return 0;
}
```

## Unique
- 인접한 중복 요소를 뒤로 재배치하는 함수이다.
- 중복되지 않는 범위의 끝을 나타내는 반복자를 반환한다.
- 함수 사용후 중복요소가 제거된 것이 아니기 때문에 완전히 제거하려면 `erase()`함수의 추가적인 사용이 필요하다.
- 정렬된 상태에서 사용할 경우 모든 중복요소의 제거가 가능하다.

### 예제 코드
```c++
#include <iostream>
#include <vector>
#include <algorithm> // for std::unique and std::sort
using namespace std;

/*
std::unique 함수는 주어진 범위에서 인접한 중복 요소를 재배치합니다.
이 함수는 정렬된 상태에서 사용하면 모든 중복 요소를 올바르게 처리할 수 있습니다.


예제:
벡터가 {1, 2, 2, 3, 3, 3, 4, 4, 5}로 주어졌을 때,
unique(v.begin(), v.end())를 호출하면,
벡터는 {1, 2, 3, 4, 5, 3, 4, 4, 5}로 변환되고, 반환된 new_end는 첫 번째 5를 가리킵니다.
v.erase(new_end, v.end())를 호출하면, 벡터는 {1, 2, 3, 4, 5}로 변환됩니다.
*/

int main() {
    // 예시 벡터 생성 (정렬되지 않은 상태)
    vector<int> v1 = {3, 1, 2, 3, 2, 4, 1, 5, 3};

    // 중복 요소 재배치 (정렬되지 않은 상태에서)
    auto last1 = unique(v1.begin(), v1.end());

    // 결과 출력 (정렬되지 않은 상태에서 unique 사용 후)
    cout << "정렬되지 않은 상태에서 unique 사용 후:\n";
    for (auto it = v1.begin(); it != last1; ++it) {
        cout << *it << " ";
    }
    cout << endl;

    // 벡터를 정렬된 상태로 만들기
    vector<int> v2 = {3, 1, 2, 3, 2, 4, 1, 5, 3};
    sort(v2.begin(), v2.end());

    // 중복 요소 재배치 (정렬된 상태에서)
    auto last2 = unique(v2.begin(), v2.end());

    // 새로운 끝 부분까지 벡터를 잘라내기
    v2.erase(last2, v2.end());

    // 결과 출력 (정렬된 상태에서 unique 사용 후)
    cout << "정렬된 상태에서 unique 사용 후:\n";
    for (int num : v2) {
        cout << num << " ";
    }
    cout << endl;

    return 0;
}

/*
출력값:

정렬되지 않은 상태에서 unique 사용 후:
3 1 2 3 2 4 1 5 3 

정렬된 상태에서 unique 사용 후:
1 2 3 4 5
*/
```
unique 함수는 사용할 때 다음과 같은 주의점이 존재한다.
1. 정렬된 상태에서 사용: 인접한 중복 요소만 재배치 하기 때문.
2. 반환값: 새로운 끝 부분을 반환한다. 중복되지 않은 요소를 앞으로 재배치 하는 것.
3. 벡터 크기 조정: 중복 요소를 재배치 한 후, 반환된 새로운 끝 부분까지 벡터를 잘라내야 한다.
4. 새로운 범위: 반환된 반복자는 중복되지 않은 요소로 이루어진 새로운 범위의 끝을 의미한다. 이 범위는 벡터의 시작부터 반환된 반복자 전까지이다.

## binary_search
- 정렬된 범위에서 특정 값을 찾는 함수.
- 값이 존재하면 true, 존재하지 않으면 false를 반환한다.
- `O(logN)`을 보장한다.

### 예제 코드

```c++
#include <iostream>
#include <vector>
#include <algorithm> // for std::binary_search and std::sort
using namespace std;

int main() {
    // 예시 벡터 생성
    vector<int> v = {1, 3, 4, 5, 7, 9, 10};

    // 찾고자 하는 값
    int value1 = 5;
    int value2 = 6;

    // 이진 탐색 사용 예시
    bool found1 = binary_search(v.begin(), v.end(), value1);
    bool found2 = binary_search(v.begin(), v.end(), value2);

    // 결과 출력
    cout << "값 " << value1 << "를 찾는 중: " << (found1 ? "찾음" : "찾지 못함") << endl;
    cout << "값 " << value2 << "를 찾는 중: " << (found2 ? "찾음" : "찾지 못함") << endl;

    return 0;
}

/*
출력값:

값 5를 찾는 중: 찾음
값 6를 찾는 중: 찾지 못함
*/
```

## max_element / min_element
- 범위 내에서 가장 큰, 가장 작은 원소를 찾는 함수.
- 반복자 범위에서 가장 큰, 가장 작은 원소를 가르키는 반복자를 반환한다.
- 선형 탐색을 사용하므로 `O(N)`의 시간 복잡도를 보장한다.

### 예제 코드
```c++
#include <iostream>
#include <vector>
#include <algorithm> // for std::max_element
using namespace std;

int main() {
    // 예시 벡터 생성
    vector<int> v = {1, 3, 4, 5, 7, 9, 10};

    // 최대값 찾기
    auto max_it = max_element(v.begin(), v.end());

    // 결과 출력
    cout << "최대값: " << *max_it << " (위치: " << distance(v.begin(), max_it) << ")" << endl;

    return 0;
}

/*
출력값:

최대값: 10 (위치: 6)
*/

//======================//

#include <iostream>
#include <vector>
#include <algorithm> // for std::min_element
using namespace std;

int main() {
    // 예시 벡터 생성
    vector<int> v = {1, 3, 4, 5, 7, 9, 10};

    // 최소값 찾기
    auto min_it = min_element(v.begin(), v.end());

    // 결과 출력
    cout << "최소값: " << *min_it << " (위치: " << distance(v.begin(), min_it) << ")" << endl;

    return 0;
}

/*
출력값:

최소값: 1 (위치: 0)
*/
```

## next_permutation
- 주어진 범위의 요소들에 대해 다음 순열을 생성한다.
- 순열이 더 이상 없으면 false, 그렇지 않다면 true를 반환한다.
- 모든 순열을 생성하기 위해서는 정렬되어 있어야 한다.
- 시간 복잡도는 `O(N*N!)`이 보장된다.

### 예제 코드
```c++
#include <iostream>
#include <vector>
#include <algorithm> // for std::next_permutation and std::sort
using namespace std;

// 벡터의 모든 순열을 출력하는 함수
void print_permutations(vector<int> v) {
    do {
        // 현재 순열 출력
        for (int num : v) {
            cout << num << " ";
        }
        cout << endl;
    } while (next_permutation(v.begin(), v.end()));
}

int main() {
    // 예시 벡터 생성
    vector<int> v = {3, 2, 1};

    // 정렬되지 않은 경우
    cout << "정렬되지 않은 경우:\n";
    vector<int> v_unsorted = v;
    print_permutations(v_unsorted);

    // 벡터를 정렬된 상태로 만들기
    sort(v.begin(), v.end());

    // 정렬된 경우
    cout << "\n정렬된 경우:\n";
    vector<int> v_sorted = v;
    print_permutations(v_sorted);

    return 0;
}

/*
출력값:

정렬되지 않은 경우:
3 2 1

정렬된 경우:
1 2 3
1 3 2
2 1 3
2 3 1
3 1 2
3 2 1
*/
```

> 출처: https://www.inflearn.com/course/cpp-%EC%BD%94%EB%94%A9%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%95%A9%EA%B2%A9/dashboard