// Trong com.sep490.bads.distributionsystem.service/CurrentUserDetailsService.java

package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.entity.User;

public interface CurrentUserDetailsService {
    /** Lấy User Entity đầy đủ của người dùng đang đăng nhập. */
    User getCurrentUser();
}