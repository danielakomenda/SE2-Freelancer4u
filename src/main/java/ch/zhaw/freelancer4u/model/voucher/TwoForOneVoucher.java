package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

import ch.zhaw.freelancer4u.model.JobType;

public class TwoForOneVoucher implements Voucher {
    private JobType jobType;

    public TwoForOneVoucher(JobType jobType) {
        this.jobType = jobType;
    }

    @Override
    public double getDiscount(List<JobVoucher> jobs) {

        double totalEarnings = 0;
        long countOfType = jobs.stream()
                .filter(job -> job.getJobType().equals(jobType))
                .count();

        if (countOfType > 1) {
            totalEarnings = jobs.stream()
                    .filter(job -> job.getJobType().equals(jobType))
                    .mapToDouble(JobVoucher::getEarnings)
                    .sum();
        }

        return totalEarnings / 2;
    }
}
