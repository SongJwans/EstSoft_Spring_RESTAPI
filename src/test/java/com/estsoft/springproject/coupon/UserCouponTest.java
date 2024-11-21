package com.estsoft.springproject.coupon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserCouponTest {

    @Test
    public void 쿠폰이_유효할_경우에만_유저에게_발급한다() {
        User user = new User("area00");
        Assertions.assertEquals(0, user.getTotalCouponCount());

//        ICoupon coupon = new DummyCoupon(); // Dummy coupon을 만들어서 사용
        // Mockito.mock 사용
        ICoupon coupon = Mockito.mock(ICoupon.class);
        // isValid() 호출시 리턴 true
        Mockito.when(coupon.isValid()).thenReturn(true);

        user.addCoupon(coupon);
        Assertions.assertEquals(1, user.getTotalCouponCount());
    }

    @Test
    public void 쿠폰이_유효하지_않을경우_발급안됨() {
        User user = new User("area00");
        Assertions.assertEquals(0, user.getTotalCouponCount());

//        ICoupon coupon = new DummyCoupon(); // Dummy coupon을 만들어서 사용
        // Mockito.mock 사용
        ICoupon coupon = Mockito.mock(ICoupon.class);
        // isValid() 호출시 리턴 true
        Mockito.when(coupon.isValid()).thenReturn(false);

        user.addCoupon(coupon);
        Assertions.assertEquals(0, user.getTotalCouponCount());
    }
}
