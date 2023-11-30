package ch.zhaw.freelancer4u.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import ch.zhaw.freelancer4u.model.Freelancer;
import ch.zhaw.freelancer4u.repository.FreelancerRepository;

class UserValidator implements OAuth2TokenValidator<Jwt> {

    FreelancerRepository freelancerRepository;

    public UserValidator(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required email is missing", null);

        String userEmail = jwt.getClaimAsString("email");
        if (userEmail != null && !userEmail.equals("")) {
            Freelancer f = freelancerRepository.findFirstByEmail(userEmail);
            if (f == null) {
                String username = jwt.getClaimAsString("nickname");
                freelancerRepository.save(new Freelancer(userEmail, username));
            }
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}