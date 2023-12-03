package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

public class FiveBucksVoucher implements Voucher{

    public FiveBucksVoucher() {}
    
    @Override
    public double getDiscount(List<JobVoucher> jobs) {
        double totalEarnings = jobs.stream()
                .mapToDouble(JobVoucher::getEarnings)
                .sum();

        if (totalEarnings >= 10) {
            return 5;
        } else {
            return 0;
        }
    }
}
