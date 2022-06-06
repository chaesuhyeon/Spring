package hello.core.singleton;

public class SingletonService {
    private static final SingletonService instance = new SingletonService();
    // 자기 자신을 내부에 private으로 하나 생성 . static으로 생성

    public static SingletonService getInstance() {
        return instance;
    }

    public static void main(String[] args) {

    }
}
