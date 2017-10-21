package com.endless.bank;

/**
 * This interface enable other class and thread to recall a class
 * after finishing its job.
 *
 * @author  Eric Matte
 * @version 1.0
 */
public interface BankCallable {
    void callBack(BankResponse bankResponse);
}
