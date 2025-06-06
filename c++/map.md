## Map

### map의 특성
- 키-값 쌍으로 이루어진 순서가 있는 집합
- 키는 중복을 허용하지 않는다.
- 원소가 자동으로 정렬된다.
- 삽입/삭제/탐색에 `O(log N)`의 시간이 걸린다.
- 삽입 시 같은 키가 존재하면 삽입 하지않고 값을 업데이트 한다.
- 정렬이 필요하지 않은경우 비효율적이다.

### 예제 코드
```c++
#include <iostream>
#include <map>
#include <string>

using namespace std;

int main() {
    // map 컨테이너 선언
    // key: int (학생 ID), value: string (학생 이름)
    map<int, string> studentMap;

    // 삽입: 학생 ID와 이름을 맵에 추가
    // insert 함수
    // 인자: 삽입할 키와 값 쌍 (key, value)
    // 동작: 키가 존재하지 않으면 삽입, 존재하면 값을 업데이트
    // 시간복잡도: O(log N)
    studentMap.insert({101, "Alice"});
    studentMap.insert({102, "Bob"});
    studentMap.insert({103, "Charlie"});

    // 맵의 모든 요소 출력
    cout << "Initial map content:\n";
    for (const auto& pair : studentMap) {
        cout << "ID: " << pair.first << ", Name: " << pair.second << endl;
    }

    // 출력값:
    // Initial map content:
    // ID: 101, Name: Alice
    // ID: 102, Name: Bob
    // ID: 103, Name: Charlie

    // 탐색: 특정 ID로 학생 이름 찾기
    // find 함수
    // 인자: 찾을 키 (key)
    // 동작: 키가 존재하면 iterator 반환, 없으면 end() 반환
    // 시간복잡도: O(log N)
    auto it = studentMap.find(102);
    if (it != studentMap.end()) {
        cout << "\nStudent with ID 102 found: " << it->second << endl;
    } else {
        cout << "\nStudent with ID 102 not found.\n";
    }

    // 출력값:
    // Student with ID 102 found: Bob

    // 업데이트: 이미 존재하는 ID의 이름 변경
    // insert 함수
    // 인자: 삽입할 키와 값 쌍 (key, value)
    // 동작: 키가 존재하지 않으면 삽입, 존재하면 값을 업데이트
    // 시간복잡도: O(log N)
    studentMap.insert({102, "Bobby"});
    cout << "\nAfter updating ID 102:\n";
    for (const auto& pair : studentMap) {
        cout << "ID: " << pair.first << ", Name: " << pair.second << endl;
    }

    // 출력값:
    // After updating ID 102:
    // ID: 101, Name: Alice
    // ID: 102, Name: Bobby
    // ID: 103, Name: Charlie

    // 삭제: 특정 ID의 학생 정보 삭제
    // erase 함수
    // 인자: 삭제할 키 (key)
    // 동작: 키가 존재하면 해당 키의 요소를 삭제
    // 시간복잡도: O(log N)
    studentMap.erase(101);
    cout << "\nAfter erasing ID 101:\n";
    for (const auto& pair : studentMap) {
        cout << "ID: " << pair.first << ", Name: " << pair.second << endl;
    }

    // 출력값:
    // After erasing ID 101:
    // ID: 102, Name: Bobby
    // ID: 103, Name: Charlie

    // [] 연산자와 find의 차이점
    // [] 연산자
    // 인자: 접근할 키 (key)
    // 동작: 키가 존재하면 해당 키의 값을 반환, 없으면 키를 생성하고 기본값을 설정
    // 시간복잡도: O(log N)
    cout << "\nUsing [] operator:\n";
    cout << "Student with ID 103: " << studentMap[103] << endl; // 존재하는 키
    cout << "Student with ID 104: " << studentMap[104] << endl; // 존재하지 않는 키, 기본값 설정

    // 출력값:
    // Using [] operator:
    // Student with ID 103: Charlie
    // Student with ID 104: 

    // find 함수
    // 인자: 찾을 키 (key)
    // 동작: 키가 존재하면 iterator 반환, 없으면 end() 반환
    // 시간복잡도: O(log N)
    cout << "\nUsing find function:\n";
    it = studentMap.find(103);
    if (it != studentMap.end()) {
        cout << "Student with ID 103 found: " << it->second << endl;
    } else {
        cout << "Student with ID 103 not found.\n";
    }

    // 출력값:
    // Using find function:
    // Student with ID 103 found: Charlie

    return 0;
}
```

### map을 사용해야 하는 경우
- 키와 값이 쌍을 효율적으로 저장하고 관리할 때.
- 키를 기준으로 정렬된 순서로 데이터를 저장할 때.

이와 반대로 map을 사용하지 말아야 하는 경우는 다음과 같다.
- 키의 중복을 허용해야 할 때 -> multimap 사용
- 키의 순서가 중요하지 않는 경우
- 데이터 크기가 매우 크고, 연산이 더 빠른 시간복잡도를 요구할 경우.

## Unordered_map

