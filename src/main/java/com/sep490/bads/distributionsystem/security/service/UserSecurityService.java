package com.sep490.bads.distributionsystem.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
//public class UserSecurityService implements UserDetailsService {
//
//
////    @Override
////    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
////        if(id.isBlank()){
////            throw new UsernameNotFoundException("User not found " + id);
////        }
////        if (user == null) {
////            throw new UsernameNotFoundException("User not found " + id);
////        }
////        return new UserDetailsImpl(user, roles);
////    }
//
//}
