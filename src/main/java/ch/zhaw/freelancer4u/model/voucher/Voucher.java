package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

public interface Voucher {
     double getDiscount(List<JobVoucher> jobs);
}