### unordered_map의 특성
- 키와 값의 쌍으로이루어진 순서가 없는 집합.
- 키는 중복을 허용하지 않으며, 원소는 해시 테이블로 관리.
- 삽입, 삭제, 탐색의 연산은 `O(1)`의 시간복잡도를 가진다.
- 최악의 경우 `O(N)`도 가능하다.

### 예제 코드
```c++
#include <iostream>
#include <unordered_map>
#include <string>

using namespace std;

int main() {
    // unordered_map 컨테이너 선언
    // key: int (학생 ID), value: string (학생 이름)
    unordered_map<int, string> studentMap;

    // 삽입: 학생 ID와 이름을 맵에 추가
    // insert 함수
    // 인자: 삽입할 키와 값 쌍 (key, value)
    // 동작: 키가 존재하지 않으면 삽입, 존재하면 값을 업데이트
    // 시간복잡도: 평균 O(1), 최악 O(N)
    studentMap.insert({101, "Alice"});
    studentMap.insert({102, "Bob"});
    studentMap.insert({103, "Charlie"});

    // 맵의 모든 요소 출력
    cout << "Initial unordered_map content:\n";
    for (const auto& pair : studentMap) {
        cout << "ID: " << pair.first << ", Name: " << pair.second << endl;
    }

    // 출력값:
    // Initial unordered_map content:
    // ID: 101, Name: Alice
    // ID: 102, Name: Bob
    // ID: 103, Name: Charlie

    // 탐색: 특정 ID로 학생 이름 찾기
    // find 함수
    // 인자: 찾을 키 (key)
    // 동작: 키가 존재하면 iterator 반환, 없으면 end() 반환
    // 시간복잡도: 평균 O(1), 최악 O(N)
    auto it = studentMap.find(102);
    if (it != studentMap.end()) {
        cout << "\nStudent with ID 102 found: " << it->second << endl;
    } else {
        cout << "\nStudent with ID 102 not found.\n";
    }

    // 출력값:
    // Student with ID 102 found: Bob

    // 업데이트: 이미 존재하는 ID의 이름 변경
    // insert 함수
    // 인자: 삽입할 키와 값 쌍 (key, value)
    // 동작: 키가 존재하지 않으면 삽입, 존재하면 값을 업데이트
    // 시간복잡도: 평균 O(1), 최악 O(N)
    studentMap.insert({102, "Bobby"});
    cout << "\nAfter updating ID 102:\n";
    for (const auto& pair : studentMap) {
        cout << "ID: " << pair.first << ", Name: " << pair.second << endl;
    }

    // 출력값:
    // After updating ID 102:
    // ID: 101, Name: Alice
    // ID: 102, Name: Bobby
    // ID: 103, Name: Charlie

    // 삭제: 특정 ID의 학생 정보 삭제
    // erase 함수
    // 인자: 삭제할 키 (key)
    // 동작: 키가 존재하면 해당 키의 요소를 삭제
    // 시간복잡도: 평균 O(1), 최악 O(N)
    studentMap.erase(101);
    cout << "\nAfter erasing ID 101:\n";
    for (const auto& pair : studentMap) {
        cout << "ID: " << pair.first << ", Name: " << pair.second << endl;
    }

    // 출력값:
    // After erasing ID 101:
    // ID: 102, Name: Bobby
    // ID: 103, Name: Charlie

    // [] 연산자와 find의 차이점
    // [] 연산자
    // 인자: 접근할 키 (key)
    // 동작: 키가 존재하면 해당 키의 값을 반환, 없으면 키를 생성하고 기본값을 설정
    // 시간복잡도: 평균 O(1), 최악 O(N)
    cout << "\nUsing [] operator:\n";
    cout << "Student with ID 103: " << studentMap[103] << endl; // 존재하는 키
    cout << "Student with ID 104: " << studentMap[104] << endl; // 존재하지 않는 키, 기본값 설정

    // 출력값:
    // Using [] operator:
    // Student with ID 103: Charlie
    // Student with ID 104: 

    // find 함수
    // 인자: 찾을 키 (key)
    // 동작: 키가 존재하면 iterator 반환, 없으면 end() 반환
    // 시간복잡도: 평균 O(1), 최악 O(N)
    cout << "\nUsing find function:\n";
    it = studentMap.find(103);
    if (it != studentMap.end()) {
        cout << "Student with ID 103 found: " << it->second << endl;
    } else {
        cout << "Student with ID 103 not found.\n";
    }

    // 출력값:
    // Using find function:
    // Student with ID 103 found: Charlie

    return 0;
}
```

### unordered_map을 사용해야 하는 경우
- 키와 값의 쌍을 효율적으로 저장하고 관리할 때.
- 키의 순서가 중요하지 않을 때.
- 빠른 삽입, 삭제, 탐색이 필요할 때.

이와 반대로 이를 사용하지 말아야 할 경우는 다음과 같다.
- 키의 순서가 중요할 때
- 해시 함수의 충돌이 빈번할 경우
- 데이터의 크기가 매우 크고, 메모리 사용량이 중요한 경우

> 출처: https://www.inflearn.com/course/cpp-%EC%BD%94%EB%94%A9%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%95%A9%EA%B2%A9/dashboard