package hello.hellospring.controller;

public class MemberForm {
    private String name; // createMemberForm.html을 보면 input 태그에 name= "name" 이 있는데 / 입력된 name을 여기다가 넣어줌

    public String getName() {
        return name;
    }

    // name이 private이므로 마음대로 접근하지 x / setName을 통해서 접근
    public void setName(String name) {
        this.name = name;
    }
}
