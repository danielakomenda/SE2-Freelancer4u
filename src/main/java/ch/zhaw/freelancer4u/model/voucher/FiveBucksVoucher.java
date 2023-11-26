package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

import ch.zhaw.freelancer4u.model.Job;

public class FiveBucksVoucher implements Voucher{

    public FiveBucksVoucher() {}
    
    @Override
    public double getDiscount(List<Job> jobs) {
        double totalEarnings = jobs.stream()
                .mapToDouble(Job::getEarnings)
                .sum();

        if (totalEarnings >= 10) {
            return 5;
        } else {
            return 0;
        }
    }
}
