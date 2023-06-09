package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCompanyChatRoomsService {
    private final CompanyRepository companyRepository;
    private final ChatRoomRepository chatRoomRepository;

    public GetCompanyChatRoomsService(CompanyRepository companyRepository,
                                      ChatRoomRepository chatRoomRepository) {
        this.companyRepository = companyRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChatRoomDto> chatRooms(Username username, Integer page) {
        if (!companyRepository.existsByUsername(username)) {
            throw new Unauthorized();
        }

        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<ChatRoomDto> chatRooms = chatRoomRepository.findAllDtoByCompany(username, pageable);

        return chatRooms;
    }
}
