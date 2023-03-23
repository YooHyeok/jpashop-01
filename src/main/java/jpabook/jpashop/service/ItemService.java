package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.controller.MemberForm;
import jpabook.jpashop.domain.item.Book;
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
     * 상품 수정
     * 변경 감지 기능
     */
    @Transactional
    public void updateItem(Long itemId, BookForm form) {
        Item item = itemRepository.findOne(itemId);
        if (form.getClass().getSimpleName().equals("BookForm")) {
            Book findItem = (Book) item; // 다형성을 통해 Book으로 다운캐스팅 (Item으로 생성했기 때문에 isbn이나 author는 수정이 불가능)
            findItem.setName(form.getName());
            findItem.setPrice(form.getPrice());
            findItem.setStockQuantity(form.getStockQuantity());
            findItem.setAuthor(form.getAuthor());
            findItem.setIsbn(form.getIsbn());
        }
        if (form.getClass().getSimpleName().equals("AlbumForm")) {

        }
        if (form.getClass().getSimpleName().equals("MovieForm")) {

        }
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
