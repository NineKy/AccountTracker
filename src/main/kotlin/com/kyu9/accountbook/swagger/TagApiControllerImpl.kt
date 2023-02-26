package com.kyu9.accountbook.swagger

import com.kyu9.accountbook.application.TagService
import com.kyu9.accountbook.swagger.api.TagApiDelegate
import com.kyu9.accountbook.swagger.model.GetSingleTagDto
import com.kyu9.accountbook.swagger.model.PostSingleTagDto
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class TagApiControllerImpl(
    private var tagService: TagService
): TagApiDelegate{
    override fun postTag(postSingleTagDto: PostSingleTagDto): ResponseEntity<GetSingleTagDto> {
        return ResponseEntity.ok(tagService.storeTag(postSingleTagDto))
    }
}