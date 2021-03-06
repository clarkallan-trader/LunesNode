package io.lunes.transaction

import io.lunes.crypto
import io.lunes.state2.{ByteStr, _}
import monix.eval.Coeval

trait SignedTransaction extends ProvenTransaction with Signed {

  protected override def proofField = "signature" -> this.signature.base58

  val signature: ByteStr

  def proofs: Proofs = Proofs.create(Seq(signature)).explicitGet()

  val signatureValid: Coeval[Boolean] = Coeval.evalOnce(crypto.verify(signature.arr, bodyBytes(), sender.publicKey))
}
