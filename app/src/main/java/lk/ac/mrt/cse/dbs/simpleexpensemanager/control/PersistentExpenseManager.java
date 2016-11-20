package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.PersistentTransactionDAO;

/**
 * Created by Isuru Tharanga on 11/20/2016.
 */
public class PersistentExpenseManager extends ExpenseManager {

    private Context context;

    public PersistentExpenseManager(Context context){
        this.context = context;
        setup();

    }

    @Override
    public void setup() {
        SQLiteDatabase expMgrDatabase = context.openOrCreateDatabase("140462E", context.MODE_PRIVATE, null);
        //Creates tables if not existing
        expMgrDatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR(50)," +
                "Holder VARCHAR(50),"+
                "Initial_amount REAL" +
                " );");

        expMgrDatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amount REAL," +
                "Log_date DATE," +
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");

        PersistentAccountDAO persistentAccountDAO = new PersistentAccountDAO(expMgrDatabase);
        setAccountsDAO(persistentAccountDAO);
        setTransactionsDAO(new PersistentTransactionDAO(expMgrDatabase));





    }
}
