package br.com.fluo.fluo.database.fw;

import android.content.Context;
import android.util.Log;

public class BOFactory {

    public static TOBase get(Context c, TOBase to) {
        try {
            DAOBase b = null;
            try {
                b = new DAOBase(c);
                return b.get(to);
            } finally {
                b.close();
            }
        } catch (Exception e) {
            Log.e("App", e.getMessage());
            return null;
        }
    }

    public static void insert(Context c, TOBase to) {
        try {
            DAOBase b = new DAOBase(c);
            b.insert(to);
            b.close();
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }


    public static void update(Context c, TOBase to) {
        try {
            DAOBase b = new DAOBase(c);
            b.update(to);
            b.close();
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }

    public static void deleteAll(Context c, TOBase to) {
        try {
            DAOBase b = new DAOBase(c);
            b.deleteAll(to);
            b.close();
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }

    public static void delete(Context c, TOBase to) {
        try {
            DAOBase b = new DAOBase(c);
            b.delete(to);
            b.close();
        } catch (Exception e) {
            Log.e("App", e.getMessage());
        }
    }


}