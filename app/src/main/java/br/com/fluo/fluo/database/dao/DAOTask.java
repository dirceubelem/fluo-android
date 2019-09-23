package br.com.fluo.fluo.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.fluo.fluo.database.fw.Helper;
import br.com.fluo.fluo.database.model.TOTask;
import br.com.fluo.fluo.helper.db.DBHelper;

public class DAOTask extends DBHelper {

    private SQLiteDatabase db = null;

    public DAOTask(Context context) {
        super(context);
    }

    public static List<TOTask> list(Context context) throws Exception {

        DAOTask d = new DAOTask(context);
        List<TOTask> l = new ArrayList<>();

        try {

            TOTask t = new TOTask();

            if (d.db == null) {
                d.db = d.getWritableDatabase();
            }

            String tabela = Helper.obterNomeTabela(t);

            List<Field> colunasChave = Helper.obterColunasChave(t);

            String where = "";

            String[] vs = new String[colunasChave.size()];

            String[] columns = t.getColumns().split(",");

            Cursor cursor = d.db.query(tabela, // a. table
                    columns, // b. column names
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    t = new TOTask();
                    t.load(cursor);
                    l.add(t);
                }
            }

        } finally {
            d.close();
        }

        return l;
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

}
