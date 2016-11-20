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
        SQLiteDatabase expmgrdatabase = context.openOrCreateDatabase("expenses", context.MODE_PRIVATE, null);

        expmgrdatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                        "Account_no VARCHAR PRIMARY KEY," +
                        "Initial_amt REAL" +
                " );");

        expmgrdatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amt REAL," +
                "Log_date DATE,"+
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");

        AccountDAO persistentaccountDAO = new PersistentAccountDAO(expmgrdatabase);
        setAccountsDAO(persistentaccountDAO);
        setTransactionsDAO(new PersistentTransactionDAO(expmgrdatabase));





    }
}
