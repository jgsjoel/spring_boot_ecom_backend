package com.ddelight.ddAPI.Authentication.services;

import com.ddelight.ddAPI.Authentication.dto.RegisterRequest;
import com.ddelight.ddAPI.account.dto.AddressRequest;
import com.ddelight.ddAPI.account.dto.UpdateRequest;
import com.ddelight.ddAPI.common.entities.Address;
import com.ddelight.ddAPI.common.entities.User;
import com.ddelight.ddAPI.common.exception.DuplicateEntityException;
import com.ddelight.ddAPI.common.exception.NoSuchEntityException;
import com.ddelight.ddAPI.common.repositories.AddressRepo;
import com.ddelight.ddAPI.common.repositories.RoleRepo;
import com.ddelight.ddAPI.common.repositories.UserRepo;
import com.ddelight.ddAPI.common.utils.AuthenticatedUserUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private AuthenticatedUserUtil authenticatedUserUtil;

    private final AddressRepo addressRepo;

    public UserService(UserRepo userRepo,
                       RoleRepo roleRepo,
                       BCryptPasswordEncoder passwordEncoder,
                       AddressRepo addressRepo,
                       AuthenticatedUserUtil authenticatedUserUtil){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.addressRepo = addressRepo;
        this.authenticatedUserUtil = authenticatedUserUtil;
    }

    public void saveUser(RegisterRequest request){
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLaseName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password1()));
        user.setRole(roleRepo.findRolesByRole("ROLE_USER").get());
        user.setActive(false);

        try {
            userRepo.save(user);
        }catch (DataIntegrityViolationException ex){
            throw new DuplicateEntityException("User with same Email already exist!");
        }

    }

    public void activateUserAccount(String email){

        Optional<User> userOptional = userRepo.findByEmail(email);
        if(!userOptional.isPresent()){
            throw new NoSuchEntityException("User Cannot Be found");
        }
        User user = userOptional.get();
        user.setActive(true);
        userRepo.save(user);

    }

    public User getUserDetails() {
        Optional<User> user = userRepo.findByEmail(authenticatedUserUtil.getAuthenticatedUser());
        if (!user.isPresent()){
            throw new NoSuchEntityException("User Not Found");
        }
        return user.get();
    }

    public void updateUser(UpdateRequest request) {

        User user = getUserDetails();
        user.setFirstName(request.firstName());
        user.setLaseName(request.lastName());
        user.setMobile(request.mobile());

        userRepo.save(user);
    }

    public Address getUserAddress() {
        Optional<Address> address = addressRepo.findByUserEmail(authenticatedUserUtil.getAuthenticatedUser());
        return (!address.isPresent()) ? null : address.get();
    }

    public void updateAddress(AddressRequest request) {
        Address address = getUserAddress();
        if(address == null){
            address = new Address();
            address.setUser(getUserDetails());
        }
        address.setAddress(request.address());

        addressRepo.save(address);

    }
}
