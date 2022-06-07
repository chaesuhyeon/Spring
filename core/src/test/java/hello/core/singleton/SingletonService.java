package hello.core.singleton;

//자바가 뜨면서 static영역에 있는 instance를 초기화 하고
public class SingletonService {
    private static final SingletonService instance = new SingletonService();
    // 자기 자신을 내부에 private으로 하나 생성 . static으로 생성
    // 자기 자신을 생성해서 instance 변수에 참조로 넣어줌

    public static SingletonService getInstance() {
        return instance;
    }

    // private으로 막아줌
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출 ");
    }
}
