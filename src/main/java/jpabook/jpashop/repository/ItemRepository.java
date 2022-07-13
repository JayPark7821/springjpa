package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // db에서 id값을 생성하기때문에 최초에는 id값이 없다 (db에 저장하기 전까지)
        if (item.getId() == null) {
            em.persist(item);
        }else{
            // id값이 있으면 merge
            Item merge = em.merge(item);
            //  merge 와 item은 다르다

            // -> Item findItem = itemRepository.findOne(itemId);
            //    findItem.setPrice(param.getPrice());
            //    findItem.setName(param.getName());
            //    findItem.setStockQuantity(param.getStockQuantity());
            //    return findItem


            // 주의 변경감지 기능을 사용하면 원하는 속성만 선택해서 변경가능
            // merge는 merge시 값이 없으면 null로 업데이트함.
            // 넘어온 파라미터로 전부 업데이트 해버린다.

            // 실무에서는 변경감지를 사용한다!!!!!!!!!!!!
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }


}
