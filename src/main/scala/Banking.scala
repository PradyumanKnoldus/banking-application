// I have changed some return types of methods to maintain immutability

import scala.util.Random

class Banking ( private val bankAccounts: Map[Long, Double] = Map.empty ) {

  // Create new account by allocating random account number and depositing balance in account
  def createAccount(openingBalance: Double): Banking = {
    val accountNumber = Random.nextLong().abs
    if (bankAccounts.contains(accountNumber)) {
      createAccount(openingBalance)
    } else {
      new Banking(bankAccounts + (accountNumber -> openingBalance))
    }
  }

  // List all accounts with balance
  def listAccounts(): Map[Long, Double] = {
    bankAccounts
  }

  // Fetch account balance using account number
  def fetchAccountBalance(accountNumber: Long): Double = {
    bankAccounts.getOrElse(accountNumber, throw new NoSuchElementException("Account not found"))
  }

  // Update account balance based on a list of transactions
  def updateBalance(transactions: List[Transactions]): Banking = {
    val updatedAccounts = transactions.foldLeft(bankAccounts) { (accounts, transaction) =>
      val currentBalance = accounts.getOrElse(transaction.accountNumber, 0.0)
      val updatedBalance = transaction.transactionType match {
        case "credit" => currentBalance + transaction.amount
        case "debit" => currentBalance - transaction.amount
        case _ => currentBalance
      }
      accounts + (transaction.accountNumber -> updatedBalance)
    }
    new Banking(updatedAccounts)
  }


  // Delete account using account number
  def deleteAccount(accountNumber: Long): Boolean = {
    if (bankAccounts.contains(accountNumber)) {
      new Banking(bankAccounts - accountNumber)
      true
    } else {
      throw new NoSuchElementException("Account not found")
    }
  }

}
