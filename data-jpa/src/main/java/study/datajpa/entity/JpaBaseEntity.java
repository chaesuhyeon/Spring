package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 속성만 상속
public class JpaBaseEntity {

    @Column(updatable = false) // createdDate는 update 되지 못하게 막음 -> 혹시 실수로 바꿔도 db에는 수정되지 않음
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // persist 하기 전에 실행
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now; // update에 data를 미리 넣어두면 쿼리 날릴 때 편함
    }

    @PreUpdate // update 하기 전에 호출
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
