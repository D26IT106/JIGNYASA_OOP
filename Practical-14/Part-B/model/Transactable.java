package model;

import exception.InvalidAmountException;
import exception.InsufficientFundsException;

public interface Transactable
{
    void deposit(long amount) throws InvalidAmountException;
    void withdraw(long amount) throws InvalidAmountException, InsufficientFundsException;
}