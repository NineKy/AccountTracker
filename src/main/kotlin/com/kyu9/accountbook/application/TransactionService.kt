package com.kyu9.accountbook.application

import com.kyu9.accountbook.application.repository.UsageTransactionRepoImpl
import com.kyu9.accountbook.common.MyTime
import com.kyu9.accountbook.domain.UsageTransaction
import com.kyu9.accountbook.swagger.model.GetListTransResponseDto
import com.kyu9.accountbook.swagger.model.GetSingleTransResponseDto
import com.kyu9.accountbook.swagger.model.PostTranRequestDto
import com.kyu9.accountbook.swagger.model.PostTransResponseDto
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@RequiredArgsConstructor
class TransactionService(
        private val transactionRepoImpl: UsageTransactionRepoImpl
) {

    fun storeFromDto(tranReqDto: PostTranRequestDto): PostTransResponseDto {
//        val registered = if (tranReqDto.registeredAt == null) MyTime.now() else MyTime.toLocalDateTimeWithYyyymmdd(tranReqDto.registeredAt)

        var registered: LocalDateTime
        if (tranReqDto.registeredAt == null && tranReqDto.registeredAtYyyymmdd == null) {
            registered = MyTime.now()
        }
        else if (tranReqDto.registeredAt == null && tranReqDto.registeredAtYyyymmdd == "") {
            registered = MyTime.now()
        }
        else {
            registered = MyTime.toLocalDateTimeWithYyyymmdd(tranReqDto.registeredAtYyyymmdd!!)
        }

        return transactionRepoImpl.storeEntity(
                UsageTransaction(
                        userId = tranReqDto.userId!!,
                        amount = tranReqDto.amount?.toLong()!!,
                        registered = registered,
                        title = tranReqDto.title!!,
                        content = tranReqDto.content!!,
                        tagId = tranReqDto.tagId?.toLong()!!,
                        moneyType = tranReqDto.moneyType.toString()
                )
        ).let {
            PostTransResponseDto(it.id)
        }
    }

    fun getSingleTransaction(utid: String): GetSingleTransResponseDto {
        return transactionRepoImpl.getEntityWithId(utid).let {
            GetSingleTransResponseDto(
                    userId = it.userId,
                    utid = it.id,
                    amount = it.amount.toInt(),
                    registeredAt = MyTime.toYyyymmddhhmmss(it.registered),
                    title = it.title,
                    content = it.content,
                    tagId = it.tagId.toInt(),
                    moneyType = GetSingleTransResponseDto.MoneyType.valueOf(it.moneyType.toString().uppercase()),
                    created = MyTime.toYyyymmddhhmmss(it.created),
                    updated = MyTime.toYyyymmddhhmmss(it.updated)
            )
        }
    }

    fun deleteSingleTransaction(utid: String) {
        transactionRepoImpl.removeEntityWithId(utid)
    }

    fun getAllTransaction(): GetListTransResponseDto {
        //tag 을 조인해서 가져오는 방식 언젠간 해야함..
        val map = transactionRepoImpl.getAllEntityOrderByCreatedDesc().map {
            GetSingleTransResponseDto(
                    userId = it.userId,
                    utid = it.id,
                    amount = it.amount.toInt(),
                    registeredAt = MyTime.toYyyyMmDd(it.registered),
                    title = it.title,
                    content = it.content,
                    tagId = it.tagId.toInt(),
                    moneyType = GetSingleTransResponseDto.MoneyType.valueOf(it.moneyType.toString().uppercase()),
                    created = MyTime.toYyyymmddhhmmss(it.created),
                    updated = MyTime.toYyyymmddhhmmss(it.updated)
            )
        }

        return GetListTransResponseDto(map)
    }
}