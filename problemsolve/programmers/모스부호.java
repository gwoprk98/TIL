package problemsolve.programmers;

import java.util.HashMap;

class 모스부호 {
    public String solution(String letter) {
        String answer = "";
        HashMap<String, Character> morse = new HashMap<>();
        morse.put(".-", 'a');
        morse.put("-...", 'b');
        morse.put("-.-.", 'c');
        morse.put("-..", 'd');
        morse.put(".", 'e');
        morse.put("..-.", 'f');
        morse.put("--.", 'g');
        morse.put("....", 'h');
        morse.put("..", 'i');
        morse.put(".---", 'j');
        morse.put("-.-", 'k');
        morse.put(".-..", 'l');
        morse.put("--", 'm');
        morse.put("-.", 'n');
        morse.put("---", 'o');
        morse.put(".--.", 'p');
        morse.put("--.-", 'q');
        morse.put(".-.", 'r');
        morse.put("...", 's');
        morse.put("-", 't');
        morse.put("..-", 'u');
        morse.put("...-", 'v');
        morse.put(".--", 'w');
        morse.put("-..-", 'x');
        morse.put("-.--", 'y');
        morse.put("--..", 'z');

        String[] str = letter.split(" ");
        for (String s : str) {
            answer+= morse.get(s);
        }

        return answer;
    }
}