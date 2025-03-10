## Set

### Set의 특성
- 중복을 허용하지 않는 순서가 없는 집합이다.
- 원소가 자동으로 정렬된다(내부에 균형이진트리 알고리즘)
- 삽입/삭제/탐색에 `O(log N)`의 시간이 소요
- 중복을 허용하지 않거나 원소를 삽입과 동시에 정렬해야 하는 경우 효율적이다.
- 정렬이 필요하지 않은 경우 사용하지 않는것이 좋다.

### set 예제 코드
```c++
#include <iostream>
#include <set>
#include <vector>
#include <algorithm>

int main() {
    // 초기화
    std::set<int> s;

    // insert: 원소 삽입
    // 시간복잡도: O(log N)
    s.insert(10);  // {10}
    s.insert(20);  // {10, 20}
    s.insert(10);  // {10, 20} (중복된 값은 삽입되지 않음)

    // find: 원소 탐색
    // 시간복잡도: O(log N)
    auto it = s.find(10); // 10을 가리키는 반복자 반환
    if (it != s.end()) {
        std::cout << "Found: " << *it << std::endl; // 출력: Found: 10
    } else {
        std::cout << "Not Found" << std::endl;
    }

    // erase: 원소 삭제
    // 시간복잡도: O(log N)
    s.erase(10); // {20}

    // find 메서드를 사용하여 삭제된 원소를 찾으려 하면
    it = s.find(10);
    if (it != s.end()) {
        std::cout << "Found: " << *it << std::endl;
    } else {
        std::cout << "Not Found" << std::endl; // 출력: Not Found
    }
}
```

### set을 사용해야 하는 경우
1. 중복을 허용하지 않는 경우
    - 유일한 사용자 id를 저장해야 하는 경우가 이에 해당한다.
```c++
 std::set<int> uniqueIds;
    uniqueIds.insert(1);
    uniqueIds.insert(2);
    uniqueIds.insert(1); // 중복 삽입 무시
    for (int id : uniqueIds) {
        std::cout << "User ID: " << id << std::endl; // 출력: User ID: 1, User ID: 2
    }
```
2. 정렬된 순서가 필요한 경우
```c++
std::set<int> sortedData;
    sortedData.insert(5);
    sortedData.insert(1);
    sortedData.insert(3);
    for (int val : sortedData) {
        std::cout << "Sorted Value: " << val << std::endl; // 출력: Sorted Value: 1, 3, 5
    }
```
3. 탐색, 삽입, 삭제의 성능이 중요한 경우
    - 데이터의 존재 여부를 빈번히 검사해야하는 경우가 이에 해당한다.
```c++
 std::set<int> dataSet;
    dataSet.insert(15);
    if (dataSet.find(15) != dataSet.end()) {
        std::cout << "15 is in the set" << std::endl;
    }
```

이와 반대로 set을 사용하지 말아야 하는 경우는 다음과 같다.
1. 중복된 원소를 허용해야하는 경우
2. 정렬이 필요없는 경우
3. 빈번 삽입과 삭제가 필요한 경우

## Unordered_set

### unordered_set의 특성
- 중복을 허용하지 않는 순서가 없는 집합
- 원소가 해시 테이블로 관리(자동 정렬 되지 않는다.)
- 삽입/삭제/탐색은 평균적으로 `O(1)`의 시간이 걸리나 최악의 경우 `O(N)`의 시간이 걸릴 수 도 있다.
- 중복을 허용하지 않는다.
- 해시 함수에 따라 원소 순서가 결정된다.

### unordered_set 예제 코드
```c++
#include <iostream>
#include <unordered_set>

using namespace std;

int main() {
    // unordered_set 컨테이너 선언
    // value: int (학생 ID)
    unordered_set<int> studentSet;

    // 삽입: 학생 ID를 셋에 추가
    // insert 함수
    // 인자: 삽입할 값 (value)
    // 동작: 값이 존재하지 않으면 삽입
    // 시간복잡도: 평균 O(1), 최악 O(N)
    studentSet.insert(101);
    studentSet.insert(102);
    studentSet.insert(103);

    // 셋의 모든 요소 출력
    cout << "Initial unordered_set content:\n";
    for (const auto& value : studentSet) {
        cout << "ID: " << value << endl;
    }

    // 출력값:
    // Initial unordered_set content:
    // ID: 101
    // ID: 102
    // ID: 103

    // 탐색: 특정 ID가 셋에 있는지 찾기
    // find 함수
    // 인자: 찾을 값 (value)
    // 동작: 값이 존재하면 iterator 반환, 없으면 end() 반환
    // 시간복잡도: 평균 O(1), 최악 O(N)
    auto it = studentSet.find(102);
    if (it != studentSet.end()) {
        cout << "\nStudent with ID 102 found.\n";
    } else {
        cout << "\nStudent with ID 102 not found.\n";
    }

    // 출력값:
    // Student with ID 102 found.

    // 삭제: 특정 ID의 학생 정보 삭제
    // erase 함수
    // 인자: 삭제할 값 (value)
    // 동작: 값이 존재하면 해당 값을 삭제
    // 시간복잡도: 평균 O(1), 최악 O(N)
    studentSet.erase(101);
    cout << "\nAfter erasing ID 101:\n";
    for (const auto& value : studentSet) {
        cout << "ID: " << value << endl;
    }

    // 출력값:
    // After erasing ID 101:
    // ID: 102
    // ID: 103

    // [] 연산자는 unordered_set에서는 사용할 수 없음. 대신 find를 사용해야 함.
    // find 함수
    // 인자: 찾을 값 (value)
    // 동작: 값이 존재하면 iterator 반환, 없으면 end() 반환
    // 시간복잡도: 평균 O(1), 최악 O(N)
    it = studentSet.find(103);
    if (it != studentSet.end()) {
        cout << "\nStudent with ID 103 found: " << *it << endl;
    } else {
        cout << "\nStudent with ID 103 not found.\n";
    }

    // 출력값:
    // Student with ID 103 found: 103

    return 0;
}
```

### unorder_set을 사용해야 하는 경우
- 중복되지 않는 값의 집합을 효율적으로 저장하고 관리.
- 원소의 순서가 중요하지 않을 때.
- 빠른 삽입, 삭제, 탐색이 필요할 때

이와 반대로 이를 사용하지 말아야 할 경우는 다음과 같다.
- 원소의 순서가 중요할 때 -> set을 사용한다.
- 해시함수의 충돌이 빈번하게 발생할 때.
- 메모리 사용량이 중요한 경우.

> 출처: https://www.inflearn.com/course/cpp-%EC%BD%94%EB%94%A9%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%95%A9%EA%B2%A9/dashboard