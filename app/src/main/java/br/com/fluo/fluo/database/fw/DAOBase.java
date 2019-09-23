package br.com.fluo.fluo.database.fw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.fluo.fluo.helper.db.DBHelper;

public class DAOBase extends DBHelper {

    private SQLiteDatabase db = null;

    public DAOBase(Context context) {
        super(context);
    }

    public void insert(TOBase t) throws Exception {

        if (db == null) {
            db = this.getWritableDatabase();
        }

        ContentValues values = Helper.createContentValuesComChave(t);

        String tabela = Helper.obterNomeTabela(t);

        db.insert(tabela, null, values);
    }

    public long update(TOBase t) throws Exception {

        if (db == null) {
            db = this.getWritableDatabase();
        }

        ContentValues values = Helper.createContentValuesSemChave(t);

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        String tabela = Helper.obterNomeTabela(t);

        long x = db.update(tabela, values, where, vs);

        Log.i("Update:", "Total " + x);

        return x;
    }


    public TOBase get(TOBase t) throws Exception {

        if (db == null) {
            db = this.getWritableDatabase();
        }

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        Cursor cursor = db.query(tabela, // a. table
                t.getColumns().split(","), // b. column names
                where, // c. selections
                vs, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null) {
            if (cursor.moveToNext()) {
                t.load(cursor);
            }
        }

        return t;
    }

    public List<TOBase> list(TOBase t) throws Exception {

        List<TOBase> l = new ArrayList<>();

        if (db == null) {
            db = this.getWritableDatabase();
        }

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        Cursor cursor = db.query(tabela, // a. table
                t.getColumns().split(","), // b. column names
                where, // c. selections
                vs, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null) {
            while (cursor.moveToNext()) {
                t.load(cursor);
                l.add(t);
            }
        }

        return l;
    }

    public void deleteAll(TOBase t) throws Exception {
        if (db == null) {
            db = this.getWritableDatabase();
        }

        long x = db.delete(t.getTableName(), null, null);

        Log.i("App", "Total: " + x);
    }

    public void delete(TOBase t) throws Exception {
        if (db == null) {
            db = this.getWritableDatabase();
        }

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        long x = db.delete(t.getTableName(), where, vs);

        Log.i("App", "Total: " + x);
    }

}
