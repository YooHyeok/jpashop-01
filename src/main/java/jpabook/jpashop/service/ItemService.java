package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Item Service
 * 상품 서비스
 * 상품 리포지토리에 단순히 위임만 하는 클래스이다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    /**
     * 상품 저장
     */
    @Transactional(readOnly = false)
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 전체 조회
     */
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     * 단건 조회
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
