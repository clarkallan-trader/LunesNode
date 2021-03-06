package io.lunes.state2.patch

import io.lunes.state2.reader.SnapshotStateReader
import io.lunes.state2.{Diff, LeaseInfo, Portfolio}
import scorex.account.Address
import io.lunes.transaction.lease.LeaseTransaction

object CancelLeaseOverflow {
  def apply(s: SnapshotStateReader): Diff = {

    def cancelLeaseOut(l: LeaseInfo): LeaseInfo = LeaseInfo(0, -l.leaseOut)

    val portfolioUpd = s.accountPortfolios
      .collect { case (acc, pf) if pf.spendableBalance < 0 =>
        acc -> Portfolio(0, cancelLeaseOut(pf.leaseInfo), Map.empty)
      }

    val cancelledLeases = for {
      a <- portfolioUpd.keys
      txId <- s.accountTransactionIds(a, Int.MaxValue)
      leaseId <- s.transactionInfo(txId).collect {
        case (_, Some(l: LeaseTransaction)) if (l.sender: Address) == a => l.id()
      }
    } yield (leaseId, false)

    Diff(transactions = Map.empty,
      portfolios = portfolioUpd,
      issuedAssets = Map.empty,
      aliases = Map.empty,
      orderFills = Map.empty,
      leaseState = cancelledLeases.toMap,
      scripts = Map.empty)
  }
}
