package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * * userA
 *   * JPA1 BOOK
 *   * JPA2 BOOk
 * * userB
 *   * SPRING1 BOOK
 *   * SPRING2 BOOk
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class  InitService {
        private final EntityManager em;
        public void dbInit1(){
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOk" , 10000 , 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOk" , 20000 , 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);

            //Order의 createOrder 메서드를 보면 OrderItem을 (OrderItem... orderItems) 이런식으로 스프레드를 사용하여 넣었기 때문에 아래처럼 OrderItem을 여러개 넣을 수 있음
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public void dbInit2(){
            Member member = createMember("userB", "전주", "2", "2222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOk" , 20000 , 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOk" , 40000 , 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);

            //Order의 createOrder 메서드를 보면 OrderItem을 (OrderItem... orderItems) 이런식으로 스프레드를 사용하여 넣었기 때문에 아래처럼 OrderItem을 여러개 넣을 수 있음
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        //== 공통 메서드 ==//
        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private static Book createBook(String name, int price , int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
        //== end 공통 메서드 ==//
    }

}