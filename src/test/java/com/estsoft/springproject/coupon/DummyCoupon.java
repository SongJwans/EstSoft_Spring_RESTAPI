package com.estsoft.springproject.coupon;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

// 테스트 코드에서만 사용되는 테스트 코드
public class DummyCoupon implements ICoupon {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }

    @Override
    public boolean isAppliable(Item item) {
        return false;
    }

    @Override
    public void doExpire() {

    }
}
