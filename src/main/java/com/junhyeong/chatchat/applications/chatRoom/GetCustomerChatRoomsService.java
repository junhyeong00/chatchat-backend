package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class  GetCustomerChatRoomsService {
    private final CustomerRepository customerRepository;
    private final ChatRoomRepository chatRoomRepository;

    public GetCustomerChatRoomsService(CustomerRepository customerRepository,
                                       ChatRoomRepository chatRoomRepository) {
        this.customerRepository = customerRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChatRoomDto> chatRooms(Username username, Integer page) {
        if (!customerRepository.existsByUsername(username)) {
            throw new Unauthorized();
        }

        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<ChatRoomDto> chatRooms = chatRoomRepository.findAllDtoByCustomer(username, pageable);

        //탈퇴 처리
        return chatRooms;
    }
}
