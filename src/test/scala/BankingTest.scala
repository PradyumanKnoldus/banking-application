import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class BankingTest extends AnyFunSuite {

  // Test creating a new account with an opening balance
  test("Create a new account with an opening balance") {
    val bank = new Banking()
    val openingBalance = 1000.0
    val account = bank.createAccount(openingBalance)
    assert(account.fetchAccountBalance(account.listAccounts().head._1) == openingBalance)
  }

  // Test creating multiple accounts
  test("Create multiple accounts") {
    val bank = new Banking()
    val oneAccount = bank.createAccount(1000.0)
    val twoAccount = bank.createAccount(2000.0).createAccount(1000.0)
    val threeAccount = bank.createAccount(3000.0).createAccount(1000.0).createAccount(1000.0)
    assert(oneAccount.listAccounts().size == 1)
    assert(twoAccount.listAccounts().size == 2)
    assert(threeAccount.listAccounts().size == 3)
  }

  // Test fetching the balance of an existing account
  test("Fetch the balance of an existing account") {
    val bank = new Banking()
    val openingBalance = 1000.0
    val account = bank.createAccount(openingBalance)
    val accountNumber = account.listAccounts().head._1
    assert(account.fetchAccountBalance(accountNumber) == openingBalance)
  }

  // Test fetching the balance of a non-existing account
  test("Fetch the balance of a non-existing account") {
    val bank = new Banking()
    val accountNumber = Random.nextLong().abs
    assertThrows[NoSuchElementException](bank.fetchAccountBalance(accountNumber))
  }


  test("updateBalance should credit the balance of an existing account") {
    val bank = new Banking()
    val openingBalance = 100.0
    val newAccount = bank.createAccount(openingBalance)
    val accountNumber = newAccount.listAccounts().keys.head
    val transactions = List(Transactions(1, accountNumber, "credit", 25.0))
    val updatedBanking = newAccount.updateBalance(transactions)
    assert(updatedBanking.fetchAccountBalance(accountNumber) == 125.0)
  }

  test("updateBalance should debit the balance of an existing account") {
    val bank = new Banking()
    val openingBalance = 100.0
    val newAccount = bank.createAccount(openingBalance)
    val accountNumber = newAccount.listAccounts().keys.head
    val transactions = List(Transactions(1, accountNumber, "debit", 25.0))
    val updatedBanking = newAccount.updateBalance(transactions)
    assert(updatedBanking.fetchAccountBalance(accountNumber) == 75.0)
  }

  test("updateBalance should update the balance of an existing account (multiple transactions)") {
    val bank = new Banking()
    val openingBalance = 0.0
    val newAccount = bank.createAccount(openingBalance)
    val accountNumber = newAccount.listAccounts().keys.head
    val transactions = List(Transactions(1, accountNumber, "credit", 50.0), Transactions(2, accountNumber, "debit", 25.0))
    val updatedBanking = newAccount.updateBalance(transactions)
    assert(updatedBanking.fetchAccountBalance(accountNumber) == 25.0)
  }

  test("deleteAccount should throw a NoSuchElementException if account does not exist") {
    val bank = new Banking()
    assertThrows[NoSuchElementException] {
      bank.deleteAccount(1234567890)
    }
  }

  test("deleteAccount should delete an existing account") {
    val bank = new Banking(Map(123L -> 100.0, 456L -> 200.0))
    bank.deleteAccount(123L)
  }
}
