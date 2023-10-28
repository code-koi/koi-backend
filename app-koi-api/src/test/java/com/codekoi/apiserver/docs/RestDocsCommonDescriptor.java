package com.codekoi.apiserver.docs;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class RestDocsCommonDescriptor {


    public static FieldDescriptor[] userProfileDto(String subRootPath) {
        return new FieldDescriptor[]{fieldWithPath(subRootPath + ".user").type(JsonFieldType.OBJECT)
                .description("유저 정보"),
                fieldWithPath(subRootPath + ".user.profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지").optional(),
                fieldWithPath(subRootPath + ".user.nickname").type(JsonFieldType.STRING)
                        .description("닉네임"),
                fieldWithPath(subRootPath + ".user.id").type(JsonFieldType.NUMBER)
                        .description("유저 고유 아이디")};


    }
}
