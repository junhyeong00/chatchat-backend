package com.junhyeong.chatchat.models.message;

import com.junhyeong.chatchat.models.commom.Name;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Content {
    @Column(name = "content", length = 2000)
    private String value;

    public Content() {
    }

    public Content(String value) {
        this.value = value;
    }

    public static Content guideMessageOf(Name name) {
        return new Content(
                "안녕하세요, " + name.value() + " 상담 채널입니다.\n\n" +
                        "자주 묻는 질문을 확인하시려면 좌측 하단 말풍선 버튼을 클릭해주세요.\n\n" +
                        "1:1 상담을 원하시면 궁금한 사항을 입력하여 메세지로 전송해주세요. 메세지가 전달되면 상담원이 연결됩니다."
        );
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Content otherContent = (Content) object;

        return Objects.equals(value, otherContent.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
