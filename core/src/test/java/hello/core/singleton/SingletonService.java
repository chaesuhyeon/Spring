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
    // 이렇게하면 다른 곳에서  new 사용해서 SingletonService()를 생성할 수 없음
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출 ");
    }
}

// static 영역에 객체 instance를 미리 하나 생성해서 올려둠
// 이 객체 인스턴스가 필요하면 오직 getInstance() 메서드를 통해서만 조회할 수 있음
// 딱 1개의 객체 인스턴스만 존재해야 하므로 생성자를 private으로 막아서 혹시라도 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막음
