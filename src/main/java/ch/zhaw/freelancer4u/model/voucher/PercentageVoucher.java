package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

public class PercentageVoucher implements Voucher {
    private int discount;

    public PercentageVoucher(int discount) {
        if (discount >50) {
            throw new RuntimeException("Error: Discount value must less or equal 50.");
        }
        else if (discount <=0 ) {
            throw new RuntimeException("Error: Discount value must be greater zero.");
        }
        else {
        this.discount=discount;
        }
    }


    @Override
    public double getDiscount(List<JobVoucher> jobs) {
        double totalEarnings = jobs.stream()
                .mapToDouble(JobVoucher::getEarnings)
                .sum();

        if (totalEarnings > 0) {
            return totalEarnings*discount/100;
        } else {
            return 0;
        }
    }
}
