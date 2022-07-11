* ### 엔티티 클래스 개발
  * 예제에서는 설명을 쉽게하기 위해 엔티티 클래스에 `Getter`, `Setter`를 모두 열고, 최대한 단순하게 설계
  * 실무에서는 가급적 `Getter`는 열어두고, `Setter`는 꼭 필요한 경우에만 사용하는 것을 추천


___
* ### @Embedded @Embeddable
> 새로운 값 타입을 직접 정의해서 사용할 수 있는데,   
>JPA에서는 이것을 임베디드 타입(embedded type)이라 합니다.  
>중요한 것은 직접 정의한 임베디드 타입도 int, String처럼 값 타입이라는 것입니다.
>

* @Embeddable: 값 타입을 정의하는 곳에 표시
* @Embedded: 값 타입을 사용하는 곳에 표시
* 임베디드 타입은 기본 생성자가 필수


MEMBER 와 ORDERS   
객체상 MEMBER도 주문정보 ORDERS를 가지고 있고   
주문정보도 ORDERS도 MEMBER를 가지고있다.
그런데 DB상 FK는 ORDERS에 MEMBER_ID 하나뿐이 없다

MEMBER와 ORDERS의 관계를 변경하고싶으면 
ORDERS에 MEMBER_ID값을 변경해야지 관계가 변한다.  

객체는 변경 포인트가 2군데 이다. MEMBER, ORDERS  
DB는 FK만 변경하면 된다.

연관관계의 주인의 값이 변경될때 FK값 UPDATE 즉 FK을 가지고 있는 테이블이 연관관계의 주인으로!!
만약 FK가 없는 테이블이 연관관계의 주인이되면 주인을 변경하면 변경하지 않은 다른 테이블의 FK값이 변경된다!!!  

연관관계의 주인이 아닌쪽에 mappedBy = "테이블명" 입력해주면된다.


  
  
1 : 1 관계에서는 자주 조회하는쪽에 FK를 넣는다.
ORDERS를 가지고 DELIVERY를 찾는경우가 많으니 ORDERS에 FK를 할당

___

값 타입은 변경 불가능하게 설계해야한다.
@Setter 를 제거하고 생성자에서 모든 값을 초기화해서 변경 불가능한 클래스를 만들자

```java

package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // jpa 내장타입
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;


    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}

```

* 엔티티에는 가급적 setter를 사용하지 말자.
Setter가 모두 열려있으면 변경포인트가 너무 많아서 유지보수가 어렵다.  

  

* 모든 연관관계는 지연로딩으로 설정!  
즉시로딩은 예측이 어렵고 어떤 sql이 실행될지 추적하기 어렵다.  
실무에서는 모든 연관관계는 지연로딩`LAZY`로 설정해야한다.  
연관된 엔티티를 함께 DB에서 조회해야하면, fetch join 또는 엔티티 그래프 기능을 사용  
@XToOne(OneToOne, ManyToOne)관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야 한다.




