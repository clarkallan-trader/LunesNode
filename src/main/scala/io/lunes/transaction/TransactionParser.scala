package io.lunes.transaction

import io.lunes.utils.base58Length
import io.lunes.transaction.assets._
import io.lunes.transaction.assets.exchange.ExchangeTransaction
import io.lunes.transaction.lease.{LeaseCancelTransaction, LeaseTransaction}
import io.lunes.transaction.smart.SetScriptTransaction

import scala.util.{Failure, Try}

object TransactionParser {

  object TransactionType extends Enumeration {
    val GenesisTransaction = Value(1)
    val PaymentTransaction = Value(2)
    val IssueTransaction = Value(3)
    val TransferTransaction = Value(4)
    val ReissueTransaction = Value(5)
    val BurnTransaction = Value(6)
    val ExchangeTransaction = Value(7)
    val LeaseTransaction = Value(8)
    val LeaseCancelTransaction = Value(9)
    val CreateAliasTransaction = Value(10)
    val MassTransferTransaction = Value(11)
    val SetScriptTransaction = Value(12)
    val ScriptTransferTransaction = Value(13)
    val DataTransaction = Value(14)

  }

  val TimestampLength = 8
  val AmountLength = 8
  val TypeLength = 1
  val SignatureLength = 64
  val SignatureStringLength: Int = base58Length(SignatureLength)
  val KeyLength = 32
  val KeyStringLength: Int = base58Length(KeyLength)

  def parseBytes(data: Array[Byte]): Try[Transaction] =
    data.head match {
      case txType: Byte if txType == TransactionType.GenesisTransaction.id =>
        GenesisTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.PaymentTransaction.id =>
        PaymentTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.IssueTransaction.id =>
        IssueTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.TransferTransaction.id =>
        TransferTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.ReissueTransaction.id =>
        ReissueTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.BurnTransaction.id =>
        BurnTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.ExchangeTransaction.id =>
        ExchangeTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.LeaseTransaction.id =>
        LeaseTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.LeaseCancelTransaction.id =>
        LeaseCancelTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.CreateAliasTransaction.id =>
        CreateAliasTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.MassTransferTransaction.id =>
        MassTransferTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.SetScriptTransaction.id =>
        SetScriptTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.ScriptTransferTransaction.id =>
        ScriptTransferTransaction.parseTail(data.tail)

      case txType: Byte if txType == TransactionType.DataTransaction.id =>
        DataTransaction.parseTail(data.tail)

      case txType => Failure(new Exception(s"Invalid transaction type: $txType"))
    }
}
