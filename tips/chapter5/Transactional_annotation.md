1. 같은 클래스 내부 호출 — 프록시 우회

```java

@Service
public class BookService {
    public void processBooks() {
        saveBook(book);  // ❌ 프록시를 거치지 않아 @Transactional이 무시된다
    }

    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
```

2. 트랜잭션 안에서 외부 호출

```java
// [실무노트] 트랜잭션 안에서 외부 API를 호출하면 커넥션 점유 시간이 길어진다.
// 외부 서비스가 느려지면 커넥션 풀이 고갈되어 전체 서비스가 마비될 수 있다.
// ❌ 안티패턴
@Transactional
public void createOrder(Order order) {
    orderRepository.save(order);
    paymentClient.charge(order);       // 외부 API — 커넥션을 물고 대기
    notificationClient.send(order);    // 외부 API — 커넥션을 물고 대기
}

// ✅ 트랜잭션 범위를 최소화
@Transactional
public void saveOrder(Order order) {
    orderRepository.save(order);       // DB 작업만 트랜잭션 안에서
}

public void createOrder(Order order) {
    saveOrder(order);                  // 트랜잭션 종료
    paymentClient.charge(order);       // 트랜잭션 밖에서
    notificationClient.send(order);    // 트랜잭션 밖에서
}
```

3. rollback-only 마킹 — try-catch로도 롤백을 막을 수 없다

```java
// [실무노트] 가장 혼란스러운 함정 중 하나다.
// 내부 빈에서 RuntimeException이 발생하면 Spring은 트랜잭션에 "rollback-only" 마크를 찍는다.
// 외부에서 try-catch로 예외를 삼켜도, 커밋 시점에 마크를 확인하고 롤백한다.
// → UnexpectedRollbackException 발생
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        try {
            inventoryService.decreaseStock(order.getItemId(), order.getQuantity());
        } catch (RuntimeException e) {
// ❌ 예외를 잡았지만 소용없다!
// inventoryService에서 이미 rollback-only가 마킹되었다.
            log.warn("재고 차감 실패, 무시하고 진행", e);
        }
// 여기까지 정상 도달하지만...
// 메서드 종료 시 commit() 호출 → rollback-only 감지 → 롤백!
// → UnexpectedRollbackException 발생
    }
}

@Service
public class InventoryService {
    @Transactional  // 전파 속성 REQUIRED (기본) → 같은 트랜잭션에 참여
    public void decreaseStock(Long itemId, int quantity) {
// ...
        throw new RuntimeException("재고 부족");
// → Spring이 현재 트랜잭션에 rollback-only 마킹
    }
}
```

4. @TransactionalEventListener에서 DML이 동작하지 않는다

```java
// [실무노트] @TransactionalEventListener는 기본 phase가 AFTER_COMMIT이다.
// 트랜잭션이 커밋된 "후"에 실행되지만, 기존 트랜잭션의 커넥션을 그대로 사용한다.
// 이미 커밋이 완료된 커넥션이므로 INSERT/UPDATE/DELETE가 조용히 무시된다.
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(order.getId()));
// → 트랜잭션 커밋 후 리스너 실행
    }
}

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final NotificationRepository notificationRepository;

    // ❌ AFTER_COMMIT(기본) 시점에서 DML이 동작하지 않는다
    @TransactionalEventListener
    public void onOrderCreated(OrderCreatedEvent event) {
// 이미 커밋된 커넥션을 재사용하므로 save()가 DB에 반영되지 않는다.
// 예외도 발생하지 않아 디버깅이 매우 어렵다.
        notificationRepository.save(
            new Notification(event.getOrderId(), "주문 생성됨")
        );  // ❌ DB에 저장되지 않음!
    }
}
```
