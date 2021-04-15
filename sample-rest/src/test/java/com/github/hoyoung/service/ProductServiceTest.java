package com.github.hoyoung.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by HoYoung on 2021/03/18.
 */
@SpringBootTest
@ActiveProfiles(value = "local")
@Slf4j
public class ProductServiceTest {
  @Autowired
  private JPAQueryFactory query;

  private final Function<String, String> fun = name -> "[".concat(name).concat("]");
  private final Function<String, String> fun2 = name -> "[ ".concat(name).concat(" ]");

  @Getter
  @Setter
  class A {
    private String name;
  }

  @Getter
  @Setter
  class B {
    private String name;
  }

  @Getter
  @Setter
  @ToString
  class C {
    private A a;
    private B b;

    public C(A a, B b) {
      this.a = a;
      this.b = b;
    }

    public Object sample() {
      return this.a.getName() + this.b.getName();
    }
  }


  @Test
  public void context() {
    BiFunction<A, B, C> fun = C::new;

    A a = new A();
    a.setName("AAA");
    B b = new B();
    b.setName("BBB");

    C c = fun.apply(a, b);

    log.debug("{}", c.sample());


    log.debug(">>>>>>>>>>>");
//    QProduct qProduct = QProduct.product;
//    QProductOption qProductOption = QProductOption.productOption;
//
//    Map<Product, List<ProductOption>> transform = query.from(qProduct)
//        .innerJoin(qProductOption)
//        .on(qProduct.id.eq(qProductOption.product.id))
//        .where(qProduct.name.eq("aaa"))
//        .transform(GroupBy.groupBy(qProduct).as(GroupBy.list(qProductOption)));
//
//    log.debug("[{}, {}]", transform.size(), ObjectUtils.isEmpty(transform.keySet()));
//
//    transform.forEach((key, value) -> log.debug(">>>> {}, {}", key, value.size()));

//    Function<String, String> fun = name -> "[".concat(name).concat("]");

//    log.debug(">>> {}", fun.);;
//    IntConsumer intConsumer = IntConsumer;

//    LongBinaryOperator longBinaryOperator = (left, right) -> left * right;

//    fun.andThen(() => {})
    ;

//    log.debug(">>> {}", fun.andThen(fun2).apply("APPLE"));;
//    log.debug(">>> {}", longBinaryOperator.applyAsLong(10, 100));;
//
//
//    IntPredicate predicateA = a -> a % 2 == 0;
//    IntPredicate predicateB = b -> b % 3 == 0;
//
//    boolean result;
//    IntPredicate predicateAB = predicateA.and(predicateB);
//    result = predicateAB.test(9);
//    System.out.println("9는 2와 3의 배수입니까? " + result);
//
//    predicateAB = predicateA.or(predicateB);
//    result = predicateAB.test(9);
//    System.out.println("9는 2또는 3의 배수입니까? " + result);
//
//    predicateAB = predicateA.negate();
//    result = predicateAB.test(9);
//    System.out.println("9는 홀수입니까? " + result);



//    log.debug("{}", );
//    fun("aa");
  }

}