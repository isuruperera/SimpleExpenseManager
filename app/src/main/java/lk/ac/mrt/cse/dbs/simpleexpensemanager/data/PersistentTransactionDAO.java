package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Isuru Tharanga on 11/20/2016.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase expMgrDatabase;
    public PersistentTransactionDAO(SQLiteDatabase expMgrDdatabase) {
        this.expMgrDatabase = expMgrDdatabase;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO TransactionLog (Account_no,Type,Amt,Log_date) VALUES (?,?,?,?)";
        SQLiteStatement statement = expMgrDatabase.compileStatement(sql);
        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());
        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultSet = expMgrDatabase.rawQuery("SELECT * FROM TransactionLog",null);
        resultSet.moveToFirst();
        List<Transaction> transactions = new ArrayList<Transaction>();
        while(resultSet.moveToNext()){
            Transaction transaction = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
            resultSet.getString(resultSet.getColumnIndex("Account_no")),
            (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
            resultSet.getDouble(resultSet.getColumnIndex("Amt")));
            transactions.add(transaction);
         }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = expMgrDatabase.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit,null);
        resultSet.moveToFirst();
        List<Transaction> transactions = new ArrayList<Transaction>();
        while(resultSet.moveToNext()){
            Transaction transaction = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
            resultSet.getString(resultSet.getColumnIndex("Account_no")),
            (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
            resultSet.getDouble(resultSet.getColumnIndex("Amt")));
            transactions.add(transaction);
        }
        return transactions;
    }
}
