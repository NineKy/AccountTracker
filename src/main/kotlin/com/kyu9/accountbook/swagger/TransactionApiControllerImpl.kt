package com.kyu9.accountbook.swagger

import com.kyu9.accountbook.application.TransactionService
import com.kyu9.accountbook.swagger.api.TransactionApiDelegate
import com.kyu9.accountbook.swagger.model.GetListTransResponseDto
import com.kyu9.accountbook.swagger.model.GetSingleTransResponseDto
import com.kyu9.accountbook.swagger.model.PostTranRequestDto
import com.kyu9.accountbook.swagger.model.PostTransResponseDto
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TransactionApiControllerImpl(

): TransactionApiDelegate {

    @Autowired
    lateinit var transactionService: TransactionService

    override fun postTransaction(postTranRequestDto: PostTranRequestDto): ResponseEntity<PostTransResponseDto> {
        return ResponseEntity.ok(transactionService.storeFromDto(postTranRequestDto))
    }

    override fun getSingleTransaction(utid: String): ResponseEntity<GetSingleTransResponseDto> {
        return ResponseEntity.ok(transactionService.getSingleTransaction(utid))
    }

    override fun deleteSingleTransaction(utid: String): ResponseEntity<Unit> {
        transactionService.deleteSingleTransaction(utid)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun getAllTransactions(): ResponseEntity<GetListTransResponseDto> {
        return ResponseEntity.ok(transactionService.getAllTransaction())
    }
}