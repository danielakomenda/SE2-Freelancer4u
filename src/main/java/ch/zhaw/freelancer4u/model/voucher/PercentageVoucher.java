package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

import ch.zhaw.freelancer4u.model.Job;

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
    public double getDiscount(List<Job> jobs) {
        double totalEarnings = jobs.stream()
                .mapToDouble(Job::getEarnings)
                .sum();

        if (totalEarnings > 0) {
            return totalEarnings*discount/100;
        } else {
            return 0;
        }
    }
}
