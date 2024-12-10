package com.ecommerce.ECommerceAPI.api.controller.user;

import com.ecommerce.ECommerceAPI.model.Address;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.dao.AddressDAO;
import com.ecommerce.ECommerceAPI.model.dao.LocalUserDAO;
import com.ecommerce.ECommerceAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Rest Controller for user data interactions.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /** The Address DAO. */
    private AddressDAO addressDAO;
    private UserService userService;
    private LocalUserDAO localUserDAO;

    /**
     * Constructor for spring injection.
     * @param addressDAO
     * @param simpMessagingTemplate
     * @param userService
     */
    public UserController(AddressDAO addressDAO,
                          UserService userService, LocalUserDAO localUserDAO) {
        this.addressDAO = addressDAO;
        this.userService = userService;
        this.localUserDAO = localUserDAO;
    }

    /**
     * Gets all addresses for the given user and presents them.
     * @param user The authenticated user account.
     * @param userId The user ID to get the addresses of.
     * @return The list of addresses.
     */
    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        if (!userService.userHasPermissionToUser(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(addressDAO.findByUser_Id(userId));
    }

    /**
     * Allows the user to add a new address.
     * @param user The authenticated user.
     * @param userId The user id for the new address.
     * @param address The Address to be added.
     * @return The saved address.
     */
    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId,
            @RequestBody Address address) {
        if (!userService.userHasPermissionToUser(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);
        Address savedAddress = addressDAO.save(address);
        return ResponseEntity.ok(savedAddress);
    }

    /**
     * Updates the given address.
     * @param user The authenticated user.
     * @param userId The user ID the address belongs to.
     * @param addressId The address ID to alter.
     * @param address The updated address object.
     * @return The saved address object.
     */
    @PatchMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId,
            @PathVariable Long addressId, @RequestBody Address address) {
        if (!userService.userHasPermissionToUser(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (address.getId() == addressId) {
            Optional<Address> opOriginalAddress = addressDAO.findById(addressId);
            if (opOriginalAddress.isPresent()) {
                LocalUser originalUser = opOriginalAddress.get().getUser();
                if (originalUser.getId() == userId) {
                    address.setUser(originalUser);
                    Address savedAddress = addressDAO.save(address);
                    return ResponseEntity.ok(savedAddress);
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }

    //ADMIN - OK
    @GetMapping("/{userId}")
    public ResponseEntity<LocalUser> getLocalUser(@PathVariable Long userId){
        Optional<LocalUser> user = localUserDAO.findById(userId);
        if (user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //ADMIN - OK
    @GetMapping("/search")
    public ResponseEntity<LocalUser> getLocalUserByUsername(@RequestParam String username){
        Optional<LocalUser> user = localUserDAO.findByUsernameIgnoreCase(username);
        if (user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
