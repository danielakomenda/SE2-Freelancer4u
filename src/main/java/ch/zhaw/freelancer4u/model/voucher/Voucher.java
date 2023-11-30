package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;

import ch.zhaw.freelancer4u.model.Job;

public interface Voucher {
     double getDiscount(List<Job> jobs);
}
