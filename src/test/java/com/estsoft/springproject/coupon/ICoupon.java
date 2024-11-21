package com.estsoft.springproject.coupon;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

public interface ICoupon {
    String getName();
    boolean isValid();
    int getDiscountPercent();
    public boolean isAppliable(Item item);  // 해당 아이템에 적용 가능 여부
    public void doExpire();
}
