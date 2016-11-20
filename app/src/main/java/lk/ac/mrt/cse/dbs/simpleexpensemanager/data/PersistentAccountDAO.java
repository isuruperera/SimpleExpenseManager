package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Isuru Tharanga on 11/20/2016.
 */
public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase expMgrDatabase;
    public PersistentAccountDAO(SQLiteDatabase expmgrdatabase) {
        this.expMgrDatabase = expmgrdatabase;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor resultSet = expMgrDatabase.rawQuery("SELECT Account_no FROM Account",null);

        List<String> accounts = new ArrayList<String>();

        if(resultSet.moveToFirst()) {
        accounts.add(resultSet.getString(resultSet.getColumnIndex("Account_no")));
        do {
            accounts.add(resultSet.getString(resultSet.getColumnIndex("Account_no")));
            }
        while (resultSet.moveToNext());
        }

    return accounts;
        }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultSet = expMgrDatabase.rawQuery("SELECT * FROM Account", null);
       // resultSet.moveToFirst();
        List<Account> accounts = new ArrayList<Account>();
        if(resultSet.moveToFirst()) {
        do {

            Account account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
            resultSet.getString(resultSet.getColumnIndex("Holder")),
            resultSet.getString(resultSet.getColumnIndex("Bank")),
            resultSet.getDouble(resultSet.getColumnIndex("Initial_amount")));
            accounts.add(account);
        }
            while (resultSet.moveToNext());
     }

    return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = expMgrDatabase.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);
        //resultSet.moveToFirst();
         Account account = null;
        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        resultSet.getString(resultSet.getColumnIndex("Bank")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("Initial_amount")));
            }
            while (resultSet.moveToNext());
        }

            return account;
    }

    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (Account_no,Bank,Holder,Initial_amount) VALUES (?,?,?,?)";
        SQLiteStatement statement = expMgrDatabase.compileStatement(sql);
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());
        statement.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement statement = expMgrDatabase.compileStatement(sql);
        statement.bindString(1,accountNo);
        statement.executeUpdateDelete();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Initial_amount = Initial_amount + ?";
        SQLiteStatement statement = expMgrDatabase.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }
            statement.executeUpdateDelete();



    }


}
